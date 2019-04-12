## Netty之Channel端口绑定

![](https://github.com/dqqzj/tutorial/blob/master/netty/src/main/resources/pictures/channel/bind.png)
端口绑定步骤

- 端口绑定入口`AbstractUnsafe#bind`
  -  实际绑定方法`AbstractUnsafe#doBind`
    - jdk底层绑定`NioServerSocketChannel#doBind`
  - 事件传播`DefaultChannelPipeline#fireChannelActive`
  - `HeadContext#readIfIsAutoRead`

调用`AbstractBootstrap#doBind`中的`AbstractBootstrap#doBind0`，代码如下：

```java
private ChannelFuture doBind(final SocketAddress localAddress) {
   //省略其他代码......
    AbstractBootstrap.doBind0(regFuture, channel, localAddress, promise);
   //省略其他代码......
}
```

继续跟进源码到`AbstractChannel#bind`

```java
public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
    return this.pipeline.bind(localAddress, promise);
}
```

调用到`DefaultChannelPipeline#bind`

```java
public final ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
    return this.tail.bind(localAddress, promise);
}
```

然后调用到`AbstractChannelHandlerContext#bind`

```java
public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
   //省略无关代码......
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeBind(localAddress, promise);
        } else {
            safeExecute(executor, new Runnable() {
                public void run() {
                    next.invokeBind(localAddress, promise);
                }
            }, promise, (Object)null);
        }
    //省略无关代码......
    }
}
```

实际调用了`AbstractChannelHandlerContext#invokeBind`

```java
private void invokeBind(SocketAddress localAddress, ChannelPromise promise) {
    if (this.invokeHandler()) {
        try {
            ((ChannelOutboundHandler)this.handler()).bind(this, localAddress, promise);
        } catch (Throwable var4) {
            notifyOutboundHandlerException(var4, promise);
        }
    } else {
        this.bind(localAddress, promise);
    }

}
```

根据源码会发现此处调用的是`DefaultChannelPipeline`中的内部类`HeadContext`中的`bind`方法，该类定义如下：

```java
final class HeadContext extends AbstractChannelHandlerContext implements ChannelOutboundHandler, ChannelInboundHandler {
  //省略无关代码......
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
            this.unsafe.bind(localAddress, promise);
        }
  //省略无关代码......
}
```

#### 端口绑定入口

绕了一大圈又回到了`AbstractChannel`的内部类`AbstractUnsafe`中

```java
protected abstract class AbstractUnsafe implements Unsafe {
  //省略无关代码......
  public final void bind(SocketAddress localAddress, ChannelPromise promise) {
            //省略无关代码......
                boolean wasActive = AbstractChannel.this.isActive();
                try {
                    AbstractChannel.this.doBind(localAddress);
                } catch (Throwable var5) {
                    this.safeSetFailure(promise, var5);
                    this.closeIfClosed();
                    return;
                }

                if (!wasActive && AbstractChannel.this.isActive()) {
                    this.invokeLater(new Runnable() {
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelActive();
                        }
                    });
                }

                this.safeSetSuccess(promise);
            }
        }
  //省略无关代码......
}
```

最终的绑定是在`NioServerSocketChannel#doBind`

```java
protected void doBind(SocketAddress localAddress) throws Exception {
    if (PlatformDependent.javaVersion() >= 7) {
        this.javaChannel().bind(localAddress, this.config.getBacklog());
    } else {
        this.javaChannel().socket().bind(localAddress, this.config.getBacklog());
    }

}
```

通过`AbstractChannel.this.pipeline.fireChannelActive();`的调用会直接进入到`DefaultChannelPipeline#channelActive`

```java
public void channelActive(ChannelHandlerContext ctx) {
    ctx.fireChannelActive();
    this.readIfIsAutoRead();
}
```

事件传播的调用就在于`ctx.fireChannelActive();`，当你实现了在`netty`的服务端实现了`handler`情况下，就会触发相应的事件，类似于我的案例中的

```java
static class ServerHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println(o);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("this DefaultChannelPipeline#channelActive in invoked！");
    }
}
```

`DefaultChannelPipeline#readIfIsAutoRead`检测通道的自动读取，如果`autoRead`打开了，那么会在连接打开和建立的时候默认调用`Channel`的`read`方法，

```java
private void readIfIsAutoRead() {
    if (DefaultChannelPipeline.this.channel.config().isAutoRead()) {
        DefaultChannelPipeline.this.channel.read();
    }

}
```

然后最终会调用到`AbstractNioChannel#doBeginRead`方法

```java
protected void doBeginRead() throws Exception {
    SelectionKey selectionKey = this.selectionKey;
    if (selectionKey.isValid()) {
        this.readPending = true;
        int interestOps = selectionKey.interestOps();
        if ((interestOps & this.readInterestOp) == 0) {
            selectionKey.interestOps(interestOps | this.readInterestOp);
        }

    }
}
```