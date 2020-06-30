package com.jinsit.kmec.WO.WT.MW;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM00;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM01;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.GPSService;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;


/**
 * 정기검사 입회화면
 * @author 원성민
 *
 */
public class WT_MW00_R02 extends Activity implements OnClickListener, android.widget.AdapterView.OnItemClickListener{
	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;

	private static final String JOB_ACT_MOVE = "11";
	private static final String JOB_ACT_START = "15";
	private static final String JOB_ACT_ARRIVE = "31";
	private static final String JOB_ACT_COMPLETE = "39";
	
	private static final String WORK_CD = "CA04";
	Context context;
	Activity activity;
	TextView tv_mw_r02bldgInfo,tv_mw_r02addr,tv_mw_r02date, 
	tv_mw_r02workNm,  tv_mw_r02st, tv_mw_r02cs_fr, 	//tv_mw_r02startTm ,
	tv_mw_moveTm, tv_mw_arriveTm,tv_mw_r02completeTm;
	Button  btn_mw_r02jobComplete;
	
	TextView tv_mw_r02CheckDay1, tv_mw_r02FailInfo1,tv_mw_r02Rowid;
	Spinner tv_mw_r02SuccessInfo1,tv_mw_r02InspecterInfo1;
	ListView lv_mw_r02RegularInspection;
	 EditText et ;
	
	WT_MW00_R02_Adapter00 wT_MW00_R02_Adapter00;
	private ProgressDialog ProgressDialog;
	private ProgressDialog progress;

	
	private String jobNo, empId, workDt, carNo,bldgNo;//
	private String successSt="",inspSt="",detailRmk;
	String nfcPlc;
	String refControlNo;
	private String jobAct;
	private EasyJsonMap ejm;
	private CommonSession commonSession;
	private EasyJsonMap ej01;
	private EasyJsonList ejl;
	private WO_WT00_R01_ITEM00 workTargetData;
	private WO_TS00_R00_ITEM01 item01;
	private ArrayList<WT_MW00_R02_ITEM00> wT_MW00_R02_ITEM00;
	
	String item[] = { "불합격", "합격","조건합격" };
	String item2[] = { "관리원", "기술원" };
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_mw00_r02);
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
		tv_mw_r02bldgInfo = (TextView) findViewById(R.id.tv_mw_r02bldgInfo);
		tv_mw_r02addr = (TextView) findViewById(R.id.tv_mw_r02addr);
		tv_mw_r02date = (TextView) findViewById(R.id.tv_mw_r02date);
		tv_mw_r02workNm = (TextView) findViewById(R.id.tv_mw_r02workNm);
		tv_mw_r02st = (TextView) findViewById(R.id.tv_mw_r02st);
		tv_mw_r02cs_fr = (TextView) findViewById(R.id.tv_mw_r02cs_fr);
		//tv_mw_r02startTm = (TextView)findViewById(R.id.tv_mw_r02startTm);
		//tv_mw_r02startTm.setOnClickListener(this);
		
		tv_mw_moveTm = (TextView) findViewById(R.id.tv_mw_moveTm);
		tv_mw_moveTm.setOnClickListener(this);
		tv_mw_arriveTm = (TextView) findViewById(R.id.tv_mw_arriveTm);
		tv_mw_arriveTm.setOnClickListener(this);
		
		tv_mw_r02completeTm = (TextView) findViewById(R.id.tv_mw_r02completeTm);
		btn_mw_r02jobComplete = (Button) findViewById(R.id.btn_mw_r02jobComplete);
		btn_mw_r02jobComplete.setOnClickListener(this);;
		
		tv_mw_r02Rowid=(TextView) findViewById(R.id.tv_mw_r02Rowid);
		tv_mw_r02CheckDay1=(TextView) findViewById(R.id.tv_mw_r02CheckDay1);
		tv_mw_r02CheckDay1.setText(DateUtil.nowDate());
		tv_mw_r02SuccessInfo1=(Spinner) findViewById(R.id.tv_mw_r02SuccessInfo1);
		//tv_mw_r02SuccessInfo1.setOnClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, item);
		tv_mw_r02SuccessInfo1.setAdapter(adapter);
		
		tv_mw_r02SuccessInfo1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//successSt = tv_mw_r02SuccessInfo1.getItemAtPosition(position).toString();
				successSt = String.valueOf(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		
		tv_mw_r02InspecterInfo1=(Spinner) findViewById(R.id.tv_mw_r02InspecterInfo1);
		//tv_mw_r02SuccessInfo1.setOnClickListener(this);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, item2);
		tv_mw_r02InspecterInfo1.setAdapter(adapter2);
		
		tv_mw_r02InspecterInfo1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//successSt = tv_mw_r02SuccessInfo1.getItemAtPosition(position).toString();
				inspSt = String.valueOf(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		
		tv_mw_r02FailInfo1=(TextView) findViewById(R.id.tv_mw_r02FailInfo1);
		tv_mw_r02FailInfo1.setOnClickListener(this);
		
		lv_mw_r02RegularInspection = (ListView)findViewById(R.id.lv_mw_r02RegularInspection);
		lv_mw_r02RegularInspection.setOnItemClickListener(this);
		new WorkTargetDetailAsync().execute();
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}
	
	private void dataSetting() {
		tv_mw_r02bldgInfo.setText(workTargetData.getBLDG_NM() + "/"
				+ workTargetData.getCAR_NO());
		tv_mw_r02addr.setText(workTargetData.getADDR());
		tv_mw_r02date.setText(workTargetData.getWORK_DT());
		tv_mw_r02workNm.setText(workTargetData.getWORK_NM());
		tv_mw_r02st.setText(workTargetData.getST());
		tv_mw_r02cs_fr.setText(workTargetData.getCS_FR());
		//tv_mw_r02startTm.setText(workTargetData.getSTART_TM());
		tv_mw_moveTm.setText(workTargetData.getMOVE_TM());
		tv_mw_arriveTm.setText(workTargetData.getARRIVE_TM());
		tv_mw_r02completeTm.setText(workTargetData.getCOMPLETE_TM());
		empId = workTargetData.getCS_EMP_ID();
		workDt = workTargetData.getWORK_DT();
		refControlNo = workTargetData.getREF_CONTR_NO();
		carNo = workTargetData.getCAR_NO();
		bldgNo = workTargetData.getBLDG_NO();
		
		new RegularInspectionAsync().execute();
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
//		case R.id.tv_mw_r02SuccessInfo1:
//			this.successInfoChoice();
//			
//			break;
		case R.id.tv_mw_r02FailInfo1:
			//부적합내용 입력할 에디트창 팝업
			this.failInfoInput();
			
			break;
	/*	case R.id.tv_mw_r02startTm:
			if(workTargetData.getST().equals("계획")){
				timeRegistration(JOB_ACT_START);
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				SimpleDialog sm01 = new SimpleDialog(context, "알림","계획상태에서 이동을 할 수 있습니다.");
				sm01.show();
			}
			break;*/
	
		case R.id.btn_mw_r02jobComplete:
			
			if(!tv_mw_arriveTm.getText().toString().equals("")&&!successSt.equals("")){
				this.jobAct = JOB_ACT_COMPLETE;
				repaireTimeRegistration();
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				SimpleDialog sm01 = new SimpleDialog(context, "알림","시작을 먼저 하십시오.");
				sm01.show();
			}
			break;
		}
	}
	
	
