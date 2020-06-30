package com.jinsit.kmec.comm.jinLib;

import android.content.Context;

import com.jinsit.kmec.comm.GPSService;
/**
 * @author  yowonsm
 */
public class GpsStates {
	public static boolean isGpsStatus(Context context){
		boolean isGps = false;
		GPSService gs = new GPSService(context);
		if (gs.isGetLocation()) {
			// GPS상태체크
			isGps = true;
		}
		return isGps;

	}


}
