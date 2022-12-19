package cn.myflv.noactive.core.hook;

import cn.myflv.noactive.constant.CommonConstants;
import cn.myflv.noactive.core.HookHandler;
import cn.myflv.noactive.core.hook.android.ActivitySwitchHook;
import cn.myflv.noactive.core.hook.android.SystemReadyHook;
import cn.myflv.noactive.core.proxy.ActivityManagerService;
import cn.myflv.noactive.core.proxy.AppStandbyController;
import cn.myflv.noactive.core.proxy.NetworkManagementService;
import cn.myflv.noactive.core.proxy.PowerManagerService;
import cn.myflv.noactive.core.proxy.ServiceManager;
import cn.myflv.noactive.core.util.XLog;
import cn.myflv.noactive.server.NoActiveService;
import de.robv.android.xposed.XC_MethodHook;
import lombok.Getter;

/**
 * 安卓系统Hook.
 */
public class Android {

    @Getter
    public static boolean booting = true;

    public static void setBooting(XC_MethodHook.MethodHookParam param) {
        booting = false;
        XLog.i("系统启动完成");
        NoActiveService noActiveService = new NoActiveService();
        ServiceManager.addService(CommonConstants.NO_ACTIVE_SERVICE, noActiveService);
        XLog.i("注入系统服务");
    }

    public static void hook() {
        if (!HookHandler.loadPackageParam.packageName.equals(CommonConstants.ANDROID)) {
            return;
        }
        XLog.i("开始加载Hook");
        SystemReadyHook.hook();
        ActivityManagerService.loadHook();
        PowerManagerService.loadHook();
        NetworkManagementService.loadHook();
        AppStandbyController.loadHook();
        ActivitySwitchHook.hook();
    }

    public static void systemReady(Runnable runnable) {
        if (isBooting()) {
            return;
        }
        runnable.run();
    }

}
