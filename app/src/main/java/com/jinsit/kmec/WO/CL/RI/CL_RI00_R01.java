package com.jinsit.kmec.WO.CL.RI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.WO.WT.RI.RoutineCheckListData;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_Adapter01;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM00;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM01;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R04;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
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
 * CL_RI00_R01 
 * @discription 완료목록의 상세화면
 *  작업대상상세정보 완료상태, 진행현황을 보여준다.
 * @author 원성민
 *
 */
public class CL_RI00_R01 extends Activity implements	OnItemClickListener, OnClickListener  {
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

	ArrayList<RoutineCheckListData> routineCheckListItem;
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

	private LinearLayout linear_wt_mainInspector;	//주점검자, /부점검자 클릭 리스너
	private LinearLayout linear_wt_subInspector;
	private RadioGroup radioGroupInspector;
	private RadioButton radioButtonSingleInsp, radioButtonDoubleInsp;
	private TextView tv_mainInspName, tv_mainInspCode, tv_subInspName, tv_subInspCode;
	private String InspTyp; //점검타입 CsTp
	private Button btn_wt_sendData;


	private String ndefMsg;
	private String tagUid = "";


	private CommonSession commonSession;

	@Override
	protected void onResume() {
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
		localDetailSet();

	}

	@Override
	protected void onRestart() {
		super.onRestart();

		Log.e("onRestart", "onRestart  =");
	};

	@Override
	protected void onPause() {
		super.onPause();

	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r01);
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

		tv_wt_arriveTm = (TextView) findViewById(R.id.tv_wt_arriveTm);

		tv_wt_completeTm = (TextView) findViewById(R.id.tv_wt_completeTm);
		btn_wt_adminInfo = (Button) findViewById(R.id.btn_wt_adminInfo);

		btn_wt_jobComplete = (Button) findViewById(R.id.btn_wt_jobComplete);
		//btn_wt_jobComplete.setVisibility(View.GONE);
		btn_wt_jobComplete.setText("고객승인 보기");
		btn_wt_jobComplete.setOnClickListener(this);

		linear_wt_mainInspector = (LinearLayout)findViewById(R.id.linear_wt_mainInspector);
		linear_wt_mainInspector.setOnClickListener(this);
		linear_wt_subInspector = (LinearLayout)findViewById(R.id.linear_wt_subInspector);
		linear_wt_subInspector.setOnClickListener(this);

		radioGroupInspector= (RadioGroup)findViewById(R.id.radioGroupInspector);
		radioButtonSingleInsp = (RadioButton) findViewById(R.id.radioButtonSingleInsp);
		radioButtonSingleInsp.setClickable(false);
		radioButtonDoubleInsp = (RadioButton) findViewById(R.id.radioButtonDoubleInsp);
		radioButtonDoubleInsp.setClickable(false);

		tv_mainInspName = (TextView) findViewById(R.id.tv_mainInspName);
		tv_mainInspCode = (TextView) findViewById(R.id.tv_mainInspCode);
		tv_subInspName = (TextView) findViewById(R.id.tv_subInspName);
		tv_subInspCode = (TextView) findViewById(R.id.tv_subInspCode);

		btn_wt_sendData = (Button)findViewById(R.id.btn_wt_sendData);
		btn_wt_sendData.setOnClickListener(this);
		btn_wt_sendData.setEnabled(true);

