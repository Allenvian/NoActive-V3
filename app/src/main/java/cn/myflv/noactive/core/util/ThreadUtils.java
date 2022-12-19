package cn.myflv.noactive.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.myflv.noactive.wrapper.RunnableWrapper;

public class ThreadUtils {

    private final static String LOCK_KEY_PREFIX = "lock:key:";
    private final static ExecutorService thawActionPool = new ThreadPoolExecutor(2,
            Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    private final static ExecutorService otherActionPool = new ThreadPoolExecutor(4,
            Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    private final static ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
    private final static Map<String, Long> threadTokenMap = new HashMap<>();
    private final static Map<String, Thread> threadMap = new HashMap<>();


    /**
     * 间隔定时执行.
     *
     * @param runnable 执行内容
     * @param minute   间隔分钟
     */
    public static void scheduleInterval(Runnable runnable, int minute) {
        scheduledThreadPool.scheduleWithFixedDelay(runnable, 0, minute, TimeUnit.MINUTES);
    }

    /**
     * 新线程执行.
     *
     * @param runnable 执行内容
     */
    public static void newThread(Runnable runnable) {
        otherActionPool.execute(() -> safeRun(runnable));
    }

    /**
     * 解冻线程.
     *
     * @param key      key
     * @param runnable 执行方法
     */
    public static void thawThread(String key, Runnable runnable) {
        thawActionPool.execute(() -> {
            synchronized (threadTokenMap) {
                threadTokenMap.remove(key);
            }
            runWithLock(key, runnable);
        });
    }


    /**
     * 新开线程.
     *
     * @param key      线程Key
     * @param runnable 执行方法
     * @param delay    延迟
     */
    public static void newThread(String key, Runnable runnable, long delay) {
        long currentToken = System.currentTimeMillis();
        otherActionPool.execute(() -> {
            synchronized (threadTokenMap) {
                threadTokenMap.put(key, currentToken);
            }
            sleep(delay);
            synchronized (threadTokenMap) {
                if (!Objects.equals(threadTokenMap.get(key), currentToken)) {
                    XLog.d(key + " thread updated");
                    return;
                }
            }
            runWithLock(key, runnable);
        });
    }

    /**
     * 锁key执行.
     *
     * @param key      线程Key
     * @param runnable 执行方法
     */
    public static void runWithLock(String key, Runnable runnable) {
        synchronized (threadMap) {
            Optional.ofNullable(threadMap.remove(key)).ifPresent(Thread::interrupt);
            threadMap.put(key, Thread.currentThread());
        }
        synchronized (getLockKey(key)) {
            safeRun(runnable);
        }
        synchronized (threadMap) {
            if (Thread.currentThread().equals(threadMap.get(key))) {
                threadMap.remove(key);
            }
        }
    }

    /**
     * 获取锁Key.
     *
     * @param key 线程Key
     * @return 锁Key
     */
    public static String getLockKey(String key) {
        return (LOCK_KEY_PREFIX + key).intern();
    }

    /**
     * 延迟.
     *
     * @param ms 毫秒
     */
    public static boolean sleep(int ms) {
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException ignored) {
            return false;
        }
    }

    /**
     * 延迟.
     *
     * @param ms 毫秒
     */
    public static void sleep(long ms) {
        runNoThrow(() -> Thread.sleep(ms));
    }


    /**
     * 打印调用堆栈.
     */
    public static void printStackTrace(Throwable throwable) {
        XLog.e("---------------> ");
        XLog.e(throwable.getMessage());
        StackTraceElement[] stackElements = throwable.getStackTrace();
        for (StackTraceElement element : stackElements) {
            XLog.e("at " + element.getClassName() + "." + element.getMethodName() +
                    "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
        }
        XLog.e(" <---------------");
    }

    public static void safeRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            printStackTrace(throwable);
        }
    }

    public static void runNoThrow(RunnableWrapper runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {
        }
    }


}
