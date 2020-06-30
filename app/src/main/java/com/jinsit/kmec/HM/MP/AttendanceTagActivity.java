package com.jinsit.kmec.HM.MP;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.LocationService;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.service.AttendService;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class AttendanceTagActivity extends Activity implements OnClickListener {

	private String ndefMsg;
	private String tagUid = "";

	private ProgressDialog progress;
	Context context;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;

	private CommonSession session;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;

	private String attendance;

	private Location location = null;

	private String officeName;
	private String localCoordX;
	private String localCoordY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendancetag);
		activityInit();
		android.app.ActionBar aBar = getActionBar();
		this.attendance = this.getIntent().getStringExtra("attendance");
		aBar.setTitle(attendance);
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		if (this.attendance.equals("퇴근")) {
			TextView commuteMsgTextView = (TextView) findViewById(R.id.commuteMsgTextView);
			commuteMsgTextView
					.setText("사무실 또는 현장 NFC태그에 접촉하거나 현재위치정보를 전송해주세요.");
			Button offWorkButton = (Button) findViewById(R.id.offWorkButton);
			offWorkButton.setVisibility(View.VISIBLE);
			offWorkButton.setOnClickListener(this);

		}

	}

	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		ndefMsg = "";
		tagUid = "";
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		session = new CommonSession(context);
		setConfig();
		//getOnLocation();
	}

	private void setConfig() {
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		enableTagWriteMode();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
		getOnLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableTagWriteMode();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] duration = { 50, 100, 200, 300 };
		vib.vibrate(duration, -1);

		// Tag writing mode
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			tagUid = NfcParser.getTagId(detectedTag);
			ndefMsg = NfcParser.getTagText(detectedTag);
			Log.e("tagUid", tagUid);
			Log.e("ndefMsg", ndefMsg);

			//tagUid = "047AA1AAA93C80";

			if (!tagUid.isEmpty()) {

				if(ApplicationInfo.isDebugMode() == true)		//디버그 모드면 걍 바로 하고
				{
					new OfficeNfcCheckAsync().execute();
					/*if (attendance.equals("출근"))
					{
						new Database().execute("getToWork_T");
					}
					else
					{
						new Database().execute("getOffWork_T");
					}*/
				}
				else //디버그 모드 아니면 사무실 NFC 태그 체크
				{
					// progress(true);
					if (this.location == null) // 태그했는데 location이 널이면 GPS활성화 한 후에 다시하도록
					{
						gpsActivation();
					}
					else
					{
						new OfficeNfcCheckAsync().execute();
					}
				}


			} else {

			}

		}

	}

	// utils
	private void alert(String msg, Context context) {
		AlertView.showAlert(msg, context);
	}

	private void enableTagWriteMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
				mWriteTagFilters, null);
	}

	private void disableTagWriteMode() {
		mNfcAdapter.disableForegroundDispatch(this);
	}

	public Location getLocation() {
		LocationService locationService = new LocationService(this.context);
		Location location = locationService.getLocation();
		return location;
	}

	private void getOnLocation() {
		this.location = getLocation();
		if (this.location == null) {
			gpsActivation();
			return;
		}

	}

	@SuppressLint("NewApi")
	private void gpsActivation() {
		new AlertDialog.Builder(context).setTitle("위치서비스 동의")
				.setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
				.setNeutralButton("이동", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						context.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				}).setOnDismissListener(new OnDismissListener() {
			@SuppressLint("NewApi")
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

			}
		}).show();
	}

	private void commuteResult(String commute, String rtnCode, String commuteTime) {
		Bundle bundle = new Bundle();
		bundle.putString("commute", commute);
		bundle.putString("rtnCode", rtnCode);
		bundle.putString("commuteTime", commuteTime);

		// Add the set of extended data to the intent and start it
		Intent intent = new Intent();
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}

	// a class which accesses to database
	private class Database extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress(true);
		}

		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress(false);
			setData(result);
		}
	}// end of Database class

	private void crud(String div) {

		if (div.equals("getToWork_TT")) {

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl() + "sm/" + div + ".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			if(session.isCommuteFailed() == true && 	//출근시 음영지역에서 실패하면서
					"출근".equals(session.getCommuteStatus()) 	//pref에 들어있는 상태가 출근이면서
					&& session.getWorkDt() != null 	//pref에 WorkDt가 들어있으면서
					&& session.getWorkDt().equals(session.getCommuteWorkDt())) //pref에 WorkDt랑 출근일자가 같으면
			{
				arguments.add(new BasicNameValuePair("usrId", session.getCommuteEmpId()));
				arguments.add(new BasicNameValuePair("workDt", session.getCommuteWorkDt()));
				arguments.add(new BasicNameValuePair("latitude", session.getLatitude()));
				arguments.add(new BasicNameValuePair("longitude", session.getLongitude()));
				arguments.add(new BasicNameValuePair("attendTm", session.getCommuteTime()));
				arguments.add(new BasicNameValuePair("attendRmk", session.getOfficeName()));
			}
			else
			{
				arguments.add(new BasicNameValuePair("usrId", session.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", session.getWorkDt()));
				//arguments.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
				//arguments.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
				arguments.add(new BasicNameValuePair("latitude", localCoordX));
				arguments.add(new BasicNameValuePair("longitude", localCoordY));
				arguments.add(new BasicNameValuePair("attendTm", DateUtil.nowDateFormat("HH:mm")));
				arguments.add(new BasicNameValuePair("attendRmk", officeName));	//출근 사무실명 추가
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

			if(session.isCommuteFailed() == true && 	//출근시 음영지역에서 실패하면서
					"퇴근".equals(session.getCommuteStatus()) 	//pref에 들어있는 상태가 출근이면서
					&& session.getWorkDt() != null 	//pref에 WorkDt가 들어있으면서
					&& session.getWorkDt().equals(session.getCommuteWorkDt())) //pref에 WorkDt랑 출근일자가 같으면
			{
				arguments.add(new BasicNameValuePair("usrId", session.getCommuteEmpId()));
				arguments.add(new BasicNameValuePair("workDt", session.getCommuteWorkDt()));
				arguments.add(new BasicNameValuePair("latitude", session.getLatitude()));
				arguments.add(new BasicNameValuePair("longitude", session.getLongitude()));
				arguments.add(new BasicNameValuePair("attendTm", session.getCommuteTime()));
				arguments.add(new BasicNameValuePair("attendRmk", session.getOfficeName()));
			}
			else
			{
				arguments.add(new BasicNameValuePair("usrId", session.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", session.getWorkDt()));
				//arguments.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
				//arguments.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
				arguments.add(new BasicNameValuePair("latitude", localCoordX));
				arguments.add(new BasicNameValuePair("longitude", localCoordY));
				arguments.add(new BasicNameValuePair("attendTm", DateUtil.nowDateFormat("HH:mm")));
				arguments.add(new BasicNameValuePair("attendRmk", officeName));	//출근 사무실명 추가
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

						if(session.getCommuteTime().equals("") == true)
						{
							commuteTime = DateUtil.nowDateFormat("HH:mm");
						}else{
							commuteTime = session.getCommuteTime();
						}

						commuteResult("출근", rtn, commuteTime);
						session.setCommute("", "","", "", "","","","", false);
					} else if (rtn.equals("0")) {
						alert("출근 미처리입니다.", context);
					} else if (rtn.equals("2")) {
						alert("오늘의 출퇴근을 모두 하셨습니다.", context);
					} else if (rtn.equals("5")) {
						if(session.getCommuteTime().equals("") == true)
						{
							commuteTime = DateUtil.nowDateFormat("HH:mm");
						}else{
							commuteTime = session.getCommuteTime();
						}
						commuteResult("출근", rtn, commuteTime);
						session.setCommute("", "","", "", "","","","", false);

					}

				} else if (isError) {
					session.setCommute(session.getEmpId(),
							session.getEmpNm(),
							session.getWorkDt(),
							DateUtil.nowDateFormat("HH:mm"),
							"출근",
							//String.valueOf(location.getLatitude()),
							//String.valueOf(location.getLongitude()),
							localCoordX,
							localCoordY,
							officeName,
							true);
					AlertView.showAlert(ejm01.getValue("errMsg"), context);
					startAttendService();
				}
			} catch (Exception e) {
				session.setCommute(session.getEmpId(),
						session.getEmpNm(),
						session.getWorkDt(),
						DateUtil.nowDateFormat("HH:mm"),
						"출근",
						//String.valueOf(location.getLatitude()),
						//String.valueOf(location.getLongitude()),
						localCoordX,
						localCoordY,
						officeName,
						true);
				AlertView.showAlert(e.toString(), context);
				e.printStackTrace();
				startAttendService();
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
						if(session.getCommuteTime().equals("") == true)
						{
							commuteTime = DateUtil.nowDateFormat("HH:mm");
						}else{
							commuteTime = session.getCommuteTime();
						}
						commuteResult("퇴근", rtn, commuteTime);
						session.setCommute("", "","", "", "","","", "", false);

					} else if (rtn.equals("0")) {
						alert("퇴근 미처리입니다.", context);
					} else if (rtn.equals("2")) {
						alert("이미 퇴근상태입니다.", context);
					} else if (rtn.equals("3")) {
						// 퇴근하려는데 '진행중','계획' 상태인 작업이 있을 경우
						alert("완료 안된 작업이 있습니다. 확인해 주세요.", context);
					}

				}
				else if (isError)
				{
					session.setCommute(session.getEmpId(),
							session.getEmpNm(),
							session.getWorkDt(),
							DateUtil.nowDateFormat("HH:mm"),
							"퇴근",
							//tring.valueOf(location.getLatitude()),
							//String.valueOf(location.getLongitude()),
							localCoordX,
							localCoordY,
							officeName,
							true);
					AlertView.showAlert(ejm01.getValue("errMsg"), context);
					startAttendService();
				}
			} catch (Exception e) {
				session.setCommute(session.getEmpId(),
						session.getEmpNm(),
						session.getWorkDt(),
						DateUtil.nowDateFormat("HH:mm"),
						"퇴근",
						//String.valueOf(location.getLatitude()),
						//String.valueOf(location.getLongitude()),
						localCoordX,
						localCoordY,
						officeName,
						true);
				AlertView.showAlert(e.toString(), context);
				e.printStackTrace();

				startAttendService();
			}

		}

	}// end of setData()

	private class OfficeNfcCheckAsync extends AsyncTask<Void, String, Void> {
		EasyJsonMap ejmMsg, ejmData;

		@Override
		protected void onPostExecute(Void result) {
			// HM_MP00_R00.this.ProgressDialog.dismiss();
			try {
				boolean isError = ejmMsg.getValue("errCd").equals("1") ? true
						: false;
				if (!isError) {

					String rtn = ejmData.getValue("RTN");
					officeName = ejmData.getValue("Office");
					localCoordX = ejmData.getValue("LocalCoordY");	//서버에서 잘못등록한거 X,Y크로스 해서 올림
					localCoordY = ejmData.getValue("LocalCoordX");

					if (rtn.equals("0"))
					{
						alert("등록된 NFC TAG가 없습니다.", context);
					}
					else if(rtn.equals("2"))
					{
						alert("회의실 NFC TAG로는 출근을 할 수 없습니다.", context);
					}
					else
					{ // 1,2,3,사무실,회의실,빌딩 NFC태그 일경우 다 출퇴근 프로세스 탐

						if (attendance.equals("출근"))
						{
							new Database().execute("getToWork_TT");
						}
						else
						{
							new Database().execute("getOffWork_TT");
						}
					}

				} else if (isError) {
					AlertView.showAlert(ejmMsg.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// HM_MP00_R00.this.ProgressDialog =
			// android.app.ProgressDialog.show(
			// HM_MP00_R00.this, "조회중", "태그를 조회중입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				CommonSession cs = new CommonSession(context);
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "sm/checkUserOfficeAttendance_T.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));
				arguments.add(new BasicNameValuePair("nfcTag", tagUid));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejmMsg = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
					ejmData = new EasyJsonMap(
							returnJson.getJSONObject("dataMap"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			AttendanceTagActivity.this.progress = android.app.ProgressDialog
					.show(context, "알림", "조회 중입니다.");
		} else {
			AttendanceTagActivity.this.progress.dismiss();
		}
	}

	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;

	private void setToggleNavi() {
		boolean isHide = naviPref.isHide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int totalHeight = ScreenUtil.getRealScreenHeight(this);;
		int naviHeight = getResources().getDrawable(R.drawable.home_menu_on)
				.getIntrinsicHeight();
		int viewArea = naviHeight / 6;
		int setPadding = totalHeight - viewArea - naviHeight;
		if (isHide) {
			testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
			Log.e("isHide", "naviHide = " + isHide);
		} else {
			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
			Log.e("isHide", "naviHide = " + naviPref.isHide());
		}
	}

	private void navigationInit() {
		testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
		homeNavi = new HomeNavigation(context, null);
		homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		homeNavi.setToggleNavi();
		Button navi = (Button) homeNavi.getBtn_naviHOME();
		navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				naviPref.setHide(!naviPref.isHide());
				homeNavi.setToggleNavi();

			}
		});
	}


	private void startAttendService(){
		Intent intent = new Intent(context, AttendService.class);
		startService(intent);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.offWorkButton:
				if (this.location == null) // 태그했는데 location이 널이면 GPS활성화 한 후에 다시 하도록
				{
					gpsActivation();
				}
				else
				{
					//퇴근 버튼으로만 했을 경우 lat, lng는 GPS location값에서 받아오고 office명은 공백처리한다.
					localCoordX = String.valueOf(location.getLatitude());
					localCoordY = String.valueOf(location.getLongitude());
					officeName = "";
					new Database().execute("getOffWork_TT");
				}
				break;

			default:
				break;
		}
	}
};