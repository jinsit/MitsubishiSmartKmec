package com.jinsit.kmec.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DialogActivity;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class LoginStatusService extends Service {

	private Context context;
	private CommonSession commonSession;
	private EasyJsonMap ejm;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("LoginStatusService", "onCreate = LoginStatusService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.w("LoginStatusService", "onStartCommand = LoginStatusService");
		this.context = this;
		this.commonSession = new CommonSession(context);
		new LoginStatusAsync().execute();

		return START_STICKY; // 강제로그아웃 유무는 항상 체크해야 하기 때문에 스티키로 한다. "좀비써비스"
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public class LoginStatusAsync extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			commonSession = new CommonSession(context);
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "cm/checkIfLoginOrNot.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("empId", commonSession
					.getEmpId()));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejm = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}// end of doInBackground()

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			try {

				boolean isError = ejm.getValue("errorCd").equals("0") ? false
						: true;
				if (!isError) {
					if (result) {
						if (ejm.getValue("RTN").equals("1")) {
							// 로그아웃상태
							if(commonSession.isLoggined())forceLogoutForDialog();
						}else{
							//forceLogoutForDialog();
						}

					} else {

					}
				} else if (isError) {
					alert(ejm.getValue("errorMsg"), context);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void forceLogoutForDialog(){
		Intent intent = new Intent(this.context, DialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("ACTION.LoginStatusService");
		this.context.startActivity(intent);
	}
	// utils
	private void alert(String msg, Context context) {
		AlertView.showAlert(msg, context);
	}



}
