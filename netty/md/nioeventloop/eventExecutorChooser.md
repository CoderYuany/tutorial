### Netty之EventExecutorChooser创建

![](https://github.com/dqqzj/tutorial/blob/master/netty/src/main/resources/pictures/nioeventloop/eventExecutorChooser.png)
- 判断是否是2的指数`isPowerOfTwo`
- 使用优化的`PowerOfTwoEventExecutorChooser`
  - 进行选择线程执行器来绑定新连接`this.executors[this.idx.getAndIncrement() & this.executors.length - 1]`
- 使用普通的`GenericEventExecutorChooser`
  - 进行选择线程执行器来绑定新连接`this.executors[Math.abs(this.idx.getAndIncrement() % this.executors.length)]`

```java
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
       //省略无关代码......
            this.chooser = chooserFactory.newChooser(this.children);
            //省略无关代码......
    }
```

跟进源码到`DefaultEventExecutorChooserFactory#newChooser`

```java
public EventExecutorChooser newChooser(EventExecutor[] executors) {
        return (EventExecutorChooser)(isPowerOfTwo(executors.length) ? new DefaultEventExecutorChooserFactory.PowerOfTwoEventExecutorChooser(executors) : new DefaultEventExecutorChooserFactory.GenericEventExecutorChooser(executors));
    }
```

根据线程执行器的大小来判断是否为**2的指数**进行选择线程执行器的选择器便于以后从执行器中选择线程来绑定到新的连接到来。

```java
private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        PowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        public EventExecutor next() {
            return this.executors[this.idx.getAndIncrement() & this.executors.length - 1];
        }
    }
```



```java
private static final class GenericEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        public EventExecutor next() {
            return this.executors[Math.abs(this.idx.getAndIncrement() % this.executors.length)];
        }
    }
```

不同之处就在于逻辑运算，位运算比取模运算更加高效。