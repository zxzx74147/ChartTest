package com.zxzx74147.devlib.os;

import android.util.Log;

import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.devlib.utils.DisplayUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * DeviceID用于区分设备，要保证绝对唯一，生成之后内外均存储。生成规则待定 ; TODO
 * 对于内外存均需要存储数据，统一提供操作类，并向上层隐藏同步过程 ;
 * 
 * @author zhengxin
 * 
 */
public class DeviceInfoMananger {
	private final String TAG = "DeviceInfoMananger__";
	private static DeviceInfoMananger mMananger;
	private String mInfo = null;

	private final String cpuFliter = "tegra";


	private static final String KEY_DEVICE_INFO = "key_device_info";

	private DeviceInfoMananger() {
		// 初始化mDeviceID

	}

	public static DeviceInfoMananger sharedInstance() {
		if (mMananger == null) {
			mMananger = new DeviceInfoMananger();
		}
		return mMananger;
	}


	/**
	 * 禁止tegra芯片且不是小米的手机使用抱抱
	 * @return
	 */
	public boolean infliterCpu(){


		try {
			InputStream is = new FileInputStream("/proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				String nameFeatures = "Hardware";
				String line ;
				while (( line = br.readLine()) != null) {
					String[] pair = null;
					pair = line.split(":");
					if (pair.length != 2)
						continue;
					String key = pair[0].trim();
					String value = pair[1].trim();
					if (key.compareToIgnoreCase(nameFeatures) == 0 &&
							value.toLowerCase().contains(cpuFliter)
							&& !android.os.Build.BRAND.contains("MI")) {

						return true;
					}

					Log.e(TAG,key+";"+value);

				}
			} finally {
				br.close();
				ir.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 生成DeviceID
	public String getDeviceInfo() {

		if (mInfo == null) {
			try {
				DeviceInfo mData = new DeviceInfo();
				mData.screen_width = DisplayUtil.getDisplayMetrics().widthPixels;
				mData.screen_height =DisplayUtil.getDisplayMetrics().heightPixels;
				mData.density = DisplayUtil.getDisplayMetrics().density;
				mData.max_memory = ((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024;
				mData.brand = android.os.Build.BRAND;// 手机品牌
				Log.e(TAG,"brand"+mData.brand);

				String str1 = "/proc/cpuinfo";
				String str2 = "";
				String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
				String[] arrayOfString;
				FileReader fr = new FileReader(str1);
				BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
				str2 = localBufferedReader.readLine();
				arrayOfString = str2.split("\\s+");
				for (int i = 2; i < arrayOfString.length; i++) {
					cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
				}
				str2 = localBufferedReader.readLine();
				arrayOfString = str2.split("\\s+");
				cpuInfo[1] += arrayOfString[2];
				localBufferedReader.close();
				mData.cpuInfo = cpuInfo[0] + cpuInfo[1];
//                mData.cpuCoreNum = BdUtilHelper.getCPUCoresNum();
				mInfo = JsonHelper.toJson(mData);
                fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mInfo;
	}


}
