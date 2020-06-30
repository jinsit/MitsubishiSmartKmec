package com.jinsit.kmec.comm.jinLib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationInfo {
	//private final static String UPDATE_DAY = "2016-09-30";
	private final static String UPDATE_DAY = "2020-03-10";
	private final static String TITLE = "Application 정보";

	private static final boolean IS_DEBUG_MODE = false;			//true 디버그모드, false 릴리즈모드

	public static String getUpdateDay()
	{
		return UPDATE_DAY;
	}

	public static String getVersion(Context context) {
		String verSion = "";
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		verSion = pi.versionName;
		return TITLE + "\n  - Version : " + verSion + "\n  - Date      : "
				+ getUpdateDay();
	}

	public static String getVersionLogin(Context context) {
		String verSion = "";
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		verSion = pi.versionName;
		return "ver" + verSion + "(" + getUpdateDay() + ")";
	}

	public static int getVersionCode(Context context) {
		int versionCode = 0;
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		versionCode = pi.versionCode;
		return versionCode;
	}

	/**
	 * true 디버그모드, false 릴리즈모드
	 */
	public static boolean isDebugMode(){
		return IS_DEBUG_MODE;
	}


}