		new SignAsync().execute();
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
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
		local점검자Set();
	}

	private void local점검자Set(){
		String query = new DatabaseRawQuery().selectInspector(commonSession.getEmpId(), workDt, jobNo);

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고
		mCursor.moveToFirst();

		String csTp = mCursor.getString(1);
		String main = mCursor.getString(2);
		String mainNm = mCursor.getString(3);
		String sub = mCursor.getString(4);
		String subNm = mCursor.getString(5);

		if(main != null){
			tv_mainInspCode.setText(main);
			tv_mainInspName.setText(mainNm);
		}else{
			tv_mainInspCode.setText(commonSession.getMngUsrId());
			tv_mainInspName.setText(commonSession.getEmpNm());

		}
		if(sub != null){
			tv_subInspCode.setText(sub);
			tv_subInspName.setText(subNm);
		}else{
			tv_subInspCode.setText("");
			tv_subInspName.setText("");
		}

		//레디오 그룹 셋팅해주기
		if("1".equals(csTp)){
			this.singleInspectorClick();
		}else{
			this.doubleInspectorClick();
		}

	}

	private void localPartSet(){
		String query = new DatabaseRawQuery().selectCheckListPart(commonSession.getEmpId(), workDt, jobNo);

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
				null); // 쿼리 날리고
		mCursor.moveToFirst();

		nfcPositionInit(mCursor);
		nfcWorkStateInit(mCursor);
		//workStateInit(mCursor);


		wO_WT00_R01_Adapter01 =
				new WO_WT00_R01_Adapter01(activity,
						R.layout.listitem_wt_jobstatus, mCursor, new String[] {
						"NFC_PLC_NM", "JOB_ST_NM" },
						null, 0);
		lv_wt_jobStatus.setAdapter(wO_WT00_R01_Adapter01);

		db.close();
		dbHelper.close();
		listViewHeightSet(wO_WT00_R01_Adapter01, lv_wt_jobStatus);
	}

	void nfcWorkStateInit(Cursor mCursor){
		workStatusData = new ArrayList<WO_WT00_R01_ITEM01>();
		WO_WT00_R01_ITEM01 wO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
		Field[] fields = wO_WT00_R01_ITEM01.getClass().getDeclaredFields();
		String name = "";
		String value = "";

		WO_WT00_R01_ITEM01 insertWO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
		if(mCursor !=null&&mCursor.getCount()!=0){
			do{

				insertWO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
				for (Field field : fields) {
					name = field.getName();
					try {

						value = mCursor.getString(mCursor.getColumnIndex(name));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						field.set(insertWO_WT00_R01_ITEM01, value);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				workStatusData.add(insertWO_WT00_R01_ITEM01);
			}while(mCursor.moveToNext());

		}else{

		}

	}

	private void nfcPositionInit(Cursor cursor) {
		// TODO Auto-generated method stub
		try{

			WO_WT00_R01_ITEM01 wO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
			Field[] fields = wO_WT00_R01_ITEM01.getClass().getDeclaredFields();
			String name = "";
			String value = "";
			for (Field field : fields) {
				name = field.getName();
				try {
					value = cursor.getString(cursor.getColumnIndex(name));

					//value = field.get(partCheckListData.get(i)).toString();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					field.set(wO_WT00_R01_ITEM01, value);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			nfcPosition = wO_WT00_R01_ITEM01;
		}catch(Exception e){
			nfcPosition = null;
		}
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
	 * 작업완료 등록
	 *
	 * @author 원성민
	 *
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub



		Cursor cursor = wO_WT00_R01_Adapter01.getCursor();
		nfcPlc = cursor.getString(cursor.getColumnIndex("NFC_PLC"));
		nfcPlcNm = cursor.getString(cursor.getColumnIndex("NFC_PLC_NM"));

		Intent intent = new Intent(
				CL_RI00_R01.this, CL_RI00_R02.class);
		intent.putExtra("nfcPlc", nfcPlc);
		intent.putExtra("jobNo", jobNo);
		intent.putExtra("workDt", workDt);
		intent.putExtra("nfcPlcNm", nfcPlcNm);
		startActivity(intent);
		//startCheckList();

	}
	private String nfcPlcNm = "";

//	private void startCheckList(){
//		 nfcPosition = null;
//			for (int i = 0; i < workStatusData.size(); i++) {
//				if (nfcPlc.equals(workStatusData.get(i).getNFC_PLC())) {
//					nfcPosition = workStatusData.get(i);
//
//					break;
//				}
//			}
//
//			if (nfcPosition != null) {
//				nfcPlcNm = 	nfcPosition.getNFC_PLC_NM();
//				if(nfcPosition.getJOB_ST().equals("39")){
//					SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
//							nfcPosition.getNFC_PLC_NM() + " 점검이 완료되었습니다. 다시 점검 하시겠습니까?",
//							new btnClickListener() {
//
//								@Override
//								public void onButtonClick() {
//									// TODO Auto-generated method stub
//									Intent intent = new Intent(
//											CL_RI00_R01.this, CL_RI00_R02.class);
//									intent.putExtra("nfcPlc", nfcPlc);
//									intent.putExtra("jobNo", jobNo);
//									intent.putExtra("workDt", workDt);
//									intent.putExtra("nfcPlcNm", nfcPlcNm);
//									startActivity(intent);
//
//
//									//new StartCheckListAsync().execute();
//
//
//								}
//							});
//					ynDialog.show();
//				}else{
//
//					GPSService gs = new GPSService(context);
//
//					if(gs.isGetLocation()){
//						//GPS상태체크
//						//GPS가 켜져있지 않으면 작업을 시작할 수 없다.
//						CallService.startGPSService(context);
//						SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
//								nfcPosition.getNFC_PLC_NM() + " 점검을 시작하시겠습니까?",
//								new btnClickListener() {
//
//									@Override
//									public void onButtonClick() {
//										// TODO Auto-generated method stub
//										jobPartStart();
//
//										Intent intent = new Intent(
//												WO_WT00_R01.this, WO_WT00_R02.class);
//										intent.putExtra("nfcPlc", nfcPlc);
//										intent.putExtra("jobNo", jobNo);
//										intent.putExtra("workDt", workDt);
//										intent.putExtra("nfcPlcNm", nfcPlcNm);
//										startActivity(intent);
//									}
//								});
//						ynDialog.show();
//					}else{
//
//						new AlertDialog.Builder(context)
//								.setTitle("위치서비스 동의")
//								.setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
//								.setNeutralButton("이동",
//										new DialogInterface.OnClickListener() {
//											@Override
//											public void onClick(DialogInterface dialog,
//													int which) {
//												context.startActivity(new Intent(
//														android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//											}
//										}).show();
//					}
//				}
//			}else{
//				Toast.makeText(context, "현재 점검항목과 NFC태그가 일치하지 않습니다." , 2000).show();
//			}
//
//
//
//	}



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

	private void singleInspectorClick(){
		InspTyp = "1";
		radioGroupInspector.check(R.id.radioButtonSingleInsp);
		linear_wt_subInspector.setVisibility(View.GONE);
		tv_subInspCode.setText("");
		tv_subInspName.setText("");
		radioGroupInspector.setClickable(false);

	}
	private void doubleInspectorClick(){
		InspTyp = "2";
		radioGroupInspector.check(R.id.radioButtonDoubleInsp);
		linear_wt_subInspector.setVisibility(View.VISIBLE);
		radioGroupInspector.setClickable(false);
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
	String custSign;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_wt_jobComplete:
				if(custSign != null && !custSign.equals("")){
					CL_RI00_R01P wt01 = new CL_RI00_R01P(context,tv_wt_bldgInfo.getText().toString(),
							refControlNo,
							workDt,
							jobNo,
							"",custSign,activity);
					wt01.show();
					wt01.publicProgress();
				}else{
					SimpleDialog sm01 = new SimpleDialog(context, "알림","아직 고객승인이 안되었습니다.");
					sm01.show();
				}
				break;

			case R.id.btn_wt_sendData:
				sendMngData();
				break;
		}
	}
	private EasyJsonMap ejm03;
	private class SignAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... params) {

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ip/selectCustomerApproval.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId() ));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					custSign = "";
					ejm03 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
					custSign = ejm03.getValue("CUST_SIGN");
				} catch (JSONException e) {
					e.printStackTrace();
					custSign = "";
				}


			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}



	}


	private void sendMngData(){
		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, "점검결과를 승관원으로 전송 하시겠습니까?",
				new SimpleYesNoDialog.btnClickListener() {

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new CreateMngSendDataAsync().execute();
					}
				});
		ynDialog.show();
	}
	private class CreateMngSendDataAsync extends AsyncTask<Integer, Void, Void> {

		String rtn = "";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void  doInBackground(Integer... params) {
			// 1. bagicWorkTime

			//서버와 통신에서 데이터 받아오기 트라이캐치
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()+"ip/createMngSendData.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", workDt));
			arguments.add(new BasicNameValuePair("jobNo", jobNo));
			JSONObject returnJson = getHttp.getPost(param_url, arguments, false);

			try
			{
				ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );


				int jsonSize = returnJson.getJSONArray("dataList").length();
				if(jsonSize > 0)
				{
					rtn = ejl.getValue(0, "RTN");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			try{
				if("1".equals(rtn)){
					//결과전송 결과가 1이면 정상성공 마무리
					//Toast.makeText(context, "점검결과 전송이 완료되었습니다.", Toast.LENGTH_LONG);

					Intent intent = new Intent(activity, WO_WT00_R04.class);
					intent.putExtra("empId", commonSession.getEmpId());
					intent.putExtra("workDt",workDt);
					intent.putExtra("jobNo", jobNo);

					activity.startActivity(intent);
					finish();

				}else{
					AlertView.showAlert(rtn, context);

				}
			}
			catch(Exception ex)
			{
				AlertView.showAlert(rtn + ex.toString(), context);
			}
		}
	}
	//----------------------내비게이션 영역--------------------------------------//
}
