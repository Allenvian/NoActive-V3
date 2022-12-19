package cn.myflv.noactive.core.hook.android;

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

    public static void hook() {
        HookHelpers.config().className(true, ClassConstants.BroadcastQueue).methodName(MethodConstants.deliverToRegisteredReceiverLocked)
                .params(ClassConstants.BroadcastRecord, ClassConstants.BroadcastFilter, boolean.class, int.class)
                .hookBefore(param -> Android.systemReady(() -> before(param)))
                .hookAfter(param -> Android.systemReady(() -> after(param))).hook();
    }


    private static void before(XC_MethodHook.MethodHookParam param) {
        Object receiverList = HookHelpers.getObjByPath(param.args[1], FieldConstants.receiverList);
        Object app = HookHelpers.getObjByPath(receiverList, FieldConstants.app);
        ProcessRecord processRecord = new ProcessRecord(app);
        AppInfo appInfo = processRecord.getAppInfo();
        if (!appInfo.isTargetApp()) {
            return;
        }
        if (!appInfo.isFrozen()) {
            appInfo.setBroadcast(true);
            return;
        }
        param.setObjectExtra(FieldConstants.app, app);
        XposedHelpers.setObjectField(receiverList, FieldConstants.app, null);
        XLog.d(processRecord.getProcessNameWithUser() + " clear broadcast");
    }

    private static void after(XC_MethodHook.MethodHookParam param) {

    }

}
