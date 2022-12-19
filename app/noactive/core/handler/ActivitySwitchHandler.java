package cn.myflv.noactive.core.handler;

import cn.myflv.noactive.constant.CommonConstants;
import cn.myflv.noactive.core.entity.AppInfo;

public class ActivitySwitchHandler {
    /**
     * 上一个App.
     */
    public static AppInfo lastApp = AppInfo.getInstance(CommonConstants.ANDROID);

    public static void newEvent(AppInfo currentApp) {
        // 解冻当前App
        FreezerHandler.unFreeze(currentApp);
        // 冻结当前App
        FreezerHandler.freeze(lastApp);
        lastApp = currentApp;
    }

}
