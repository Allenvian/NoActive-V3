package cn.myflv.noactive.core.proxy;

import java.util.Objects;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;

public class PowerManagerService {

    private static Object instance;

    private static void setInstance(XC_MethodHook.MethodHookParam param) {
        if (Objects.nonNull(instance)) {
            return;
        }
        instance = param.thisObject;
        XLog.i("PowerManagerService 获取成功");
    }

    public static void loadHook() {
        HookHelpers.hookAfter(ClassConstants.PowerManagerService, MethodConstants.onStart, PowerManagerService::setInstance);
        XLog.i("PowerManagerService hook 成功");
    }
}
