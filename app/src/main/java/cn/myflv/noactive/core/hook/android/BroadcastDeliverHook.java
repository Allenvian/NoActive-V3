package cn.myflv.noactive.core.hook.android;

import java.util.Objects;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.FieldConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.entity.ProcessRecord;
import cn.myflv.noactive.core.hook.Android;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class BroadcastDeliverHook {

    private final static String KEY = "appInfo";

    public static void hook() {
        HookHelpers.config().className(true, ClassConstants.BroadcastQueue).methodName(MethodConstants.deliverToRegisteredReceiverLocked)
                .params(ClassConstants.BroadcastRecord, ClassConstants.BroadcastFilter, boolean.class, int.class)
                .hookBefore(param -> Android.systemReady(() -> before(param)))
                .hookAfter(param -> Android.systemReady(() -> after(param))).hook();
    }

    private static void before(XC_MethodHook.MethodHookParam param) {
        // 获取进程
        Object app = HookHelpers.getObjByPath(param.args[1], FieldConstants.receiverList, FieldConstants.app);
        // 转包装对象
        ProcessRecord processRecord = new ProcessRecord(app);
        // 获取应用信息
        AppInfo appInfo = processRecord.getAppInfo();
        if (appInfo.isIgnoreApp()) {
            return;
        }
        if (!appInfo.isFrozen()) {
            broadcastStart(param, appInfo);
            return;
        }
        clearBroadcast(param, processRecord);
    }

    private static void after(XC_MethodHook.MethodHookParam param) {
        if (Objects.isNull(param.getObjectExtra(KEY))) {
            restoreBroadcast(param);
        } else {
            broadcastFinish(param);
        }
    }

    private static void setApp(XC_MethodHook.MethodHookParam param, Object value) {
        Object receiverList = HookHelpers.getObjByPath(param.args[1], FieldConstants.receiverList);
        XposedHelpers.setObjectField(receiverList, FieldConstants.app, value);
    }

    private static void clearBroadcast(XC_MethodHook.MethodHookParam param, ProcessRecord processRecord) {
        param.setObjectExtra(FieldConstants.app, processRecord.getInstance());
        setApp(param, null);
        XLog.d(processRecord.getProcessNameWithUser(), "清理广播");
    }

    private static void restoreBroadcast(XC_MethodHook.MethodHookParam param) {
        Object app = param.getObjectExtra(FieldConstants.app);
        if (Objects.isNull(app)) {
            return;
        }
        setApp(param, app);
    }

    private static void broadcastStart(XC_MethodHook.MethodHookParam param, AppInfo appInfo) {
        appInfo.setBroadcast(true);
        param.setObjectExtra(KEY, appInfo.getKey());
        XLog.d(appInfo.getKey(), "开始广播");
    }

    private static void broadcastFinish(XC_MethodHook.MethodHookParam param) {
        String appKey = (String) param.getObjectExtra(KEY);
        AppInfo appInfo = AppInfo.getInstance(appKey);
        appInfo.setBroadcast(false);
        XLog.d(appInfo.getKey(), "结束广播");
    }

}
