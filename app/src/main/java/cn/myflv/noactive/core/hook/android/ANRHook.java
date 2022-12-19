package cn.myflv.noactive.core.hook.android;

import android.os.Build;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.entity.AppInfo;
import cn.myflv.noactive.core.entity.ProcessRecord;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;

public class ANRHook {

    public static void hook() {
        boolean isAndroidQ = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q;
        HookHelpers.config().className(ClassConstants.AnrHelper)
                .className(isAndroidQ, ClassConstants.ProcessRecord)
                .methodName(MethodConstants.appNotResponding)
                .params(ClassConstants.ProcessRecord, String.class, ClassConstants.ApplicationInfo,
                        String.class, ClassConstants.WindowProcessController,
                        boolean.class, String.class)
                .params(isAndroidQ, () -> new Object[]{
                        ClassConstants.ProcessRecord, String.class, ClassConstants.ApplicationInfo,
                        String.class, ClassConstants.WindowProcessController, boolean.class, String.class})
                .hookReplace(ANRHook::keep).hookReplace(isAndroidQ, HookHelpers.DO_NOTHING).hook();
    }

    public static Object keep(XC_MethodHook.MethodHookParam param) {
        // ANR进程为空就不处理
        if (param.args[0] == null) return null;
        // ANR进程
        ProcessRecord processRecord = new ProcessRecord(param.args[0]);
        AppInfo appInfo = processRecord.getAppInfo();
        if (!appInfo.isFrozen()) {
            return HookHelpers.invokeOriginalMethod(param);
        }
        XLog.d("keep", processRecord.getProcessNameWithUser());
        return null;
    }

}
