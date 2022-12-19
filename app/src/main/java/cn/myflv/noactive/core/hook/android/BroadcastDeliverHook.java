package cn.myflv.noactive.core.hook.android;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.FieldConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.entity.ProcessRecord;
import cn.myflv.noactive.core.hook.Android;
import cn.myflv.noactive.core.util.HookHelpers;
import de.robv.android.xposed.XC_MethodHook;

public class BroadcastDeliverHook {

    public static void hook() {
        HookHelpers.config().className(true,ClassConstants.BroadcastQueue).methodName(MethodConstants.deliverToRegisteredReceiverLocked)
                .params(ClassConstants.BroadcastRecord, ClassConstants.BroadcastFilter, boolean.class, int.class)
                .hookBefore(param -> Android.systemReady(() -> before(param)))
                .hookAfter(param -> Android.systemReady(() -> after(param))).hook();
    }


    private static void before(XC_MethodHook.MethodHookParam param) {
        Object app = HookHelpers.getObjByPath(param.args[1], FieldConstants.receiverList, FieldConstants.app);
        ProcessRecord processRecord = new ProcessRecord(app);
        AppInfo appInfo = processRecord.getAppInfo();
        if (!appInfo.isFrozen()){
            return;
        }

    }

    private static void after(XC_MethodHook.MethodHookParam param) {

    }

}
