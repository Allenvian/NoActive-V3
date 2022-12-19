package cn.myflv.noactive.core.util;

public class ThreadUtils {

    @FunctionalInterface
    public interface RunnableWrapper {
        void run() throws Throwable;
    }

    public void runNoThrow(RunnableWrapper runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            XLog.e(throwable.getMessage());
        }
    }

}
