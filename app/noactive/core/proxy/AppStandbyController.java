package cn.myflv.noactive.core.proxy;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;

public class AppStandbyController {

    private static Object instance;

    private static void setInstance(XC_MethodHook.MethodHookParam param) {
        instance = param.thisObject;
        XLog.i("AppStandbyController 获取成功");
    }

    public static void loadHook() {
        HookHelpers.config().className(ClassConstants.AppStandbyController).methodName(MethodConstants.onBootPhase)
                .params(int.class).hookAfter(AppStandbyController::setInstance).hook();
        XLog.i("AppStandbyController hook 成功");
    }
}
