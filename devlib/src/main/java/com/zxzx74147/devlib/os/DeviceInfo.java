package com.zxzx74147.devlib.os;

import java.io.Serializable;

/**
 * Created by jiaojiao on 2017/3/16.
 */

public class DeviceInfo implements Serializable {
    public int screen_width;
    public int screen_height;
    public float density;
    public int max_memory;
    public String brand;
    public String cpuInfo;
    public int cpuCoreNum;
    public String deviceOp;
    public String deviceType;
}
