package cn.myflv.noactive.enums;

import android.os.Build;

import java.util.Objects;

import lombok.Getter;

public enum AndroidVersionEnum {
    R(30, "11"),
    S(31, "12");

    @Getter
    private final Integer sdkVersion;
    @Getter
    private final String version;

    AndroidVersionEnum(Integer sdkVersion, String version) {
        this.sdkVersion = sdkVersion;
        this.version = version;
    }

    public static String getVersionBySdkVersion(Integer value) {
        for (AndroidVersionEnum versionEnum : AndroidVersionEnum.values()) {
            if (Objects.equals(versionEnum.getSdkVersion(), value)) {
                return versionEnum.getVersion();
            }
        }
        return null;
    }

    public static String getCurrentVersion() {
        return getVersionBySdkVersion(Build.VERSION.SDK_INT);
    }
}
