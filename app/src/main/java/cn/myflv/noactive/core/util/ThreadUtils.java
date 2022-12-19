package cn.myflv.noactive.core.util;

public class ThreadUtils {

    public void runNoThrow(RunnableWrapper runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            XLog.e(throwable.getMessage());
        }
    }

    @FunctionalInterface
    public interface RunnableWrapper {
        void run() throws Throwable;
    }

}
