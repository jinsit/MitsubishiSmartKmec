package com.jinsit.kmec.WO.WT.MW;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SelectPendCode;
import com.jinsit.kmec.IR.NM.NFCRegistrationData;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM00;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM01;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.GPSService;
import com.jinsit.kmec.comm.TimerService;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.GpsStates;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class WT_MW00_R00 extends Activity implements OnClickListener{
	
	
	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;

	private static final String JOB_ACT_MOVE = "11";
	private static final String JOB_ACT_START = "15";
	private static final String JOB_ACT_ARRIVE = "31";
	private static final String JOB_ACT_COMPLETE = "39";
	Context context;
	Activity activity;
	TextView tv_mw_date, tv_mw_workNm, tv_mw_bldgInfo, tv_mw_addr,
			tv_mw_csDeptNm, tv_mw_empNm1, tv_mw_empHp1, tv_mw_empNm2,
			tv_mw_empHp2, tv_mw_st, tv_mw_cs_fr, tv_mw_moveTm, tv_mw_arriveTm,
			tv_mw_completeTm;
	Button  btn_mw_jobComplete;
	private ProgressDialog ProgressDialog;
	private ProgressDialog progress;

	
	private String jobNo, empId, workDt, carNo;//
	String nfcPlc;
	String refControlNo;
	private String jobAct;
	private EasyJsonMap ejm;
	private CommonSession commonSession;
	private EasyJsonMap ej01;
	private WO_WT00_R01_ITEM00 workTargetData;
	private WO_TS00_R00_ITEM01 item01;

	// /nfc
			private NfcAdapter mNfcAdapter;
			private PendingIntent mNfcPendingIntent;
			private String ndefMsg;
			private String tagUid = "";
			private boolean progressWorking;
			private boolean onPopUp;
			
			
			@Override
			protected void onResume() {
				super.onResume();
				enableTagWriteMode();
				naviPref = new HomeNaviPreference(context);
				navigationInit();
			}
			
			@Override
			protected void onPause() {
				super.onPause();
				disableTagWriteMode();
			}

			@Override
			protected void onNewIntent(Intent intent) {
				if(progressWorking){
					return;
				}
				if(onPopUp){
					return;
				}

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

					if (!tagUid.isEmpty()) {
						progress(true);
						new NfcCheckAsync().execute("bagicWorkTime");
					}
				}

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
			
			private class NfcCheckAsync extends AsyncTask<String, Integer, String> {
				EasyJsonMap ejmMsg, ejmData;
				GetHttp http = new GetHttp();
				JSONObject returnJson01;
				
				

				@Override
				protected void onProgressUpdate(Integer... values) {
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					// 1. bagicWorkTime
					if (params[0].equals("bagicWorkTime")) {
					try {
						
						CommonSession cs = new CommonSession(context);
						String param_url = WebServerInfo.getUrl()
								+ "sm/checkUserOfficeAttendance.do";

						List<NameValuePair> arguments = new ArrayList<NameValuePair>();
						arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));
						arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));
						arguments.add(new BasicNameValuePair("nfcTag", tagUid));
						returnJson01 = http.getPost(param_url, arguments,
								true);

						try {
							ejmMsg = new EasyJsonMap( returnJson01.getJSONObject("msgMap") );
							ejmData = new EasyJsonMap( returnJson01.getJSONObject("dataMap") );
						} catch (JSONException e) {
							e.printStackTrace();
						}

					} catch (Exception ex) {
					}
					return params[0];
				}
				return "None";

				}
				
				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);

					// 1. bagicWorkTime
					if (result.equals("bagicWorkTime")) {
						try {
							boolean isError = ejmMsg.getValue("errCd").equals("1") ? true
									: false;
							if (!isError) {
								String rtn = ejmData.getValue("RTN");
								if (rtn.equals("0")) {
									// nfcTagEvent(ndefMsg);
									progress(false);
									showAlert("등록된 NFC TAG가 없습니다. 태그를 등록하시고 진행하세요.");
								} else if (rtn.equals("1")) {
									// 사무실 태그
									progress(false);
									
										if(workTargetData.getST().equals("계획")){
											//이동
											timeRegistration(JOB_ACT_MOVE);
										}else if(!workTargetData.getST().equals("계획")
												&&workTargetData.getST().equals("이동")){
											//도착
											showAlert("사무실 NFC TAG로는 도착할 수 없습니다.");
											//timeRegistration(JOB_ACT_ARRIVE);
										}else if(workTargetData.getST().equals("작업")){
											showAlert("사무실 NFC TAG로는 작업을 완료할 수 없습니다.");
											//jobAct = JOB_ACT_COMPLETE;
											//repaireTimeRegistration();
										}
										else{
											////계획모드에서만 예약을 할 수 있습니다.
											SimpleDialog sm01 = new SimpleDialog(context, "알림","시작을 먼저 하십시오.");
											sm01.show();
										}
									
									
								} else if (rtn.equals("2")) {
									// 회의실 태그
									progress(false);
									
									if(workTargetData.getST().equals("계획")){
										//이동
										timeRegistration(JOB_ACT_MOVE);
									}else if(!workTargetData.getST().equals("계획")
											&&workTargetData.getST().equals("이동")){
										//도착
										showAlert("회의실 NFC TAG로는 도착할 수 없습니다.");
										//timeRegistration(JOB_ACT_ARRIVE);
									}else if(workTargetData.getST().equals("작업")){
										showAlert("회의실 NFC TAG로는 작업을 완료할 수 없습니다.");
										//jobAct = JOB_ACT_COMPLETE;
										//repaireTimeRegistration();
									}
									else{
										////계획모드에서만 예약을 할 수 있습니다.
										SimpleDialog sm01 = new SimpleDialog(context, "알림","시작을 먼저 하십시오.");
										sm01.show();
									}
									
								
								} else if (rtn.equals("3")) {
									// 빌딩테그
									//progress(false);
								
									
									if (tv_mw_moveTm.getText().toString().equals("")){
										progress(false);
										timeRegistration(JOB_ACT_MOVE);
									} else if (!tv_mw_moveTm.getText().toString().equals("")||
											tv_mw_arriveTm.getText().toString().equals("")) {
										new selectNfcInfo().execute("bagicWorkTime");
									}
								}

							} else if (isError) {
								progress(false);
								showAlert(ejmMsg.getValue("errMsg"));
							}
						} catch (Exception e) {
							e.printStackTrace();
							progress(false);
							showAlert("Nfc Tag 조회 에러. "+e.getMessage());
						}
					}
				}
			}
			
			
			
			

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.menu_ip_wo_r01, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		
		int id = item.getItemId();
	
		String[] param = { empId, workDt, jobNo };
		if (id == R.id.menu_ip_wo_pendWork) {
			// /미처리
			SelectPendCode selectPendCode = new SelectPendCode(context, param,
					activity);
			selectPendCode.show();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_mw00_r00);
		activityInit();
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업대상정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
	}

	private void activityInit() {
		// TODO Auto-generated method stub
		activity = this;
		context = this;
		commonSession = new CommonSession(context);
		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		// Log.e("jobNo", jobNo);
		tv_mw_date = (TextView) findViewById(R.id.tv_mw_date);
		tv_mw_workNm = (TextView) findViewById(R.id.tv_mw_workNm);
		tv_mw_bldgInfo = (TextView) findViewById(R.id.tv_mw_bldgInfo);
		tv_mw_addr = (TextView) findViewById(R.id.tv_mw_addr);
		tv_mw_csDeptNm = (TextView) findViewById(R.id.tv_mw_csDeptNm);
		tv_mw_empNm1 = (TextView) findViewById(R.id.tv_mw_empNm1);
		tv_mw_empHp1 = (TextView) findViewById(R.id.tv_mw_empHp1);
		tv_mw_empNm2 = (TextView) findViewById(R.id.tv_mw_empNm2);
		tv_mw_empHp2 = (TextView) findViewById(R.id.tv_mw_empHp2);
		tv_mw_st = (TextView) findViewById(R.id.tv_mw_st);
		tv_mw_cs_fr = (TextView) findViewById(R.id.tv_mw_cs_fr);
		tv_mw_moveTm = (TextView) findViewById(R.id.tv_mw_moveTm);
		tv_mw_moveTm.setOnClickListener(this);
		tv_mw_arriveTm = (TextView) findViewById(R.id.tv_mw_arriveTm);
		tv_mw_arriveTm.setOnClickListener(this);
		tv_mw_completeTm = (TextView) findViewById(R.id.tv_mw_completeTm);
	
		btn_mw_jobComplete = (Button) findViewById(R.id.btn_mw_jobComplete);
		btn_mw_jobComplete.setOnClickListener(this);
		
		//엔에픘;ㅣ
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		ndefMsg = "";
		tagUid = "";
		// /
		progressWorking = false;
		onPopUp =false;
		new WorkTargetDetailAsync().execute();
		setConfig();
	}
	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	
	private void dataSetting() {
		tv_mw_date.setText(workTargetData.getWORK_DT());
		tv_mw_workNm.setText(workTargetData.getWORK_NM());
		if(workTargetData.getBLDG_NM().equals("")){
			tv_mw_bldgInfo.setText("빌딩정보가 없습니다.");
		}else{
			tv_mw_bldgInfo.setText(workTargetData.getBLDG_NM() + "/"
					+ workTargetData.getCAR_NO());		
		}
	
		tv_mw_addr.setText(workTargetData.getADDR());
		tv_mw_csDeptNm.setText(workTargetData.getCS_DEPT_NM());

		tv_mw_empNm1.setText(workTargetData.getMAIN_EMP_NM());
		tv_mw_empHp1.setText(workTargetData.getMAIN_EMP_PHONE());

		tv_mw_empNm2.setText(workTargetData.getSUB_EMP_NM());
		tv_mw_empHp2.setText(workTargetData.getSUB_EMP_PHONE());

		tv_mw_st.setText(workTargetData.getST());
		tv_mw_cs_fr.setText(workTargetData.getCS_FR());
		tv_mw_moveTm.setText(workTargetData.getMOVE_TM());
		tv_mw_arriveTm.setText(workTargetData.getARRIVE_TM());
		tv_mw_completeTm.setText(workTargetData.getCOMPLETE_TM());

		empId = workTargetData.getCS_EMP_ID();
		workDt = workTargetData.getWORK_DT();
		refControlNo = workTargetData.getREF_CONTR_NO();
		carNo = workTargetData.getCAR_NO();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
	
		case R.id.tv_mw_moveTm:
			if(tv_mw_moveTm.getText().toString().equals("")){
				timeRegistration(JOB_ACT_MOVE);
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				SimpleDialog sm01 = new SimpleDialog(context, "알림","이미 이동하셨습니다.");
				sm01.show();
			}
			break;
		case R.id.tv_mw_arriveTm:
			if(!tv_mw_moveTm.getText().toString().equals("")&&tv_mw_arriveTm.getText().toString().equals("")){
				timeRegistration(JOB_ACT_ARRIVE);
			}else if(!tv_mw_arriveTm.getText().toString().equals("")){
				SimpleDialog sm01 = new SimpleDialog(context, "알림","이미 도착하셨습니다.");
				sm01.show();
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				SimpleDialog sm01 = new SimpleDialog(context, "알림","이동을 먼저 하십시오.");
				sm01.show();
			}
			break;
		case R.id.btn_mw_jobComplete:
			if(!tv_mw_arriveTm.getText().toString().equals("")){
				this.jobAct = JOB_ACT_COMPLETE;
				repaireTimeRegistration();
			}else{
				SimpleDialog sm01 = new SimpleDialog(context, "알림","작업을 먼저 완료하십시오.");
				sm01.show();
			}
			
			
//			if(!workTargetData.getST().equals("작업")&&!workTargetData.getST().equals("이동")){
//				this.jobAct = JOB_ACT_COMPLETE;
//				repaireTimeRegistration();
//			}
//			else{
//				////계획모드에서만 예약을 할 수 있습니다.
//				SimpleDialog sm01 = new SimpleDialog(context, "알림","도착을 먼저 하십시오.");
//				sm01.show();
//			}
			break;
		}
	}
	private void timeRegistration(String jobAct) {
		String dMsg = "";

		if (jobAct.equals(JOB_ACT_MOVE)) {
			dMsg = this.getString(R.string.msg_travelTimeRegistration);
		} else if (jobAct.equals(JOB_ACT_ARRIVE)) {
			dMsg = this.getString(R.string.msg_arrivalTimeRegistration);
		} else if (jobAct.equals(JOB_ACT_COMPLETE)) {
			dMsg = this.getString(R.string.msg_completionTimeRegistration);
		}
		this.jobAct = jobAct;
		
		if(GpsStates.isGpsStatus(context)){
			
			SimpleDialog ynDialog = new SimpleDialog(context, "알림",dMsg,
					new com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener() {

						@Override
						public void onButtonClick() {
							// TODO Auto-generated method stub
							progress(true);
							new TimeStatusChangeAsync().execute("bagicWorkTime");
						}
					});
			ynDialog.show();
		}else{
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
					}).show();
		}
		
	
	}
	
	

	private void progress(Boolean isActivated) {
		if (isActivated) {
			WT_MW00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WT_MW00_R00.this.progress.dismiss();
		}
	}

	private void repaireTimeRegistration() {
		String dMsg = "";
		dMsg = "작업을 완료하시겠습니까?";
		////
		GPSService gs = new GPSService(context);
		if (!gs.isGetLocation()) {
			gpsActivation();
			return;
		}
		
		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, dMsg,
				new btnClickListener(){

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						progress(true);
						new TimeStatusChangeAsync().execute("bagicWorkTime");
						//
						
						
					}
				});
		ynDialog.show();

	}



	
	/**
	 * 작업대상 상세정보 불러오기
	 * 
	 * @author 원성민
	 *
	 */
	private class WorkTargetDetailAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {
		
			try {
				workTargetData = new WO_WT00_R01_ITEM00(ejm.getValue(
						"CS_EMP_ID"), ejm
						.getValue( "WORK_DT"), ejm.getValue(
						 "JOB_NO"),ejm
							.getValue( "WORK_CD"), ejm
						.getValue( "WORK_NM"), ejm.getValue(
						 "ST"), ejm.getValue( "CS_DT"), ejm
						.getValue( "CS_FR"), ejm.getValue(
						"BLDG_NO"), ejm.getValue( "BLDG_NM"),
						ejm.getValue( "CAR_NO"), ejm
								.getValue( "RESERV_ST"), ejm
								.getValue( "ADDR"), ejm
								.getValue( "MAIN_EMP_NM"),
						ejm.getValue( "MAIN_EMP_PHONE"), ejm
								.getValue( "SUB_EMP_NM"), ejm
								.getValue( "SUB_EMP_PHONE"),
						ejm.getValue( "CS_DEPT_NM"), ejm
								.getValue( "NOTIFY_NM"), ejm
								.getValue( "NOTIFY_PHONE"),
						ejm.getValue( "RECEV_DESC"), ejm
								.getValue( "RECEV_TM"), ejm
								.getValue( "RESERV_TM"), ejm
								.getValue( "REPAIR_TM"), ejm
								.getValue( "MOVE_TM"), ejm
								.getValue( "ARRIVE_TM"), ejm
								.getValue( "COMPLETE_TM"),
						ejm.getValue( "RESCUE_TM"), ejm
								.getValue( "START_TM"), ejm
								.getValue( "CONTACT_CD"), ejm
								.getValue( "STATUS_CD"), ejm
								.getValue( "REF_CONTR_NO"),
						ejm.getValue("PARTS_NO"), ejm
								.getValue( "MODEL_NM"), ejm
								.getValue( "RECEV_NO"), ejm
								.getValue( "CHECK_YN"), ejm
								.getValue( "CBS_YN"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			dataSetting();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			workTargetData = new WO_WT00_R01_ITEM00();

			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectWorkTargetDetail.do";
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", 	workDt));
			arguments.add(new BasicNameValuePair("jobNo", jobNo));
			JSONObject returnJson = getHttp.getPost(param_url, arguments,
					true);

			try {
					ejm = new EasyJsonMap(
							returnJson.getJSONObject("dataMap"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			return null;
	}
		
	}
	
	
	
	private class TimeStatusChangeAsync extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

	
		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/updateWorkStatusChanged.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workTargetData.getWORK_DT()));
				arguments.add(new BasicNameValuePair("jobNo",  workTargetData.getJOB_NO()));
				arguments.add(new BasicNameValuePair("jobAct", jobAct));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				returnJson01 = http.getPost(param_url, arguments,
						true);

				try {
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				
				return "None";
			}
			return params[0];
		}
		return "None";

		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				item01 = null;
				try {
					item01 = new WO_TS00_R00_ITEM01(ej01.getValue("JOB_NO"),
							ej01.getValue("JOB_ST_NM"),
							ej01.getValue("AS_TP"),
							ej01.getValue("CS_TM_FR"));
				} catch (JSONException e) {
					e.printStackTrace();
					item01 = null;
				}

				progress(false);
				if(item01 != null){

					if(item01 != null){
						if(jobAct.equals(JOB_ACT_MOVE)){
							startService(context);
							new WorkTargetDetailAsync().execute();
						}else if(jobAct.equals(JOB_ACT_ARRIVE)){
							
							stopService(context);
							CallService.startGPSService(context);
							new WorkTargetDetailAsync().execute();
						}else if(jobAct.equals(JOB_ACT_COMPLETE)){
							CallService.startGPSService(context);
							CallService.allStopService(context);
							finish();
					}
					}
				}
			}
			
		}
	


}

	
	//----------------------내비게이션 영역--------------------------------------//
	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;

	private void setToggleNavi(){
		boolean isHide = naviPref.isHide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int totalHeight = ScreenUtil.getRealScreenHeight(this);;
		int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
		int viewArea = naviHeight/6;
		int setPadding = totalHeight-viewArea-naviHeight;
		 if(isHide){
				testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
				Log.e("isHide", "naviHide = " +isHide );
			}else{
				testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
				Log.e("isHide", "naviHide = " +naviPref.isHide() );
			}
	}
	
	private void navigationInit(){
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

	//----------------------내비게이션 영역--------------------------------------//
	
	private void startService(Context context){
		Intent intent = new Intent(context, TimerService.class);
		context.startService(intent);
	}
	private void stopService(Context context){
		Intent intent = new Intent(context, TimerService.class);
		context.stopService(intent);
	}
	
	public void showAlert(String message)
	{
		if(progressWorking){
			return;
		}
		
		if(!onPopUp){
			onPopUp = true;
		AlertView.showAlert(message, context,new android.content.DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					onPopUp = false;
				}
			});
		}
	}
	public void showAlertYn(String message, android.content.DialogInterface.OnClickListener ocl){
		if(progressWorking){
			return;
		}
		AlertView.confirmYN(context, "알림", message, ocl , new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onPopUp = false;
			}});
	}
	
	private NFCRegistrationData nFCRegistrationData;
	public class selectNfcInfo extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectNfcInfo.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("nfcTagNo", tagUid));
					
					returnJson01 = http.getPost(param_url_01, arguments, true);
					String bldgNo = "";
					try {
						Log.v("returnJson01",returnJson01.toString());
						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return params[0];
			}
			return "None";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				nFCRegistrationData = null;
				try {
					nFCRegistrationData = new NFCRegistrationData(ej01.getValue("BLDG_NO"),
							ej01.getValue("BLDG_NM"), ej01.getValue("CAR_NO"),
							ej01.getValue("DONG_CAR_NO"), 
							ej01.getValue("NFC_PLC"),
							ej01.getValue("NFC_PLC_NM"),
							ej01.getValue("GOOD_NM"), ej01.getValue("NFC_TAG"));
				} catch (JSONException e) {
					e.printStackTrace();
					nFCRegistrationData = null;
				}
				progress(false);
				
				if (nFCRegistrationData != null){
					Log.v("bldgNo",nFCRegistrationData.getBLDG_NO());
					Log.v("bldgNo1",workTargetData.getBLDG_NO());
					Log.v("bldgNo",nFCRegistrationData.getCAR_NO());
					Log.v("carNo1",workTargetData.getCAR_NO());
					Log.v("tag", tagUid);
					if ( !nFCRegistrationData.getBLDG_NO().equals(workTargetData.getBLDG_NO())){
						showAlert("현재 빌딩정보와 Nfc Tag 빌딩정보가 틀립니다.");
					}
					else if(!tv_mw_moveTm.getText().toString().equals("")&&
							tv_mw_arriveTm.getText().toString().equals("")){
						jobAct = JOB_ACT_ARRIVE;
						timeRegistration(JOB_ACT_ARRIVE);
					}
					else if(!tv_mw_moveTm.getText().toString().equals("")&&
							!tv_mw_arriveTm.getText().toString().equals("")){
						jobAct = JOB_ACT_COMPLETE;
						repaireTimeRegistration();
					}
				}
			}
		}
	}// end of SelectData inner-class
	void gpsActivation() {
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
			
				.show();
	}

	
}
