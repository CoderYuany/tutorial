### Netty之EventExecutor创建

![](https://github.com/dqqzj/tutorial/blob/master/netty/src/main/resources/pictures/nioeventloop/eventExecutor.png)
- 保存线程执行器`ThreadPerTaskExecutor`
- 创建一个`MpscQueue`
- 创建一个`selector`

```java
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
         //省略无关代码......
            this.children = new EventExecutor[nThreads];
            //省略无关代码......
            for(int i = 0; i < nThreads; ++i) {
                //省略无关代码......
                    this.children[i] = this.newChild((Executor)executor, args);
                   //省略无关代码......
                }

                //省略无关代码......
    }
```

利用循环构建`EventExecutor`其中的`newChild`调用的是`NioEventLoopGroup#newChild`

```java
protected EventLoop newChild(Executor executor, Object... args) throws Exception {
        return new NioEventLoop(this, executor, (SelectorProvider)args[0], ((SelectStrategyFactory)args[1]).newSelectStrategy(), (RejectedExecutionHandler)args[2]);
    }
```

最后调用到`NioEventLoop`的构造器。

```java
NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
        if (selectorProvider == null) {
            throw new NullPointerException("selectorProvider");
        } else if (strategy == null) {
            throw new NullPointerException("selectStrategy");
        } else {
            this.provider = selectorProvider;
            NioEventLoop.SelectorTuple selectorTuple = this.openSelector();
            this.selector = selectorTuple.selector;
            this.unwrappedSelector = selectorTuple.unwrappedSelector;
            this.selectStrategy = strategy;
        }
    }
```

跟进父类构造器到`SingleThreadEventExecutor`

```java
protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
    super(parent);
    this.threadLock = new Semaphore(0);
    this.shutdownHooks = new LinkedHashSet();
    this.state = 1;
    this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
    this.addTaskWakesUp = addTaskWakesUp;
    this.maxPendingTasks = Math.max(16, maxPendingTasks);
    this.executor = (Executor)ObjectUtil.checkNotNull(executor, "executor");
    this.taskQueue = this.newTaskQueue(this.maxPendingTasks);
    this.rejectedExecutionHandler = (RejectedExecutionHandler)ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler");
}
```

这个地方会发现将线程执行器的绑定，并且进行了`NioEventLoop#newTaskQueue`的调用

```java
protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
    return maxPendingTasks == 2147483647 ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxPendingTasks);
}
```

我们发现 `taskQueue`在`NioEventLoop`中默认是`mpsc`队列，`mpsc`队列，即多生产者单消费者队列，`netty`使用`mpsc`，方便的将外部线程的`task`聚集，在`reactor`线程内部用单线程来串行执行，我们可以借鉴`netty`的任务执行模式来处理类似多线程数据上报，定时聚合的应用

其中`openSelector`是调用的jdk底层的方法实现的，传入的`SelectorProvider`是在`NioEventLoopGroup`构造器中初始化的，见一下源码：

```
public NioEventLoopGroup(int nThreads, Executor executor) {
    this(nThreads, executor, SelectorProvider.provider());
}
```

