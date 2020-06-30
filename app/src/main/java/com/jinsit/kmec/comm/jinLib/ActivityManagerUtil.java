package com.jinsit.kmec.comm.jinLib;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class ActivityManagerUtil {

	

	public static String getClassName(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskinfo = activityManager.getRunningTasks(1);
		return runningTaskinfo.get(0).topActivity.getClassName();
	}
	
	public static String getPackageName(Context context){
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskinfo = activityManager.getRunningTasks(1);
		return runningTaskinfo.get(0).topActivity.getPackageName();
	}
}
