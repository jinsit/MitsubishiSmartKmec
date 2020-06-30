package com.jinsit.kmec.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.NT.IR_NT00_R00;
import com.jinsit.kmec.IR.NT.NoticeResponseData;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.PreferenceUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class NoticeService extends Service {

	private Context context;
	private CommonSession commonSession;
	private ArrayList<NoticeResponseData> noticeListData;
	private EasyJsonList ejl;

	public static final int NOTIFICATION_ID = 1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("onCreate", "onCreate = NoticeService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.w("onStartCommand", "onStartCommand = NoticeService");
		this.context = this;
		this.commonSession = new CommonSession(context);
		new NoticeAllTask().execute();

		return START_NOT_STICKY;
		///START_NOT_STICKY 앱을 종료했을 때 서비스 재시작 하지 않음 (
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public class NoticeAllTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				noticeListData = new ArrayList<NoticeResponseData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/selectAllNoticeData.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("noticeDt", ""));
				arguments.add(new BasicNameValuePair("deptCd", commonSession
						.getDeptCd()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {

					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					noticeListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						noticeListData.add(new NoticeResponseData(ejl.getValue(
								i, "NOTICE_DT"), ejl.getValue(i, "NOTICE_TM"),
								ejl.getValue(i, "TITLE"), ejl.getValue(i,
								"SENDER_NM"), ejl.getValue(i,
								"RECIPIENT_NM"), ejl.getValue(i,
								"CONTENT")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (noticeListData.size() > 0) {
				if (getNoticeCount() < noticeListData.size()) {
					//저장된 공지 카운트보다 조회해온 공지카운트가 크면 노티파이케이션 알림
					sendNotification();
				}
				setNoticeCount(noticeListData.size());
			}

		}
	}

	private void setNoticeCount(int count) {
		PreferenceUtil.instance(context).putNoticeCount(count);
	}

	private int getNoticeCount() {
		return PreferenceUtil.instance(context).getNoticeCount();
	}

	private void sendNotification() {
		String title = "알림";
		String msg = "새로운 공지사항이 있습니다.";
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(getApplicationContext(), IR_NT00_R00.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.app_icon_01).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setAutoCancel(true)
				.setVibrate(new long[] { 0, 500 });

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}
