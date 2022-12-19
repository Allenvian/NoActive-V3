package cn.myflv.noactive.core.handler;

import java.util.List;

import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.entity.ProcessRecord;
import cn.myflv.noactive.core.util.ThreadUtils;
import cn.myflv.noactive.core.util.XLog;

public class FreezerHandler {

    public static void freezeIfNeed(AppInfo appInfo) {
        if (appInfo.isIgnoreApp()) {
            return;
        }
        ThreadUtils.newThread(appInfo.getKey(), () -> freeze(appInfo), 3000);
    }

    public static void freeze(AppInfo appInfo) {
        XLog.i(appInfo.getKey(), "冻结");
    }

    public static void unFreezeIfNeed(AppInfo appInfo) {
        if (!appInfo.isFrozen()) {
            return;
        }
        ThreadUtils.thawThread(appInfo.getKey(), () -> unFreeze(appInfo));
    }

    public static void unFreeze(AppInfo appInfo) {
        XLog.i(appInfo.getKey(), "解冻");
    }

}
