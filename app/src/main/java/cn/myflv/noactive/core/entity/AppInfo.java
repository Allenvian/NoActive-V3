package cn.myflv.noactive.core.entity;


import java.util.Optional;

import cn.myflv.noactive.constant.CommonConstants;
import cn.myflv.noactive.core.proxy.ActivityManagerService;
import lombok.Data;

@Data
public class AppInfo {
    private final static S2Map<Integer, String, AppInfo> s2Map = new S2Map<>();
    private Integer userId;
    private String packageName;
    private boolean frozen = false;
    private boolean broadcast = false;

    private AppInfo(Integer userId, String packageName) {
        this.userId = userId;
        this.packageName = packageName;
    }

    public String getKey() {
        if (userId == ActivityManagerService.MAIN_USER) {
            return packageName;
        }
        return packageName + CommonConstants.COLON + userId;
    }

    public boolean isTargetApp() {
        return !UserConfig.whiteApps.contains(packageName);
    }

    public static AppInfo getInstance(Integer userId, String packageName) {
        int user = Optional.ofNullable(userId).orElse(ActivityManagerService.MAIN_USER);
        return s2Map.get(user, packageName, k -> new AppInfo(user, packageName));
    }

    public static AppInfo getInstance(String key) {
        String[] info = key.split(CommonConstants.COLON);
        if (info.length == 1) {
            return AppInfo.getInstance(ActivityManagerService.MAIN_USER, key);
        }
        return AppInfo.getInstance(Integer.valueOf(info[0]), info[1]);
    }

}
