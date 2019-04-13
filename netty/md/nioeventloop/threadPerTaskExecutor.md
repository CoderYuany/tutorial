### Netty之ThreadPerTaskExecutor创建

![](https://github.com/dqqzj/tutorial/blob/master/netty/src/main/resources/pictures/nioeventloop/threadPerTaskExecutor.png)
- 每一次执行任务都会创建一个线程实体
- `NioEventLoop`线程命名规则`nioEventLoop-group-xx`[**group表示NioEventLoop的分组**]

```java
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
    //省略无关代码......
        if (executor == null) {
            executor = new ThreadPerTaskExecutor(this.newDefaultThreadFactory());
        }
       //省略无关代码......
}
```

看一下`ThreadPerTaskExecutor`类的定义可以发现

```java
public final class ThreadPerTaskExecutor implements Executor {
    private final ThreadFactory threadFactory;

    public ThreadPerTaskExecutor(ThreadFactory threadFactory) {
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        } else {
            this.threadFactory = threadFactory;
        }
    }

    public void execute(Runnable command) {
        this.threadFactory.newThread(command).start();
    }
}
```

每一次执行任务都说通过`ThreadFactory`去新建一个线程来执行的。

回过头来看一下这个工厂到底是如何初始化的

```java
public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
    this(toPoolName(poolType), daemon, priority);
}
```

`toPoolName`这个方法

```java
public static String toPoolName(Class<?> poolType) {
    if (poolType == null) {
        throw new NullPointerException("poolType");
    } else {
        String poolName = StringUtil.simpleClassName(poolType);
        switch(poolName.length()) {
        case 0:
            return "unknown";
        case 1:
            return poolName.toLowerCase(Locale.US);
        default:
            return Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1)) ? Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1) : poolName;
        }
    }
}
```

就是将`NioEventLoop`的第一个字母转化为了小写。

```java
public DefaultThreadFactory(String poolName, boolean daemon, int priority, ThreadGroup threadGroup) {
    this.nextId = new AtomicInteger();
    if (poolName == null) {
        throw new NullPointerException("poolName");
    } else if (priority >= 1 && priority <= 10) {
        this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
        this.daemon = daemon;
        this.priority = priority;
        this.threadGroup = threadGroup;
    } else {
        throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
    }
}
```

这段源码可以看出来我们最开始说的线程命名规则的前缀是如何生成的，每创建一个`DefaultThreadFactory`它的

`poolId`都会增加，也就是说同一个`DefaultThreadFactory`的线程应该是属于同一个分组的。



```java
public Thread newThread(Runnable r) {
    Thread t = this.newThread(FastThreadLocalRunnable.wrap(r), this.prefix + this.nextId.incrementAndGet());

    try {
        if (t.isDaemon() != this.daemon) {
            t.setDaemon(this.daemon);
        }

        if (t.getPriority() != this.priority) {
            t.setPriority(this.priority);
        }
    } catch (Exception var4) {
    }

    return t;
}
```

最开始讲到，每一个任务都会创建新的线程去执行，完整的线程命名就在

`DefaultThreadFactory#newThread`方法中体现出来了。

```java
protected Thread newThread(Runnable r, String name) {
    return new FastThreadLocalThread(this.threadGroup, r, name);
}
```

可以看出来此处使用的不是java默认的线程，而是经过包装自定义了的，而且进行了本地线程优化了的线程模型。