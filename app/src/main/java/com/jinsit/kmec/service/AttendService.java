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
import android.os.Handler;
import android.os.IBinder;

import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class AttendService extends Service{

	private Context context;
	private CommonSession commonSession;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;

	private Handler realTimeHandler = new Handler();
	private int realTimeTimerCount = 1000 * 30 * 1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		this.context = this;
		this.commonSession = new CommonSession(context);
		this.realTimeHandler.postDelayed(this.realTimeRunnable, this.realTimeTimerCount);
		return START_STICKY; // 강제로그아웃 유무는 항상 체크해야 하기 때문에 스티키로 한다. "좀비써비스"
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private Runnable realTimeRunnable = new Runnable() {
		public void run() {

			boolean isFailed = commonSession.isCommuteFailed();


			if(isFailed == true)
			{
				if ("출근".equals(commonSession.getCommuteStatus())) {
					new Database().execute("getToWork_TT");
				} else {
					new Database().execute("getOffWork_TT");
				}
				realTimeHandler.postDelayed(realTimeRunnable,realTimeTimerCount);

			}else{
				stopSelf();
			}

		}
	};

	private class Database extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
		}
	}// end of Database class

	private void crud(String div) {

		if (div.equals("getToWork_TT")) {

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl() + "sm/" + div + ".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			if(commonSession.isCommuteFailed() == true && 	//출근시 음영지역에서 실패하면서
					"출근".equals(commonSession.getCommuteStatus()) 	//pref에 들어있는 상태가 출근이면서
					&& commonSession.getWorkDt() != null 	//pref에 WorkDt가 들어있으면서
					&& commonSession.getWorkDt().equals(commonSession.getCommuteWorkDt())) //pref에 WorkDt랑 출근일자가 같으면
			{
				arguments.add(new BasicNameValuePair("usrId", commonSession.getCommuteEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getCommuteWorkDt()));
				arguments.add(new BasicNameValuePair("latitude", commonSession.getLatitude()));
				arguments.add(new BasicNameValuePair("longitude", commonSession.getLongitude()));
				arguments.add(new BasicNameValuePair("attendTm", commonSession.getCommuteTime()));
				arguments.add(new BasicNameValuePair("attendRmk", commonSession.getOfficeName()));
			}

			JSONObject returnJson = http.getPost(argUrl, arguments, true);
			try {
				ejm01 = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
				ejm02 = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (div.equals("getOffWork_TT")) {

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl() + "sm/" + div + ".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			if(commonSession.isCommuteFailed() == true && 	//출근시 음영지역에서 실패하면서
					"퇴근".equals(commonSession.getCommuteStatus()) 	//pref에 들어있는 상태가 출근이면서
					&& commonSession.getWorkDt() != null 	//pref에 WorkDt가 들어있으면서
					&& commonSession.getWorkDt().equals(commonSession.getCommuteWorkDt())) //pref에 WorkDt랑 출근일자가 같으면
			{
				arguments.add(new BasicNameValuePair("usrId", commonSession.getCommuteEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getCommuteWorkDt()));
				arguments.add(new BasicNameValuePair("latitude", commonSession.getLatitude()));
				arguments.add(new BasicNameValuePair("longitude", commonSession.getLongitude()));
				arguments.add(new BasicNameValuePair("attendTm", commonSession.getCommuteTime()));
				arguments.add(new BasicNameValuePair("attendRmk", commonSession.getOfficeName()));
			}


			JSONObject returnJson = http.getPost(argUrl, arguments, true);
			try {
				ejm01 = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
				ejm02 = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}// end of crud()

	private void setData(String div) {

		if (div.equals("getToWork_TT")) {

			try {
				boolean isError = ejm01.getValue("errCd").equals("1") ? true
						: false;
				if (!isError) {

					String rtn = ejm02.getValue("RTN");
					String commuteTime = "";
					if (rtn.equals("1")) {

						if(commonSession.getCommuteTime().equals("") == true)
						{
							commuteTime = DateUtil.nowDateFormat("HH:mm");
						}else{
							commuteTime = commonSession.getCommuteTime();
						}

						//commuteResult("출근", rtn, commuteTime);
						commonSession.setCommute("", "","", "", "","","","", false);
					} else if (rtn.equals("0")) {
						//alert("출근 미처리입니다.", context);
					} else if (rtn.equals("2")) {
						//alert("오늘의 출퇴근을 모두 하셨습니다.", context);
					} else if (rtn.equals("5")) {
						if(commonSession.getCommuteTime().equals("") == true)
						{
							commuteTime = DateUtil.nowDateFormat("HH:mm");
						}else{
							commuteTime = commonSession.getCommuteTime();
						}
						//commuteResult("출근", rtn, commuteTime);
						commonSession.setCommute("", "","", "", "","","","", false);

					}

				} else if (isError) {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (div.equals("getOffWork_TT")) {

			try {
				boolean isError = ejm01.getValue("errCd").equals("1") ? true
						: false;
				if (!isError) {

					String rtn = ejm02.getValue("RTN");
					String commuteTime = "";
					if (rtn.equals("1")) {
						// Toast.makeText(context, "퇴근 되었습니다.", 2000).show();
						if(commonSession.getCommuteTime().equals("") == true)
						{
							commuteTime = DateUtil.nowDateFormat("HH:mm");
						}else{
							commuteTime = commonSession.getCommuteTime();
						}
						//commuteResult("퇴근", rtn, commuteTime);
						commonSession.setCommute("", "","", "", "","","", "", false);
					} else if (rtn.equals("0")) {
						//alert("퇴근 미처리입니다.", context);
					} else if (rtn.equals("2")) {
						//alert("이미 퇴근상태입니다.", context);
					} else if (rtn.equals("3")) {
						// 퇴근하려는데 '진행중','계획' 상태인 작업이 있을 경우
						//alert("완료 안된 작업이 있습니다. 확인해 주세요.", context);
					}

				}
				else if (isError)
				{

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}// end of setData()

}
