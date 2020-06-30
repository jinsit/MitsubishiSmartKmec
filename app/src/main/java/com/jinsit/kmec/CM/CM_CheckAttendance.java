package com.jinsit.kmec.CM;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.jinsit.kmec.GK.LO.GK_LO00_R00;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.service.LoginStatusService;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_CheckAttendance {
	private CommonSession commonSession;
	private ListView listView;
	private Context context;
	public SimpleYesNoDialog simpleYesNoDialog;

	public CM_CheckAttendance(Context context, ListView listview) {
		this.listView = listview;
		this.context = context;
		this.commonSession = new CommonSession(context);
	}

	public void checkAttendance() throws ParseException {
		/**
		 * isAttended가 올바르지 않아서 아직 안됨
		 * preference Attended 처리함
		 */
		if (this.listView.getAdapter().getCount() < 1
				&& this.commonSession.isAttended()) {

			long workOutTime = DateUtil.getSecond("17:30");
			long time = DateUtil.getSecond(DateUtil.nowDateFormat("HH:mm"));

			if (workOutTime < time) {
				if (simpleYesNoDialog == null)
					simpleYesNoDialog = new SimpleYesNoDialog(context,
							"작업이 없습니다. 퇴근하시겠습니까?", new btnClickListener() {
						@Override
						public void onButtonClick() {
							// TODO Auto-generated method stub
							new WorkOutAsync().execute();
						}
					});
				if (!simpleYesNoDialog.isShowing()) {
					simpleYesNoDialog.setCancelable(false);
					simpleYesNoDialog.show();
				}

			}

		}

	}

	public class WorkOutAsync extends AsyncTask<Void, Void, String> {
		EasyJsonMap ejm01;
		EasyJsonMap ejm02;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... params) {
			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl() + "sm/getOffWork.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("usrId", commonSession
					.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession
					.getWorkDt()));

			JSONObject returnJson = http.getPost(argUrl, arguments, true);
			try {
				ejm01 = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
				ejm02 = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
				boolean isError = ejm01.getValue("errCd").equals("1") ? true
						: false;
				if (!isError) {

					String rtn = ejm02.getValue("RTN");
					if (rtn.equals("1")) {

						Toast.makeText(context, "퇴근 되었습니다.", 2000).show();

						commonSession.setIsAttended(false);
						// 20150126 퇴근후에 로그아웃 시킨다.
						// CallService.startGPSService(context);
						attendAndLogout();
					} else if (rtn.equals("0")) {
						AlertView.showAlert("퇴근 미처리입니다.", context);

					} else if (rtn.equals("2")) {
						AlertView.showAlert("이미 퇴근상태입니다.", context);
					} else if (rtn.equals("3")) {
						// 퇴근하려는데 '진행중','계획' 상태인 작업이 있을 경우
						AlertView.showAlert("완료 안된 작업이 있습니다. 확인해 주세요.", context);
					}

				} else if (isError) {

					AlertView.showAlert(ejm01.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 강제로그아웃
	 */
	private void attendAndLogout() {
		GK_LO00_R00 logout = new GK_LO00_R00(context);
		logout.attendAndLogout();
	}

}
