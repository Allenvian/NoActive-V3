package cn.myflv.noactive.core.util;

import android.os.Build;

import cn.myflv.noactive.constant.DeviceConstants;

public class DeviceHelpers {

    public static boolean isSamsung() {
        return DeviceConstants.samsung.equals(Build.MANUFACTURER);
    }

}
