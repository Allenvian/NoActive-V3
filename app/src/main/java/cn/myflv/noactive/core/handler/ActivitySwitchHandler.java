package cn.myflv.noactive.core.handler;

import cn.myflv.noactive.constant.CommonConstants;
import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.util.XLog;

public class ActivitySwitchHandler {
    /**
     * 上一个App.
     */
    public static AppInfo lastApp = AppInfo.getInstance(CommonConstants.ANDROID);

    public static void newEvent(AppInfo currentApp) {
        if (lastApp.getKey().equals(currentApp.getKey())) {
            return;
        }
        XLog.d("当前应用", currentApp.getKey());
        // 解冻当前App
        FreezerHandler.unFreezeIfNeed(currentApp);
        // 冻结当前App
        FreezerHandler.freezeIfNeed(lastApp);
        lastApp = currentApp;
    }

}
