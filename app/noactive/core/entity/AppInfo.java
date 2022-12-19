package cn.myflv.noactive.core.entity;


import java.util.HashMap;
import java.util.Map;

import cn.myflv.noactive.core.proxy.ActivityManagerService;
import lombok.Data;

@Data
public class AppInfo {
    private Integer userId;
    private String packageName;

    private AppInfo(Integer userId, String packageName) {
        this.userId = userId;
        this.packageName = packageName;
    }

    public String getKey() {
        if (userId == ActivityManagerService.MAIN_USER) {
            return packageName;
        }
        return packageName + ":" + userId;
    }

    private final static Map<Integer, Map<String, AppInfo>> appMap = new HashMap<>();

    public static AppInfo getInstance(Integer userId, String packageName) {
        if (userId == null) {
            userId = ActivityManagerService.MAIN_USER;
        }
        Map<String, AppInfo> userAppMap = appMap.get(userId);
        if (userAppMap == null) {
            synchronized (appMap) {
                userAppMap = appMap.computeIfAbsent(userId, k -> new HashMap<>());
            }
        }
        AppInfo appInfo = userAppMap.get(packageName);
        if (appInfo == null) {
            synchronized (appMap) {
                userAppMap = appMap.computeIfAbsent(userId, k -> new HashMap<>());
                appInfo = userAppMap.get(packageName);
                if (appInfo == null) {
                    appInfo = new AppInfo(userId, packageName);
                    userAppMap.put(packageName, appInfo);
                }
            }
        }
        return appInfo;
    }

    private final static Map<String, AppInfo> cacheMap = new HashMap<>();

    public static AppInfo getInstance(String key) {
        String[] info = key.split(":");
        if (info.length == 1) {
            return AppInfo.getInstance(ActivityManagerService.MAIN_USER, key);
        }
        return AppInfo.getInstance(Integer.valueOf(info[0]), info[1]);
    }
}
