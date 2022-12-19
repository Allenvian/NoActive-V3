package cn.myflv.noactive.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.myflv.noactive.core.HookHandler;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Hook帮助类.
 */
public class HookHelpers {

    /**
     * Hook方法.
     *
     * @param className    类名
     * @param methodName   方法名
     * @param iHookParam   参数
     * @param iHookReplace 替换方法
     * @param iHookBefore  方法执行之前
     * @param iHookAfter   方法执行之后
     */
    private static void hookMethod(String className, String methodName, IHookParam iHookParam, IHookReplace iHookReplace, IHookBefore iHookBefore, IHookAfter iHookAfter) {
        // 参数判空
        if (Objects.isNull(iHookParam)) {
            iHookParam = () -> new Object[0];
        }
        // 数组转List
        List<Object> paramList = new ArrayList<>(Arrays.asList(iHookParam.params()));
        // 如果替换方法不为空
        if (Objects.nonNull(iHookReplace)) {
            // 包装Xposed替换方法
            paramList.add(new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return iHookReplace.replaceHookedMethod(param);
                }
            });
        } else {
            // 包装Xposed方法
            paramList.add(new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    if (Objects.isNull(iHookBefore)) {
                        return;
                    }
                    iHookBefore.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if (Objects.isNull(iHookAfter)) {
                        return;
                    }
                    iHookAfter.afterHookedMethod(param);
                }
            });
        }
        // 最终Hook
        XposedHelpers.findAndHookMethod(className, HookHandler.getClassLoader(), methodName, paramList.toArray());
    }

    /**
     * 简单Hook方法执行之后.
     *
     * @param className  类名
     * @param methodName 方法名
     * @param iHookAfter 方法执行之后
     */
    public static void hookAfter(String className, String methodName, IHookAfter iHookAfter) {
        hookMethod(className, methodName, null, null, null, iHookAfter);
    }

    /**
     * 简单Hook方法执行之前.
     *
     * @param className   类名
     * @param methodName  方法名
     * @param iHookBefore 方法执行之前
     */
    public static void hookBefore(String className, String methodName, IHookBefore iHookBefore) {
        hookMethod(className, methodName, null, null, iHookBefore, null);
    }

    /**
     * 简单Hook替换方法.
     *
     * @param className    类名
     * @param methodName   方法名
     * @param iHookReplace 替换方法
     */
    public static void hookReplace(String className, String methodName, IHookReplace iHookReplace) {
        hookMethod(className, methodName, null, iHookReplace, null, null);
    }

    /**
     * Builder方法.
     *
     * @return 构造者
     */
    public static Config config() {
        return new Config();
    }

    /**
     * Hook参数接口.
     */
    @FunctionalInterface
    public interface IHookParam {
        Object[] params();
    }

    /**
     * Hook方法执行之后接口.
     */
    @FunctionalInterface
    public interface IHookAfter {
        void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable;
    }

    /**
     * Hook方法执行之前接口.
     */
    @FunctionalInterface
    public interface IHookBefore {
        void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable;
    }

    /**
     * Hook替换方法接口.
     */
    @FunctionalInterface
    public interface IHookReplace {
        Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable;
    }

    @Getter
    @NoArgsConstructor
    public static class Config {
        private String className;
        private String methodName;
        private Object[] params;
        private IHookReplace hookReplace;
        private IHookBefore hookBefore;
        private IHookAfter hookAfter;

        public Config className(boolean condition, String className) {
            if (!condition) {
                return this;
            }
            this.className = className;
            return this;
        }

        public Config methodName(boolean condition, String methodName) {
            if (!condition) {
                return this;
            }
            this.methodName = methodName;
            return this;
        }

        public Config params(boolean condition, IHookParam hookParam) {
            if (!condition) {
                return this;
            }
            this.params = hookParam.params();
            return this;
        }

        public Config hookReplace(boolean condition, IHookReplace hookReplace) {
            if (!condition) {
                return this;
            }
            this.hookReplace = hookReplace;
            return this;
        }

        public Config hookBefore(boolean condition, IHookBefore hookBefore) {
            if (!condition) {
                return this;
            }
            this.hookBefore = hookBefore;
            return this;
        }

        public Config hookAfter(boolean condition, IHookAfter hookAfter) {
            if (!condition) {
                return this;
            }
            this.hookAfter = hookAfter;
            return this;
        }

        public Config className(String className) {
            return className(true, className);
        }

        public Config methodName(String methodName) {
            return methodName(true, methodName);
        }

        public Config params(Object... params) {
            return params(true, () -> params);
        }

        public Config hookReplace(IHookReplace hookReplace) {
            return hookReplace(true, hookReplace);
        }

        public Config hookBefore(IHookBefore hookBefore) {
            return hookBefore(true, hookBefore);
        }

        public Config hookAfter(IHookAfter hookAfter) {
            return hookAfter(true, hookAfter);
        }

        public void hook() {
            HookHelpers.hookMethod(className, methodName, () -> params, hookReplace, hookBefore, hookAfter);
        }
    }

    public static Class<?> findClass(String className) {
        return XposedHelpers.findClass(className, HookHandler.getClassLoader());
    }
}