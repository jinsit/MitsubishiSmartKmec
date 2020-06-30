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
import com.jinsit.kmec.comm.jinLib.ActivityManagerUtil;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DialogActivity;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class EmployeeSafeService extends Service{

	private Context context;
	private CommonSession commonSession;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("EmployeeSafeService", "onCreate = EmployeeSafeService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.w("EmployeeSafeService", "onStartCommand = EmployeeSafeService");
		this.context = this;
		this.commonSession = new CommonSession(context);
		new EmpSafeStatusAsync().execute();

		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}


	private class EmpSafeStatusAsync extends AsyncTask<Void, String, Boolean> {

		private EasyJsonMap dataMap;
		private EasyJsonMap msgMap;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if(result){

				try {

					boolean isError = msgMap.getValue("errCd").equals("0") ? false : true;
					if(!isError){
						if (dataMap.getValue("RETURN_TP").equals("N")) {

						} else if (dataMap.getValue("RETURN_TP").equals("Y")) {
							employeeSafeForDialog();
						}


					}else if(isError){
						alert(msgMap.getValue("errMsg"), context);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "hm/searchEmpSafeStatus.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					dataMap = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));

				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				return false;
			}
			return true;

		}
	}

	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}

	private void employeeSafeForDialog(){
//		if(ActivityManagerUtil.getPackageName(mContext).equals("com.jinsit.kmec")){
//			this.turnOnGps();	
//			Log.i("onStartCommand", "GPSService  turn on ");
//		}else{
//			Log.i("onStartCommand", "GPSService not turn on ");
//		}
//		

		Intent intent = new Intent(this.context, DialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("ACTION.EmployeeSafeService");
		this.context.startActivity(intent);
	}



}
