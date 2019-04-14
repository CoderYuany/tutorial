### Netty之NioEventLoop执行

- 入口`run()—>while(true)`
  - 检查是否有io事件`select()`
  - 处理io事件`processSelectedkeys()`
  - 异步处理任务`runAllTasks()`

```java
protected void run() {
        while(true) {
            while(true) {
                while(true) {
                    try {
                        try {
                            switch(this.selectStrategy.calculateStrategy(this.selectNowSupplier, this.hasTasks())) {
                            case -3:
                            case -1:
                                this.select(this.wakenUp.getAndSet(false));
                                if (this.wakenUp.get()) {
                                    this.selector.wakeup();
                                }
                                break;
                            case -2:
                                continue;
                            }
                        } catch (IOException var23) {
                            this.rebuildSelector0();
                            handleLoopException(var23);
                            continue;
                        }

                        this.cancelledKeys = 0;
                        this.needsToSelectAgain = false;
                        int ioRatio = this.ioRatio;
                        if (ioRatio == 100) {
                            try {
                                this.processSelectedKeys();
                            } finally {
                                this.runAllTasks();
                            }
                        } else {
                            long ioStartTime = System.nanoTime();
                            boolean var14 = false;

                            try {
                                var14 = true;
                                this.processSelectedKeys();
                                var14 = false;
                            } finally {
                                if (var14) {
                                    long ioTime = System.nanoTime() - ioStartTime;
                                    this.runAllTasks(ioTime * (long)(100 - ioRatio) / (long)ioRatio);
                                }
                            }

                            long ioTime = System.nanoTime() - ioStartTime;
                            this.runAllTasks(ioTime * (long)(100 - ioRatio) / (long)ioRatio);
                        }
                    } catch (Throwable var24) {
                        handleLoopException(var24);
                    }
                    break;
                }

                try {
                    if (this.isShuttingDown()) {
                        this.closeAll();
                        if (this.confirmShutdown()) {
                            return;
                        }
                    }
                } catch (Throwable var20) {
                    handleLoopException(var20);
                }
            }
        }
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

初始化值`private volatile int ioRatio = 50;`然后记录了执行io事件的处理时间，最后调用`this.runAllTasks(ioTime * (long)(100 - ioRatio) / (long)ioRatio);`并传入的时间和处理io事件的时间是一样。