### Netty之NioEventLoopGroup创建

![](https://github.com/dqqzj/tutorial/blob/master/netty/src/main/resources/pictures/nioeventloop/build.png)
创建步骤：

- 默认线程组是2*cpu的`NioEventLoopGroup`

  - 创建`ThreadPerTaskExecutor`线程创建器

  - 通过`for`循环构造`EventExecutor[^1]`线程组

    [^1]: NioEventLoop

  - 通过`DefaultEventExecutorChooserFactory`构造`EventExecutorChooser`线程选择器

    

  **源码解读：**

  服务端创建入口`EventLoopGroup boss = new NioEventLoopGroup();`

  会发现其实在`MultithreadEventLoopGroup`的构造器中进行了分配2*cpu个数线程

  ```java
  protected MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args) {
      super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args);
  }
  ```

`DEFAULT_EVENT_LOOP_THREADS`定义如下：

```java
private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
```

继续进行`MultithreadEventExecutorGroup`构造器的初始化

```java
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
    this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
}
```

此处添加了一个线程选择器工厂，便于后边的线程选择器创建。

最后的创建逻辑在

```java
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
    //省略无关代码......
        if (executor == null) {
            executor = new ThreadPerTaskExecutor(this.newDefaultThreadFactory());
        }

        this.children = new EventExecutor[nThreads];

        int j;
        for(int i = 0; i < nThreads; ++i) {
            boolean success = false;
            boolean var18 = false;

            try {
                var18 = true;
                this.children[i] = this.newChild((Executor)executor, args);
                success = true;
                var18 = false;
            } //省略无关代码......
        this.chooser = chooserFactory.newChooser(this.children);
        //省略无关代码......
    }
}
```

后续进行详细分析每一个逻辑。