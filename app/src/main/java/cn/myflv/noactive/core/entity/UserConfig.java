package cn.myflv.noactive.core.entity;

import java.util.HashSet;
import java.util.Set;

import cn.myflv.noactive.constant.CommonConstants;

public class UserConfig {

    /**
     * 白名单APP.
     */
    public final static Set<String> whiteApps = new HashSet<>();

    static {
        whiteApps.add(CommonConstants.ANDROID);
    }


}
