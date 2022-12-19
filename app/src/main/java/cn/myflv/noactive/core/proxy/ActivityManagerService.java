package cn.myflv.noactive.core.proxy;

import java.util.Objects;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.FieldConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.util.HookHelpers;
import cn.myflv.noactive.core.util.XLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import lombok.Getter;

public class ActivityManagerService {

    public final static int MAIN_USER = 0;

    private static Object instance;

    @Getter
    private static ProcessList processList;

    private static void setInstance(XC_MethodHook.MethodHookParam param) {
        if (Objects.nonNull(instance)) {
            return;
        }
        instance = param.thisObject;
        processList = new ProcessList(XposedHelpers.getObjectField(instance, FieldConstants.mProcessList));
        XLog.i("ActivityManagerService 获取成功");
    }

    public static void loadHook() {
        HookHelpers.hookAfter(ClassConstants.ActivityManagerService, MethodConstants.setSystemProcess, ActivityManagerService::setInstance);
        XLog.i("ActivityManagerService hook 成功");
    }


}
