package cn.myflv.noactive.core.proxy;

import android.os.Build;

import java.util.Objects;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;

public class AlarmManagerService {

    public final static int MAIN_USER = 0;

    private static Object instance;

    private static void setInstance(XC_MethodHook.MethodHookParam param) {
        if (Objects.nonNull(instance)) {
            return;
        }
        instance = param.thisObject;
        XLog.i("AlarmManagerService 获取成功");
    }

    public static void loadHook() {
        HookHelpers.config().className(ClassConstants.AlarmManagerService)
                .className(Build.VERSION.SDK_INT < Build.VERSION_CODES.S, ClassConstants.AlarmManagerService_R)
                .methodName(MethodConstants.onBootPhase).params(int.class)
                .hookAfter(AlarmManagerService::setInstance);
        XLog.i("AlarmManagerService hook 成功");
    }


}
