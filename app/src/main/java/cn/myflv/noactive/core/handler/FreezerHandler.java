package cn.myflv.noactive.core.handler;

import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.util.XLog;

public class FreezerHandler {

    public static void freeze(AppInfo appInfo) {
        if (appInfo.isIgnoreApp()) {
            return;
        }
        XLog.i(appInfo.getKey(), "冻结");
    }

    public static void unFreeze(AppInfo appInfo) {
        if (!appInfo.isFrozen()) {
            return;
        }
        XLog.i(appInfo.getKey(), "解冻");
    }

}
