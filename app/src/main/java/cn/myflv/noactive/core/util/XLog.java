package cn.myflv.noactive.core.util;

import cn.myflv.noactive.constant.CommonConstants;
import de.robv.android.xposed.XposedBridge;

public class XLog {

    private final static String DEBUG = "[DEBUG]";
    private final static String INFO = "[INFO]";
    private final static String WARN = "[WARN]";
    private final static String ERROR = "[ERROR]";

    private static void unify(String level, String... msg) {
        String message = String.join(CommonConstants.SPACE, msg);
        XposedBridge.log(String.join(CommonConstants.SPACE, CommonConstants.NO_ACTIVE, level, message));
    }

    public static void d(String... msg) {
        unify(DEBUG, msg);
    }

    public static void i(String... msg) {
        unify(INFO, msg);
    }

    public static void w(String... msg) {
        unify(WARN, msg);
    }

    public static void e(String... msg) {
        unify(ERROR, msg);
    }

}
