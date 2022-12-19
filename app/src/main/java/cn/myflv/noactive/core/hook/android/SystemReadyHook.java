package cn.myflv.noactive.core.hook.android;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.hook.Android;
import cn.myflv.noactive.core.util.HookHelpers;

public class SystemReadyHook {
    public static void hook() {
        HookHelpers.config().className(ClassConstants.SystemServer).methodName(MethodConstants.startOtherServices)
                .params(ClassConstants.TimingsTraceAndSlog).hookAfter(Android::setBooting).hook();
    }
}
