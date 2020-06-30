package com.jinsit.kmec.comm.jinLib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jinsit.kmec.Intro;
import com.jinsit.kmec.R;
import com.jinsit.kmec.GK.LO.GK_LO00_R00;
import com.jinsit.kmec.HM.MP.HM_MP00_R00;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 다이얼로그 액티비티 서비스에서 팝업띄울때 사용
 *
 * @author 원성민
 *
 */
public class DialogActivity extends Activity implements OnDismissListener{

	public static final String ACTION_LOGINSTATUSSERVICE = "ACTION.LoginStatusService";
	public static final String ACTION_EMPLOYEESAFESERVICE = "ACTION.EmployeeSafeService";
	public static final String ACTION_GPSSERVICE = "ACTION.GPSService";

	private Context context;
	private CommonSession commonSession;


	public static final int NOTIFICATION_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.activity_dialog);

		this.context = this;
		this.commonSession = new CommonSession(context);
		this.setConfig();


		if (this.getIntent().getAction().equals(ACTION_LOGINSTATUSSERVICE)) {
			this.forceLogout();

		} else if (this.getIntent().getAction().equals(ACTION_EMPLOYEESAFESERVICE)) {
			this.employeeSave();
		}else if (this.getIntent().getAction().equals(ACTION_GPSSERVICE)){
			this.turnOnGps();
		}

	}

	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}

	private void turnOnGps(){

		new AlertDialog.Builder(context)
				.setTitle("위치서비스 동의")
				.setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
				.setNeutralButton("이동",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								context.startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setOnDismissListener(this)
				.setCancelable(false)
				.show();

	}

	private void moveHome(){
		ActivityManager actManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo>info;
		info = actManager.getRunningTasks(1);
		for(Iterator<RunningTaskInfo> iterator = info.iterator(); iterator.hasNext();) {
			RunningTaskInfo runningTaskInfo = (RunningTaskInfo)iterator.next();

			if(runningTaskInfo.topActivity.getClassName().equals("com.jinsit.kmec.HM.MP.HM_MP00_R00")) {
				//Toast.makeText(context, "현재 홈화면에 있습니다.", 2000).show();
				//홈화면에 있을 때 롱클릭하면 리프레쉬 하려고 한다.
				Intent intent = new Intent(context, HM_MP00_R00.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}else{
				Intent intent = new Intent(context, HM_MP00_R00.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}


		}
	}




	private void employeeSave(){
		/**
		 * 20160530 공성윤주임 요청
		 * yesno Dialog로는 취소시 갱신이 안되기 때문에 그냥 취소없는 다이얼로그로 해달라고 함
		 */
		SimpleDialog ynDialog = new SimpleDialog(context, "알림","작업자 안전관리를 위해 작업자의 상태를 갱신하시겠습니까?",
				new com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener() {

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new EmpSafeStatusUpdateAsync().execute();
					}
				});
		ynDialog.setOnDismissListener(this);
		ynDialog.setCancelable(false);
		ynDialog.show();


	/*	SimpleYesNoDialog ynd = new SimpleYesNoDialog(context, "작업자 안전관리를 위해 작업자의 상태를 갱신하시겠습니까?",
				new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {
					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new EmpSafeStatusUpdateAsync().execute();
					}
				});
		ynd.setOnDismissListener(this);
		ynd.setCancelable(false);
		ynd.show();*/
	}

	private class EmpSafeStatusUpdateAsync extends AsyncTask<Void, String, Boolean> {

		private EasyJsonMap dataMap;
		private EasyJsonMap msgMap;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			//if(progressWorking)progress(false);
			//HM_MP00_R00.this.ProgressDialog.dismiss();
			try {
				boolean isError = msgMap.getValue("errCd").equals("0") ? false : true;
				if(!isError){
					if(result){
						if (dataMap.getValue("RTN").equals("1")) {
							//정상업데이트
							Toast.makeText(context, "갱신 되었습니다.", 2000).show();
						} else if (dataMap.getValue("RTN").equals("0")) {
							//업데이트 안됨
							Toast.makeText(context, "갱신을 실패하였습니다.", 2000).show();
						}
					}else{
					}
				}else if(isError){
					alert(msgMap.getValue("errMsg"), context);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//if(!progressWorking)progress(true);

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "hm/updateEmpStatus.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					dataMap = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return true;

		}
	}

	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		finish();
	}


	private void forceLogout(){
		this.attendAndLogout();
		this.sendNotification();

//		AlertView.showAlert("강제 로그아웃 되었습니다.", context,
//				new OnDismissListener() {
//					@Override
//					public void onDismiss(
//							DialogInterface dialog) {
//						attendAndLogout();
//						//finish();
//					}
//				});
	}

	private void attendAndLogout() {
		GK_LO00_R00 logout = new GK_LO00_R00(context);
		logout.forceLogout();
	}

	private void sendNotification() {
		Toast.makeText(context, "로그아웃 되었습니다.", 2000).show();

		String title = "로그아웃";
		String msg = "로그아웃 되었습니다.";
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(getApplicationContext(), Intro.class);
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