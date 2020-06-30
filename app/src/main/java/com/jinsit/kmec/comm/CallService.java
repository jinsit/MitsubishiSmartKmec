package com.jinsit.kmec.comm;

import com.jinsit.kmec.service.BackService;

import android.content.Context;
import android.content.Intent;

public class CallService {

	public static void startTimerService(Context context) {
		Intent intent = new Intent(context, TimerService.class);
		context.startService(intent);
	}

	public static void stopTimerService(Context context) {
		Intent intent = new Intent(context, TimerService.class);
		context.stopService(intent);
	}

	public static void startGPSService(Context context) {
		Intent intent = new Intent(context, GPSService.class);
		context.startService(intent);
	}

	public static void stopGPSService(Context context) {
		Intent intent = new Intent(context, GPSService.class);
		context.stopService(intent);
	}
	
	public static void allStopService(Context context){
		Intent intent = new Intent(context, GPSService.class);
		context.stopService(intent);
		Intent intentwo = new Intent(context, TimerService.class);
		context.stopService(intentwo);
	}
	
	public static void startBackService(Context context){
		Intent intent = new Intent(context, BackService.class);
		context.startService(intent);
	}
}
