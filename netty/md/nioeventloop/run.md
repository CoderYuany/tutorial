### Netty之NioEventLoop启动

- 入口`bind()—>execute(task)`

  - 创建线程`startThread()—>doStartThread()`
    - ` ThreadPerTaskEexcutor.execute()`
      - `Thread = Thread.currentThread()`
      - 启动`NioEventLoop.run()`

  源码分析入口`AbstractBootstrap#doBind0`

```java
private static void doBind0(final ChannelFuture regFuture, final Channel channel, final SocketAddress localAddress, final ChannelPromise promise) {
        channel.eventLoop().execute(new Runnable() {
            public void run() {
                if (regFuture.isSuccess()) {
                    channel.bind(localAddress, promise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    promise.setFailure(regFuture.cause());
                }

            }
        });
    }
```

根据方法进入到`SingleThreadEventExecutor#execute`

```java
public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        } else {
            boolean inEventLoop = this.inEventLoop();
            this.addTask(task);
            if (!inEventLoop) {
                this.startThread();
                if (this.isShutdown()) {
                    boolean reject = false;

                    try {
                        if (this.removeTask(task)) {
                            reject = true;
                        }
                    } catch (UnsupportedOperationException var5) {
                    }

                    if (reject) {
                        reject();
                    }
                }
            }

            if (!this.addTaskWakesUp && this.wakesUpForTask(task)) {
                this.wakeup(inEventLoop);
            }

        }
    }
```

根据线程是否启动 `this.inEventLoop()`，显然目前`NioEventLoop`还未启动，所以要执行初始化操作。

```java
private void startThread() {
        if (this.state == 1 && STATE_UPDATER.compareAndSet(this, 1, 2)) {
            try {
                this.doStartThread();
            } catch (Throwable var2) {
                STATE_UPDATER.set(this, 1);
                PlatformDependent.throwException(var2);
            }
        }

    }
```



```java
private void doStartThread() {
        //省略无关代码......
        this.executor.execute(new Runnable() {
            public void run() {
                SingleThreadEventExecutor.this.thread = Thread.currentThread();
                 //省略无关代码......
                label1907: {
                    try {
                         //省略无关代码......
                        SingleThreadEventExecutor.this.run();
                        //省略无关代码......
                    } catch (Throwable var119) {
                         //省略无关代码......
                    } finally {
                        //省略无关代码......
                    }

                     //省略无关代码......
                }
            }
        });
    }
```

启动线程的时候进行绑定`FastThreadLocalThread`到`NioEventLoop`然后执行启动方法。