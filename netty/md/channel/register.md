### Netty之Channel注册
![](https://github.com/dqqzj/tutorial/blob/master/netty/src/main/resources/pictures/channel/register.png)

注册`selector`步骤

- `NioEventLoopGroup`的初始化
- `EventExecutorChooser`的创建流程

- `AbstractChannel.register(channel)`注册入口
  - `register0()`实际注册入口
  - `doRegister()`调用jdk底层进行注册
  - `invokeHandlerAddedIfNeeded`回调处理器
  - `fireChannelRegistered`事件传播

在`AbstractBootstrap#initAndRegister`中又这么一段代码：

```java
final ChannelFuture initAndRegister() {
   //省略无关代码......
    ChannelFuture regFuture = this.config().group().register(channel);
   //省略无关代码......
    return regFuture;
}
```

**NioEventLoopGroup的初始化流程**

这段代码就是`channel`注册到`selector`的入口。跟进`this.config().group()`找到源码部分位于`AbstractBootstrapConfig#group`实际代码如下：

```java
public final EventLoopGroup group() {
    return this.bootstrap.group();
}
```

首先第一步分析这个`bootstrap`成员变量是如何初始化的。

```java
public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
    super.group(parentGroup);
    //省略无关代码......
}
```

关键地方还是`ServerBootstrap`的成员变量`ServerBootstrapConfig`初始化

```java
private final ServerBootstrapConfig config = new ServerBootstrapConfig(this);
```

```java
public final class ServerBootstrapConfig extends AbstractBootstrapConfig<ServerBootstrap, ServerChannel> {
    ServerBootstrapConfig(ServerBootstrap bootstrap) {
        super(bootstrap);
    }
}
```

到了这里就完成了`EventLoopGroup`的初始化流程分析。

重点来了，到底是如何进行注册到`selector`的呢？上述分析的`EventLoopGroup`实际上是服务端创建的`NioEventLoopGroup`，`NioEventLoopGroup`是继承于`MultithreadEventLoopGroup`，所以注册的真正入口是

这个`MultithreadEventLoopGroup#register`

```java
public ChannelFuture register(Channel channel) {
    return this.next().register(channel);
}
```

跟进源码会发现实际委托了父类`MultithreadEventExecutorGroup`的成员变量`EventExecutorChooser`进行注册

```
public EventExecutor next() {
    return this.chooser.next();
}
```

**EventExecutorChooser的创建流程**

在`MultithreadEventExecutorGroup`构造器中的关键一段代码

> this.chooser = chooserFactory.newChooser(this.children);

该`chooserFactory`工厂实际是`DefaultEventExecutorChooserFactory.INSTANCE`

```java
public EventExecutorChooser newChooser(EventExecutor[] executors) {
    return (EventExecutorChooser)(isPowerOfTwo(executors.length) ? new DefaultEventExecutorChooserFactory.PowerOfTwoEventExecutorChooser(executors) : new DefaultEventExecutorChooserFactory.GenericEventExecutorChooser(executors));
}
```

这段代码首先判断传入`EventExecutor`数组的长度是否为**2的指数**，返回一个`EventExecutorChooser`选择器，

说白了原因是**位操作&  比 % 操作要高效**。netty为了提高效率也是拼了。

所以在`NioEventLoopGroup`的构造器中最好传入**2*n**这样的哦

根据`MultithreadEventExecutorGroup`中构造器的这段代码

> this.children[i] = this.newChild((Executor)executor, args); 

能够定位到`NioEventLoopGroup#newChild`

```java
protected EventLoop newChild(Executor executor, Object... args) throws Exception {
    return new NioEventLoop(this, executor, (SelectorProvider)args[0], ((SelectStrategyFactory)args[1]).newSelectStrategy(), (RejectedExecutionHandler)args[2]);
}
```

**注册入口**

注册入口是MultithreadEventExecutorGroup#register`方法

```java
public ChannelFuture register(Channel channel) {
        return this.next().register(channel);
    }
```

跟进源码继续调用`SingleThreadEventLoop#register`

```java
public ChannelFuture register(Channel channel) {
    return this.register((ChannelPromise)(new DefaultChannelPromise(channel, this)));
}
```

最后调用`AbstractChannel#register`

```java
public final void register(EventLoop eventLoop, final ChannelPromise promise) {
    //省略无关代码......
        if (eventLoop.inEventLoop()) {
            this.register0(promise);
        } else {
            try {
                eventLoop.execute(new Runnable() {
                    public void run() {
                        AbstractUnsafe.this.register0(promise);
                    }
                });
            } catch (Throwable var4) {
               //省略无关代码......
            }
        }
}
```

`AbstractEventExecutor`中比较常用的一个方法是`inEventLoop`
 这个是判断当前线程是否在事件循环线程中，因为为了保证一个`Channel`上的事件处理的线程安全，要把所有的IO事件等使用IO线程来处理，就需要判断当前线程是否是`eventLoop`线程，如果是则可以直接执行，否则需要提交给`eventLoop`线程去执行。

```java
private void register0(ChannelPromise promise) {
    try {
        if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
            return;
        }

        boolean firstRegistration = this.neverRegistered;
        AbstractChannel.this.doRegister(); 
        this.neverRegistered = false;
        AbstractChannel.this.registered = true;
        AbstractChannel.this.pipeline.invokeHandlerAddedIfNeeded();
        this.safeSetSuccess(promise);
        AbstractChannel.this.pipeline.fireChannelRegistered();
        if (AbstractChannel.this.isActive()) {
            if (firstRegistration) {
                AbstractChannel.this.pipeline.fireChannelActive();
            } else if (AbstractChannel.this.config().isAutoRead()) {
                this.beginRead();
            }
        }
    } catch (Throwable var3) {
        this.closeForcibly();
        AbstractChannel.this.closeFuture.setClosed();
        this.safeSetFailure(promise, var3);
    }

}
```

首先判断是否注册过，若过没有注册过就执行`AbstractNioChannel#doRegister`

```java
protected void doRegister() throws Exception {
    boolean selected = false;

    while(true) {
        try {
            this.selectionKey = this.javaChannel().register(this.eventLoop().unwrappedSelector(), 0, this);
            return;
        } catch (CancelledKeyException var3) {
            if (selected) {
                throw var3;
            }

            this.eventLoop().selectNow();
            selected = true;
        }
    }
}
```

调用jdk底层的nio实现注册，`this.javaChannel()`是我们前面创建`channel`的时候保存的成员变量。后续分析注册中的其他几个方法逻辑。

