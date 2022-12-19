package cn.myflv.noactive.core.hook.android;

import android.app.usage.UsageEvents;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import java.util.Arrays;
import java.util.List;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.handler.ActivitySwitchHandler;
import cn.myflv.noactive.core.hook.Android;
import cn.myflv.noactive.core.util.HookHelpers;
import de.robv.android.xposed.XC_MethodHook;

/**
 * 应用切换Hook.
 */
public class ActivitySwitchHook {
    /**
     * 监听的事件列表.
     */
    private final static List<Integer> listenEventList = Arrays.asList(UsageEvents.Event.ACTIVITY_RESUMED, UsageEvents.Event.ACTIVITY_PAUSED);

    /**
     * Hook方法.
     */
    public static void hook() {
        HookHelpers.config().className(ClassConstants.ActivityManagerService).methodName(MethodConstants.updateActivityUsageStats)
                .params(ClassConstants.ComponentName, int.class, int.class, ClassConstants.IBinder, ClassConstants.ComponentName)
                .hookBefore(ActivitySwitchHook::send).hook();
    }

    /**
     * 发送至handler.
     *
     * @param param 方法参数
     */
    private static void send(XC_MethodHook.MethodHookParam param) {
        // 系统未启动不执行
        if (!Android.systemReady) {
            return;
        }
        // 切换事件
        int event = (int) param.args[2];
        if (!listenEventList.contains(event)) {
            return;
        }
        // 切换用户
        int userId = (int) param.args[1];
        // 切换应用
        String packageName = ((ComponentName) param.args[0]).getPackageName();
        AppInfo appInfo = AppInfo.getInstance(userId, packageName);
        ActivitySwitchHandler.newEvent(appInfo);
    }
}
