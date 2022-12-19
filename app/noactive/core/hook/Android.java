package cn.myflv.noactive.core.hook;

import cn.myflv.noactive.core.hook.android.ActivitySwitchHook;
import cn.myflv.noactive.core.hook.android.SystemReadyHook;
import cn.myflv.noactive.core.proxy.ActivityManagerService;
import cn.myflv.noactive.core.proxy.AppStandbyController;
import cn.myflv.noactive.core.proxy.NetworkManagementService;
import cn.myflv.noactive.core.proxy.PowerManagerService;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;

/**
 * 安卓系统Hook.
 */
public class Android {

    public static boolean systemReady = false;

    public static void setSystemReady(XC_MethodHook.MethodHookParam param) {
        systemReady = true;
        XLog.i("系统启动完成");
    }

    public static void hook() {
        XLog.i("开始加载Hook");
        SystemReadyHook.hook();
        ActivityManagerService.loadHook();
        PowerManagerService.loadHook();
        NetworkManagementService.loadHook();
        AppStandbyController.loadHook();
        ActivitySwitchHook.hook();
    }

}
