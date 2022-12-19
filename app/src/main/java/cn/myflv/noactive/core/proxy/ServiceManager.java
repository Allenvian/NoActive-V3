package cn.myflv.noactive.core.proxy;

import android.os.IBinder;

import cn.myflv.noactive.constant.ClassConstants;
import cn.myflv.noactive.constant.MethodConstants;
import cn.myflv.noactive.core.util.HookHelpers;
import de.robv.android.xposed.XposedHelpers;

public class ServiceManager {

    private final static Class<?> clazz;

    static {
        clazz = HookHelpers.findClass(ClassConstants.ServiceManager);
    }

    public static void addService(String name, IBinder service) {
        XposedHelpers.callStaticMethod(clazz, MethodConstants.addService, name, service);
    }

}
