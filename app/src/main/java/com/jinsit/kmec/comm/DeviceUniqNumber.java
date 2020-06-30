package com.jinsit.kmec.comm;

import java.util.UUID;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DeviceUniqNumber {
	TelephonyManager manager;
	Context context;

	public DeviceUniqNumber(Context c) {
		context  = c;
		manager = (TelephonyManager)context
				.getSystemService(Context.TELEPHONY_SERVICE);

	}

	public static String getUniqueID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}
	public String getUdid(){
		//String ret = "706BBAE0-CFC3-49DE-B02A-1DE50B6B5B69";
		//공주임 UDID
		return manager.getDeviceId();

	}
	public String getModel(){
		//String ret = "706BBAE0-CFC3-49DE-B02A-1DE50B6B5B69";
		//공주임 UDID
		return Build.MODEL;

	}
	public String getSerialNumber(){

		return manager.getSimSerialNumber();

	}
	public String getAndroidId(){
		return android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

	}
	public String getPhoneNumber(){
		return manager.getLine1Number();

	}
}