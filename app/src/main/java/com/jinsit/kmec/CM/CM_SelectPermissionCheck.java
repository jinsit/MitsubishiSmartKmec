package com.jinsit.kmec.CM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SelectPermissionCheck extends AsyncTask<CM_SelectPermissionCheck_ReqestData, Process, String>{
	private ProgressDialog progress;
	private Context context;
	public CM_SelectPermissionCheck(Context context){
		this.context = context;
	}
	//	@Override
//	protected void onPostExecute(String result) {
//		  this.ProgressDialog.dismiss();
//			
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		
//		 this.ProgressDialog = android.app.ProgressDialog.show(
//				 context,"접근권한 체크","접근권한 체크 중 입니다.");
//	}
//
//	public void startProgress(){
//		progress(true);
//		//new NoticeImageTask().execute();
//	}
//	private void progress(Boolean isActivated){
//		if(isActivated){
//			this.progress =
//					  android.app.ProgressDialog.show(context, "접근권한 체크","접근권한 체크 중 입니다.");
//		}else{
//			this.progress.dismiss();
//		}
//	}
//
	@Override
	protected String doInBackground(CM_SelectPermissionCheck_ReqestData... params) {
		// TODO Auto-generated method stub
		String retStr = "";
		try {
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()+"cm/selectPermissionCheck.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("usrId",params[0].getUsrId()));
			arguments.add(new BasicNameValuePair("menuCd",params[0].getMenuCd()));
			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

			try {
				retStr = returnJson.getString("dataString");
				Log.v("retStr", "retStr = " + retStr);


			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			// 로그인이 실패하였습니다 띄어주기
		}


		return retStr;

	}

}
