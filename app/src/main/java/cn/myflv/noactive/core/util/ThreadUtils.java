package cn.myflv.noactive.core.util;

import cn.myflv.noactive.wrapper.RunnableWrapper;

public class ThreadUtils {

    public void runNoThrow(RunnableWrapper runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            XLog.e(throwable.getMessage());
        }
    }

}
