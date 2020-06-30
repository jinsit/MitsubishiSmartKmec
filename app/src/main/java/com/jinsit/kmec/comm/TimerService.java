package com.jinsit.kmec.comm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class TimerService extends Service {
	private static final String TAG ="TimerService";
	private Context mContext = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		registerAlarm();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext = this;
		unRegisterAlarm();
	};
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	void registerAlarm() {
		Log.v(TAG, "registerStartAlarm");
		Intent intent = new Intent(mContext,
				com.jinsit.kmec.comm.AlarmReceiver.class);
		intent.setAction("ACTION.Restart.PersistentService");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
				intent, 0);

		long firstTime = SystemClock.elapsedRealtime();
		long interval = 300 * 1000; // 24시간
		//long interval = 30 * 1000; // 24시간

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				interval, pendingIntent);
		Log.e(TAG, TAG);
	}

	void unRegisterAlarm() {
		// TODO
		Log.d(TAG, "unregisterRestartAlarm");
		Intent intent = new Intent(mContext,
				com.jinsit.kmec.comm.AlarmReceiver.class);
		intent.setAction("ACTION.Restart.PersistentService");
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}

	void unregisterRestartAlarm() {
		Log.d(TAG, "unregisterRestartAlarm");
		Intent intent = new Intent(mContext,
				com.jinsit.kmec.comm.AlarmReceiver.class);
		intent.setAction("ACTION.Restart.PersistentService");
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
