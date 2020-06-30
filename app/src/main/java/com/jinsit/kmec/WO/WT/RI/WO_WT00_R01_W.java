package com.jinsit.kmec.WO.WT.RI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SelectInspector;
import com.jinsit.kmec.CM.SearchAdminInfo;
import com.jinsit.kmec.CM.SelectPendCode;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.IR.NM.IR_NM00_R00;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * WO_WT00_R01 
 * @discription 작업대상상세정보 현재 작업상태, 진행현황을 보여준다. 
 * @author 원성민
 * @deprecated 이 소스는 정기정검 웹 기반이기 때문에 사용하지 않습니다.
 *
 */
public class WO_WT00_R01_W extends Activity implements OnClickListener,
		OnItemClickListener {
	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;

	private static final String JOB_ACT_MOVE = "11";
	private static final String JOB_ACT_START = "15";
	private static final String JOB_ACT_ARRIVE = "31";
	private static final String JOB_ACT_COMPLETE = "39";
	private String jobAct = "";
	private ListView lv_wt_jobStatus = null;

	private ProgressDialog ProgressDialog;
	//private ArrayList<WorkTargetData> workTargetData;
	private WO_WT00_R01_ITEM00 workTargetData;
	private ArrayList<WO_WT00_R01_ITEM01> workStatusData;
	WO_WT00_R01_ITEM01 nfcPosition;
	
	private WO_WT00_R01_Adapter00_W workStatusAdapter;
	private WO_WT00_R01_Adapter01 wO_WT00_R01_Adapter01;
	private EasyJsonList ejl;
	private EasyJsonMap ejm;
	private RoutineCheckListData routineCheckListData;
	Context context;
	Activity activity;
	TextView tv_wt_date, tv_wt_workNm, tv_wt_bldgInfo, tv_wt_addr,
			tv_wt_csDeptNm, tv_wt_empNm1, tv_wt_empHp1, tv_wt_empNm2,
			tv_wt_empHp2, tv_wt_st, tv_wt_cs_fr, tv_wt_moveTm, tv_wt_arriveTm,
			tv_wt_completeTm;
	Button btn_wt_adminInfo, btn_wt_jobComplete;
	private String jobNo, empId, workDt, carNo;//
	String nfcPlc;
	String refControlNo;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	private CommonSession commonSession;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ip_wo_r00, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// String[] param = {empId, DateUtil.nowDate(), jobNo};
		String[] param = { empId, workDt, jobNo };
		if (id == R.id.menu_ip_wo_pendWork) {
			// /미처리

			SelectPendCode selectPendCode = new SelectPendCode(context, param,
					activity);
			selectPendCode.show();

			return true;
		} else if (id == R.id.menu_ip_wo_transferWork) {
			// 이관
			CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
					context, REGISTER_TRANSFER, param, activity);
			cM_SelectInspector.show();
			return true;
		} else if (id == R.id.menu_ip_wo_helpWork) {
			// 지원요청
			CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
					context, REGISTER_HELP, param, activity);
			cM_SelectInspector.show();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mNfcAdapter != null)
			enableTagWriteMode();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		new WorkTargetDetailAsync().execute();
		new WorkStatusAsync().execute();

		Log.e("onRestart", "onRestart  =");
	};

	@Override
	protected void onPause() {
		super.onPause();
		if (mNfcAdapter != null)
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

			if (!ndefMsg.isEmpty()) {
			nfcTagEvent(ndefMsg);
			} else {
				// 비어있을 때 작업
				SimpleYesNoDialog synd = new SimpleYesNoDialog(context,
						"등록되지 않은 태그입니다. 태그등록 하시겠습니까?", new btnClickListener() {
							@Override
							public void onButtonClick() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,IR_NM00_R00.class);
								startActivity(intent);
								finish();
							}
						});
				synd.show();
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

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r01);
		activityInit();
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	private void activityInit() {
		// TODO Auto-generated method stub
		activity = this;
		context = this;

		commonSession = new CommonSession(context);
		jobNo = getIntent().getExtras().getString("jobNo");
		// Log.e("jobNo", jobNo);
		lv_wt_jobStatus = (ListView) findViewById(R.id.lv_wt_jobStatus);
		lv_wt_jobStatus.setOnItemClickListener(this);
		tv_wt_date = (TextView) findViewById(R.id.tv_wt_date);
		tv_wt_workNm = (TextView) findViewById(R.id.tv_wt_workNm);
		tv_wt_bldgInfo = (TextView) findViewById(R.id.tv_wt_bldgInfo);
		tv_wt_addr = (TextView) findViewById(R.id.tv_wt_addr);
		tv_wt_csDeptNm = (TextView) findViewById(R.id.tv_wt_csDeptNm);
		tv_wt_empNm1 = (TextView) findViewById(R.id.tv_wt_empNm1);
		tv_wt_empHp1 = (TextView) findViewById(R.id.tv_wt_empHp1);
		tv_wt_empNm2 = (TextView) findViewById(R.id.tv_wt_empNm2);
		tv_wt_empHp2 = (TextView) findViewById(R.id.tv_wt_empHp2);
		tv_wt_st = (TextView) findViewById(R.id.tv_wt_st);
		tv_wt_cs_fr = (TextView) findViewById(R.id.tv_wt_cs_fr);
		tv_wt_moveTm = (TextView) findViewById(R.id.tv_wt_moveTm);
		tv_wt_moveTm.setOnClickListener(this);
		tv_wt_arriveTm = (TextView) findViewById(R.id.tv_wt_arriveTm);
		tv_wt_arriveTm.setOnClickListener(this);
		tv_wt_completeTm = (TextView) findViewById(R.id.tv_wt_completeTm);
		btn_wt_adminInfo = (Button) findViewById(R.id.btn_wt_adminInfo);
		btn_wt_adminInfo.setOnClickListener(this);
		btn_wt_jobComplete = (Button) findViewById(R.id.btn_wt_jobComplete);
		btn_wt_jobComplete.setOnClickListener(this);

		if(NetworkStates.isNetworkStatus(context)){
			new WorkTargetDetailAsync().execute();
			new WorkStatusAsync().execute();	
		}else{
			localDetailSet();
		
			}
			
	
		}
		
	private void localDetailSet(){
		String query = new DatabaseRawQuery().selectJobDetail(commonSession.getEmpId(), workDt, jobNo);
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
					null); // 쿼리 날리고 
		mCursor.moveToFirst();
		
				
		WO_WT00_R01_ITEM00 wO_WT00_R01_ITEM00 = new WO_WT00_R01_ITEM00();
		Field[] fields = wO_WT00_R01_ITEM00.getClass().getDeclaredFields();
		String name = "";
		String value = "";
		for (Field field : fields) {
			name = field.getName();
			try {
				value = mCursor.getString(mCursor.getColumnIndex(name));
				//value = field.get(partCheckListData.get(i)).toString();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
				 }
			try {
				field.set(wO_WT00_R01_ITEM00, value);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		workTargetData = wO_WT00_R01_ITEM00;
		mCursor.close();
		db.close();
		dbHelper.close();
		dataSetting();	
		localPartSet();
	}
	private void localPartSet(){
		String query = new DatabaseRawQuery().selectCheckListPart(commonSession.getEmpId(), workDt, jobNo);
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
					null); // 쿼리 날리고 
		mCursor.moveToFirst();
		wO_WT00_R01_Adapter01 =
			new WO_WT00_R01_Adapter01(activity,
					R.layout.listitem_wt_jobstatus, mCursor, new String[] {
							 "NFC_PLC_NM", "JOB_ST_NM" },
					null, 0);
		lv_wt_jobStatus.setAdapter(wO_WT00_R01_Adapter01);
		  
		db.close();
		dbHelper.close();
	}
	


	private void dataSetting() {
		tv_wt_date.setText(workTargetData.getWORK_DT());
		tv_wt_workNm.setText(workTargetData.getWORK_NM());
		tv_wt_bldgInfo.setText(workTargetData.getBLDG_NM() + "/"
				+ workTargetData.getCAR_NO());
		tv_wt_addr.setText(workTargetData.getADDR());
		tv_wt_csDeptNm.setText(workTargetData.getCS_DEPT_NM());

		tv_wt_empNm1.setText(workTargetData.getMAIN_EMP_NM());
		tv_wt_empHp1.setText(workTargetData.getMAIN_EMP_PHONE());

		tv_wt_empNm2.setText(workTargetData.getSUB_EMP_NM());
		tv_wt_empHp2.setText(workTargetData.getSUB_EMP_PHONE());

		tv_wt_st.setText(workTargetData.getST());
		tv_wt_cs_fr.setText(workTargetData.getCS_FR());
		tv_wt_moveTm.setText(workTargetData.getMOVE_TM());
		tv_wt_arriveTm.setText(workTargetData.getARRIVE_TM());
		tv_wt_completeTm.setText(workTargetData.getCOMPLETE_TM());

		empId = workTargetData.getCS_EMP_ID();
		workDt = workTargetData.getWORK_DT();
		refControlNo = workTargetData.getREF_CONTR_NO();
		carNo = workTargetData.getCAR_NO();
	}

	/**
	 * 각 파트별 작업상태 변화
	 * 
	 * @author 원성민
	 *
	 */
	private class WorkStatusAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {

			workStatusAdapter = new WO_WT00_R01_Adapter00_W(context, workStatusData);
			lv_wt_jobStatus.setAdapter(workStatusAdapter);
			// dataSetting();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				workStatusData = new ArrayList<WO_WT00_R01_ITEM01>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/selectWorkStatus.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();

				arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					workStatusData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						workStatusData.add(new WO_WT00_R01_ITEM01(ejl.getValue(i,
								"NFC_PLC"), ejl.getValue(i, "NFC_PLC_NM"), ejl
								.getValue(i, "JOB_ST"), ejl.getValue(i,
								"JOB_ST_NM")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}

	private class TimeStatusChangeAsync extends AsyncTask<Void, String, Void> {
		private int timeStatusResult = 0;

		@Override
		protected void onPostExecute(Void result) {

			if (timeStatusResult != 0) {
				new WorkTargetDetailAsync().execute();
			} else {
				Toast.makeText(context, "시간변경이 안되었습니다?", 2000).show();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/updateWorkStatusChanged.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();

				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				arguments.add(new BasicNameValuePair("jobAct", jobAct));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));

					timeStatusResult = returnJson.getJSONArray("dataList")
							.length();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
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
			WO_WT00_R01_W.this.ProgressDialog.dismiss();
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
			WO_WT00_R01_W.this.ProgressDialog = android.app.ProgressDialog.show(
					WO_WT00_R01_W.this, "작업대상", "작업대상목록 불러오는중");
		}

		@Override
		protected Void doInBackground(Void... params) {

			workTargetData = new WO_WT00_R01_ITEM00();

			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectWorkTargetDetail.do";
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", workDt));
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
	/**
	 * 작업완료 등록
	 * 
	 * @author 원성민
	 *
	 */
	private class WorkCompleteAsync extends AsyncTask<Void, String, Void> {
		private String retMsg = "";

		@Override
		protected void onPostExecute(Void result) {
			if (retMsg.equals("1")) {
				Toast.makeText(context, "작업을 완료등록하였습니다.", 2000).show();
				NextWorkDialog nwd = new NextWorkDialog(context,
						"다음작업은 무엇입니까?", null, activity, routineCheckListData);
				nwd.show();
			} else {
				Toast.makeText(context, "완료등록을 실패하였습니다.", 2000).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				workStatusData = new ArrayList<WO_WT00_R01_ITEM01>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/updateWorkComplete.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();


				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				arguments.add(new BasicNameValuePair("carNo", carNo));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					retMsg = returnJson.getString("dataString");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.tv_wt_moveTm:
			String moveTm = workTargetData.getMOVE_TM();
			if (moveTm.equals(""))
				timeRegistration(JOB_ACT_MOVE);
			break;
		case R.id.tv_wt_arriveTm:
			String arriveTm = workTargetData.getARRIVE_TM();
			if (arriveTm.equals(""))
				timeRegistration(JOB_ACT_ARRIVE);
			break;

		case R.id.btn_wt_adminInfo:
			// 관리자정보 호출
			SearchAdminInfo searchAdminInfo = new SearchAdminInfo(context,
					workTargetData.getBLDG_NO());
			searchAdminInfo.show();
			break;
		case R.id.btn_wt_jobComplete:
			// 작업완료 등록
			boolean jobStatus = true;
			for (int i = 0; i < workStatusData.size(); i++) {
				if (!workStatusData.get(i).getJOB_ST().equals("39")) {
					jobStatus = false;
					break;
				}
			}

			if (jobStatus) {
				jobComplete();
			} else {
				Toast.makeText(context, "모든 점검항목을 완료해주세요", 2000).show();
			}

			break;

		}
	}

	private void jobComplete() {

		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
				"작업을 완료하시겠습니까?", new btnClickListener() {
					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub

						new RoutineCheckListAsync().execute();
					}
				});
		ynDialog.show();

	}

	private void timeRegistration(String action) {
		String dMsg = "";

		if (action.equals(JOB_ACT_MOVE)) {
			dMsg = "출발 하시겠습니까?";
			jobAct = JOB_ACT_MOVE;
		} else if (action.equals(JOB_ACT_ARRIVE)) {
			dMsg = "도착하였습니까?";
			jobAct = JOB_ACT_ARRIVE;
		}
		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, dMsg,
				new btnClickListener() {

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new TimeStatusChangeAsync().execute();
					}
				});
		ynDialog.show();

	}
	private void startCheckList(){
		 nfcPosition = null;
			for (int i = 0; i < workStatusData.size(); i++) {
				if (nfcPlc.equals(workStatusData.get(i).getNFC_PLC())) {
					nfcPosition = workStatusData.get(i);
					break;
				}
			}
			
			if (nfcPosition != null) {
				SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
						nfcPosition.getNFC_PLC_NM() + " 점검을 시작하시겠습니까?",
						new btnClickListener() {

							@Override
							public void onButtonClick() {
								// TODO Auto-generated method stub
								new StartCheckListAsync().execute();


							}
						});
				ynDialog.show();
			}

			
		
	}
	private void nfcTagEvent(String ndefMessage) {
		// TODO Auto-generated method stub
		nfcPlc = ndefMessage.substring(ndefMessage.length() - 2);
		Log.e("nfcPlc no", "nfcPlc = " + nfcPlc);
		//carNo = ndefMessage.substring(11, 20);
		int indexOOf = ndefMessage.indexOf("&&&");
		Log.w("carNo no", "carNo = " + carNo);
		Log.e("indexOOf no", "indexOOf = " + indexOOf);
		String moveTm = workTargetData.getMOVE_TM();
		String arriveTm = workTargetData.getARRIVE_TM();
	
		if(carNo.equals(ndefMessage.substring(11, 20))){
		////태그된 nfc의 카넘버와 현재 앱의 카넘버가 일치하면 작업을 실행한다.
			if (moveTm.equals("")) {
			// /출발시간이 등록안된경우
			timeRegistration(JOB_ACT_MOVE);
		} else if (arriveTm.equals("")) {
			// /도착시간이 등록안된경우
			timeRegistration(JOB_ACT_ARRIVE);

		} else {
			boolean jobStatus = true;
			for (int i = 0; i < workStatusData.size(); i++) {
				if (!workStatusData.get(i).getJOB_ST().equals("39")) {
					jobStatus = false;
					break;
				}
			}

			if (jobStatus) {
				// 모든점검항목이 완료됐으면 완료등록 팝업
				jobComplete();
			} else {
				// /출발,도착시간이 등록되어있다면 각파트별 점검을 체크한다.
				startCheckList();
			}

		}
			}else{
			Toast.makeText(context, "현재작업과 태그한 호기정보가 일치하지 않습니다.", 2000).show();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		

		Cursor cursor = wO_WT00_R01_Adapter01.getCursor();
		nfcPlc = cursor.getString(cursor.getColumnIndex("NFC_PLC"));
		
		
		
		
		
		nfcPlc = workStatusData.get(position).getNFC_PLC();
		startCheckList();
	
	}

	private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {
		ArrayList<RoutineCheckListData> routineCheckListItem;

		@Override
		protected void onPostExecute(Void result) {
			int index = 0;
			for (int i = 0; i < routineCheckListItem.size(); i++) {
				if (refControlNo.equals(routineCheckListItem.get(i)
						.getREF_CONTR_NO())) {
					index = i;
					break;
				}
			}
			routineCheckListData = routineCheckListItem.get(index);
			new WorkCompleteAsync().execute();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			routineCheckListItem = new ArrayList<RoutineCheckListData>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectRoutineCheckList.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", workDt));
			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				routineCheckListItem.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for (int i = 0; i < jsonSize; i++) {
					routineCheckListItem.add(new RoutineCheckListData(ejl
							.getValue(i, "WORK_DT"),
							ejl.getValue(i, "BLDG_NO"), ejl.getValue(i,
									"BLDG_NM"), ejl.getValue(i, "E_TEXT"), ejl
									.getValue(i, "I_CNT"), ejl.getValue(i,
									"T_CNT"), ejl.getValue(i, "REF_CONTR_NO")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

	}
	
	
	private class StartCheckListAsync extends AsyncTask<Void, String, Void> {
		private String retMsg = "";

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(Void result) {
			if (retMsg.equals("1")) {
					Toast.makeText(context, "작업이 시작되었습니다", 1000).show();

					Intent intent = new Intent(
							WO_WT00_R01_W.this, WO_WT00_R02.class);
					intent.putExtra("nfcPlc", nfcPlc);
					intent.putExtra("jobNo", jobNo);
					startActivity(intent);
			} else {
				Toast.makeText(context, "작업을 시작할 수 없습니다.", 1000).show();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/updatePartCheckState.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", "1"));
				arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
				arguments.add(new BasicNameValuePair("wkTm", DateUtil.nowDateFormat("HH:mm")));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					retMsg = returnJson.getString("dataString");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}
}
