package cn.myflv.noactive.core.util;

import android.os.Build;

public class DeviceHelpers {

    public static boolean isSamsung() {
        return Build.MANUFACTURER.equals("samsung");
    }

}
