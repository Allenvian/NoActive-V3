package cn.myflv.noactive.core;

import cn.myflv.noactive.core.hook.Android;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Hook处理器.
 */
public class HookHandler implements IXposedHookLoadPackage {

    public static XC_LoadPackage.LoadPackageParam loadPackageParam;

    public static ClassLoader getClassLoader() {
        return loadPackageParam.classLoader;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        HookHandler.loadPackageParam = loadPackageParam;
        Android.hook();
    }
}
