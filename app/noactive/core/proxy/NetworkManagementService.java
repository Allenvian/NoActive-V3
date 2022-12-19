package cn.myflv.noactive.core.proxy;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;

public class NetworkManagementService {
    private static Object instance;

    private static void setInstance(XC_MethodHook.MethodHookParam param) {
        instance = param.thisObject;
        XLog.i("NetworkManagementService 获取成功");
    }
    public static void loadHook() {
        HookHelpers.hookAfter(ClassConstants.NetworkManagementService, MethodConstants.systemReady, NetworkManagementService::setInstance);
        XLog.i("NetworkManagementService hook 成功");
    }
}
