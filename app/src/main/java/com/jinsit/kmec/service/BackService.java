package com.jinsit.kmec.service;

import com.jinsit.kmec.comm.CommonSession;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BackService extends Service {

	private static final String TAG = "BackService";
	private Context context = null;
	private CommonSession commonSession;

	private Handler noticeHandler = new Handler();
	private Handler employeeSafeHandler = new Handler();
	private Handler loginStatusHandler = new Handler();
	private Handler realTimeHandler = new Handler();


	private int noticeTimerCount = 1000 * 60 * 30; // 밀리세컨 * 60초 * 5분
	private int empSafeTimerCount = 1000 * 60 * 5;
	private int loginStatusTimerCount = 1000 * 30 * 1;
	private int realTimeTimerCount = 1000 * 20 * 1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.context = this;
		Log.v(TAG, "onCreate for BackService");
		// this.registerAlarm();
		this.commonSession = new CommonSession(context);

		this.noticeHandler.postDelayed(this.noticeRunnable, this.noticeTimerCount);

		this.employeeSafeHandler.postDelayed(this.employeeSafeRunnable, this.empSafeTimerCount);

		this.loginStatusHandler.postDelayed(this.loginStatusRunnable, this.loginStatusTimerCount);

		//this.realTimeHandler.postDelayed(this.realTimeRunnable, this.realTimeTimerCount);

	}


	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy for BackService");
		this.stopSelf();

		super.onDestroy();
		this.context = this;

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onStartCommand for BackService");
		this.commonSession = new CommonSession(context);
//
//		this.noticeHandler.postDelayed(this.noticeRunnable, this.noticeTimerCount);
//
//		this.employeeSafeHandler.postDelayed(this.employeeSafeRunnable, this.empSafeTimerCount);
//
//		this.loginStatusHandler.postDelayed(this.loginStatusRunnable, this.loginStatusTimerCount);
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private Runnable noticeRunnable = new Runnable() {
		public void run() {
			Intent intent = new Intent(context, NoticeService.class);
			context.startService(intent);
			// 핸들러로 딜레이주며 반복적으로 돌린다.
			noticeHandler.postDelayed(noticeRunnable, noticeTimerCount);
		}
	};

	private Runnable employeeSafeRunnable = new Runnable() {
		public void run() {
			Intent intent = new Intent(context, EmployeeSafeService.class);
			context.startService(intent);
			employeeSafeHandler.postDelayed(employeeSafeRunnable,
					empSafeTimerCount);
		}
	};

	private Runnable loginStatusRunnable = new Runnable() {
		public void run() {
			CommonSession session = new CommonSession(context);
			if(session.isLoggined()){

				Log.w(TAG, "loginStatusRunnable  commonSession.isLoggined()  in true = " + session.isLoggined());
				Intent intent = new Intent(context, LoginStatusService.class);
				context.startService(intent);

			}
			loginStatusHandler.postDelayed(loginStatusRunnable,
					loginStatusTimerCount);
			Log.v(TAG, "loginStatusRunnable  commonSession.isLoggined() = " + session.isLoggined());
		}
	};

	private Runnable realTimeRunnable = new Runnable() {
		public void run() {
			Intent intent = new Intent(context, RealTimeJobHistoryService.class);
			context.startService(intent);
			realTimeHandler.postDelayed(realTimeRunnable,realTimeTimerCount);
		}
	};

}