//	private void successInfoChoice() {
//		// TODO Auto-generated method stub
//		
//			final String item[] = { "불합격", "합격","조건합격" };
//			new AlertDialog.Builder(context).setTitle("합격여부")
//					.setItems(item, new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							switch (which) {
//							case 0:
//								successSt="0";
//								tv_mw_r02SuccessInfo1.setText(item[which]);
//								break;
//							case 1:
//								successSt="1";
//								tv_mw_r02SuccessInfo1.setText(item[which]);
//								break;
//							case 2:
//								successSt="2";
//								tv_mw_r02SuccessInfo1.setText(item[which]);
//								break;
//							default:
//								//successSt="3";
//								//Log.e("countST",countSt+"");	
//								break;
//							}
//							
//						}
//					}).show();
//		
//	}

	private void failInfoInput() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout dial = (LinearLayout) inflater.inflate(
				R.layout.wt_mw00_r02_dialog_edittext, null);
		  et = (EditText) dial
				.findViewById(R.id.et_mw_r02FailureInput);
		  hd_etNumeric.sendEmptyMessageDelayed(0, 100);
		new AlertDialog.Builder(context)
				.setView(dial)
				.setNegativeButton("취소",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								return;
							}
						})

				.setPositiveButton("확인",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								detailRmk = et.getText().toString();
								tv_mw_r02FailInfo1.setText(detailRmk);
								
											}
						}).show();
	}
	

	private void timeRegistration(String jobAct) {
		String dMsg = "";

//		if (jobAct.equals(JOB_ACT_START)) {
//			dMsg = "작업을 시작하시겠습니까?";
//		}else if (jobAct.equals(JOB_ACT_COMPLETE)) {
//			dMsg = this.getString(R.string.msg_completionTimeRegistration);
//		}
		
		
		GPSService gs = new GPSService(context);
		if (!gs.isGetLocation()) {
			gpsActivation();
			return;
		}
		if (jobAct.equals(JOB_ACT_MOVE)) {
			dMsg = this.getString(R.string.msg_travelTimeRegistration);
		} else if (jobAct.equals(JOB_ACT_ARRIVE)) {
			dMsg = this.getString(R.string.msg_arrivalTimeRegistration);
		} else if (jobAct.equals(JOB_ACT_COMPLETE)) {
			dMsg = this.getString(R.string.msg_completionTimeRegistration);
		}
		this.jobAct = jobAct;
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
	}
	
	
	
	private void progress(Boolean isActivated) {
		if (isActivated) {
			WT_MW00_R02.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WT_MW00_R02.this.progress.dismiss();
		}
	}

	private void repaireTimeRegistration() {
		String dMsg = "";
		dMsg = "작업을 완료하시겠습니까?";
		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, dMsg,
				new btnClickListener(){

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new UpdateRegularInspectionAsync().execute();
						
					
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
			WT_MW00_R02.this.ProgressDialog.dismiss();
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
			WT_MW00_R02.this.ProgressDialog = android.app.ProgressDialog.show(
					WT_MW00_R02.this, "작업대상", "작업대상목록 불러오는중");
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
	
	private class RegularInspectionAsync extends AsyncTask<Void, String, Void> {
	
		@Override
		protected void onPostExecute(Void result) {
			WT_MW00_R02.this.ProgressDialog.dismiss();
			
			wT_MW00_R02_Adapter00 = new WT_MW00_R02_Adapter00(context,wT_MW00_R02_ITEM00);
			lv_mw_r02RegularInspection.setAdapter(wT_MW00_R02_Adapter00);
			
			listViewHeightSet(wT_MW00_R02_Adapter00, lv_mw_r02RegularInspection);
			 int rowId = wT_MW00_R02_ITEM00.size() + 1;
			 tv_mw_r02Rowid.setText(rowId + "차 검사일"); 
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WT_MW00_R02.this.ProgressDialog = android.app.ProgressDialog.show(
					WT_MW00_R02.this, "정기검사 입회", "입회내역을 불러오는중입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {

			wT_MW00_R02_ITEM00 = new ArrayList<WT_MW00_R02_ITEM00>();

			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectRegularInspection.do";
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("workCd", WORK_CD));
			arguments.add(new BasicNameValuePair("bldgNo", 	bldgNo));
			arguments.add(new BasicNameValuePair("carNo", carNo));
			JSONObject returnJson = getHttp.getPost(param_url, arguments,
					true);

			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				wT_MW00_R02_ITEM00.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for (int i = 0; i < jsonSize; i++) {
					wT_MW00_R02_ITEM00.add(new WT_MW00_R02_ITEM00(ejl.getValue(i,"ROW_NUM"), 
							ejl.getValue(i,"WORK_DT"),
							ejl.getValue(i,"INSP_ST"),
							ejl.getValue(i,"INSP_ST_NM"),
							ejl.getValue(i,"JOB_ST"),
							ejl.getValue(i,"JOB_ST_NM"), 
							ejl.getValue(i,"DETAIL_RMK")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
	}
		
	}
	
	
	
	/**
	 * 정기검사 입회 등록
	 * @author 원성민
	 *
	 */
	private class UpdateRegularInspectionAsync extends AsyncTask<Void, String, Void> {
		private String updateRet="";
		@Override
		protected void onPostExecute(Void result) {
			WT_MW00_R02.this.ProgressDialog.dismiss();
			
			if(updateRet.equals("1")){
				progress(true);
				new TimeStatusChangeAsync().execute("bagicWorkTime");	
			}else{
				
			}
			
		
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WT_MW00_R02.this.ProgressDialog = android.app.ProgressDialog.show(
					WT_MW00_R02.this, "정기검사입회", "저장중입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {

		

			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/updateRegularInspection.do";
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("workCd", WORK_CD));
			arguments.add(new BasicNameValuePair("bldgNo", 	bldgNo));
			arguments.add(new BasicNameValuePair("carNo", 	carNo));
			arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", workDt));
			arguments.add(new BasicNameValuePair("jobNo", jobNo));
			arguments.add(new BasicNameValuePair("inspSt", inspSt));
//			arguments.add(new BasicNameValuePair("csTmFr", workTargetData.getCS_FR()));
//			arguments.add(new BasicNameValuePair("csTmTo", DateUtil.nowDateFormat("HH:mm")));
			arguments.add(new BasicNameValuePair("jobSt", successSt));
			arguments.add(new BasicNameValuePair("detailRmk", detailRmk));
			arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
			
			JSONObject returnJson = getHttp.getPost(param_url, arguments,
					true);
			try {

				updateRet = returnJson.getString("dataString");
				Log.v("updateRet", "UpdateRet = " + updateRet);


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
				// 로그인이 실패하였습니다 띄어주기
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
						
						
						if(jobAct.equals(JOB_ACT_COMPLETE)){
							CallService.startGPSService(context);
							finish();
						}else if(jobAct.equals(JOB_ACT_MOVE)){
							CallService.startTimerService(context);
							new WorkTargetDetailAsync().execute();
						}else if(jobAct.equals(JOB_ACT_ARRIVE)){	
						
							CallService.stopTimerService(context);
							CallService.startGPSService(context);
							new WorkTargetDetailAsync().execute();
						}
						
						}
					}
				}
			}
		}


	private void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		WT_MW00_R02_ITEM00 mData = wT_MW00_R02_ITEM00.get(position);
		WT_MW00_R02P mw02 = new WT_MW00_R02P(context, mData);
		mw02.show();
		
	}
	
	private Handler hd_etNumeric = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm;
			imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(et, 0);
		}
	};

	
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
	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
	//----------------------내비게이션 영역--------------------------------------//
	private void gpsActivation() {
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