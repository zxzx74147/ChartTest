package com.zxzx74147.devlib.os;

import java.io.Serializable;

public class VersionInfo implements Serializable{
    /**
     *
     */
    private int versionCode;
    private String versionName;
    private String innerVersionName;
    public String buildVersion;

    public int getVersonCode() {
        return versionCode;
    }

    public void setVersonCode(int versonCode) {
        this.versionCode = versonCode;
    }


    public void setVersonName(String versonName) {
        this.versionName = versonName;
    }

    public String getInnerVersionName() {
        return innerVersionName;
    }

    public void setInnerVersionName(String innerVersionName) {
        this.innerVersionName = innerVersionName;
    }

    public String getVersonName(){
        return this.versionName;
    }


}
