package cn.myflv.noactive.enums;

import android.os.Build;

import java.util.Objects;

import lombok.Getter;

public enum AndroidVersionEnum {
    R(30, "R"),
    S(31, "S");

    @Getter
    private final Integer sdkVersion;
    @Getter
    private final String versionCode;

    AndroidVersionEnum(Integer sdkVersion, String versionCode) {
        this.sdkVersion = sdkVersion;
        this.versionCode = versionCode;
    }

    public static String getVersionCodeBySdkVersion(Integer value) {
        for (AndroidVersionEnum versionEnum : AndroidVersionEnum.values()) {
            if (Objects.equals(versionEnum.getSdkVersion(), value)) {
                return versionEnum.getVersionCode();
            }
        }
        return null;
    }

    public static String getCurrentVersionCode() {
        return getVersionCodeBySdkVersion(Build.VERSION.SDK_INT);
    }
}
