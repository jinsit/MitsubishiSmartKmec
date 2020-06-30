package com.jinsit.kmec.comm;

import com.jinsit.kmec.service.NoticeService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	public static final String ACTION_RESTART_PERSISTENTSERVICE = "ACTION.Restart.PersistentService";

	// 공지서비스
	public static final String ACTION_RESTART_NOTICESERVICE = "ACTION.Restart.NoticeService";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(ACTION_RESTART_PERSISTENTSERVICE)) {

			Log.e("AlarmReceiver", "action = "
					+ ACTION_RESTART_PERSISTENTSERVICE);

			Intent i = new Intent(context, GPSService.class);
			context.startService(i);
		} else if (intent.getAction().equals(ACTION_RESTART_NOTICESERVICE)) {

			Log.e("AlarmReceiver", "action = "
					+ ACTION_RESTART_NOTICESERVICE);
			Intent i = new Intent(context, NoticeService.class);
			context.startService(i);
		}

	}

}
