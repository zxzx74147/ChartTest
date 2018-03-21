package com.zxzx74147.devlib.os;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.utils.SharedPreferenceHelper;

/**
 * 管理包信息
 * 
 * @author zhengxin
 * 
 */
public class PackageInfoMananger {
	private static final String LAST_VERSION = "last_version";
	private static PackageInfoMananger mMananger;
	private VersionInfo versionInfo;

	private PackageInfoMananger() {

	}

	public static PackageInfoMananger sharedInstance() {
		if (mMananger == null) {
			mMananger = new PackageInfoMananger();
		}
		return mMananger;
	}

	// 获取版本信息
	public VersionInfo getVersionInfo() {
		if (versionInfo != null) {
			return versionInfo;
		}
		PackageManager packageManager = DevLib.getApp()
				.getPackageManager();
		PackageInfo packInfo = null;
		ApplicationInfo appInfo = null;
		versionInfo = new VersionInfo();
		try {
			packInfo = packageManager.getPackageInfo(DevLib.getApp().getPackageName(), 0);
			appInfo = packageManager.getApplicationInfo(packInfo.packageName,
					PackageManager.GET_META_DATA);
			versionInfo.setVersonName(packInfo.versionName);
			versionInfo.setVersonCode(packInfo.versionCode);
			versionInfo.setInnerVersionName(appInfo.metaData.getString("api_version"));
			versionInfo.buildVersion=appInfo.metaData.getString("test_version");
//			versionInfo.buildVersion = appInfo.metaData.getString("build_version");
			return versionInfo;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isFirstUpdate() {
		boolean result = false;
		try {
			String lastVersion = SharedPreferenceHelper.getString(LAST_VERSION);
			if (!(lastVersion.equals(getVersionInfo().getVersonName()))) {
				result = true;
				SharedPreferenceHelper.saveString(LAST_VERSION, getVersionInfo().getVersonName());
			}
		} catch (Exception e) {

		}
		return result;
	}

}
