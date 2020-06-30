package com.jinsit.kmec.WO.CL.MW;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R02P;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R02_Adapter00;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R02_ITEM00;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM00;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM01;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
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
public class CL_MW00_R02 extends Activity implements  android.widget.AdapterView.OnItemClickListener{
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
	TextView tv_mw_r02bldgInfo,tv_mw_r02addr,tv_mw_r02date, tv_mw_r02workNm,  tv_mw_r02st, tv_mw_r02cs_fr,
			tv_mw_r02startTm ,tv_mw_moveTm, tv_mw_arriveTm,tv_mw_r02completeTm;
	Button  btn_mw_r02jobComplete;
	LinearLayout ll_mw_r02InputArea;

	TextView tv_mw_r02CheckDay1, tv_mw_r02FailInfo1,tv_mw_r02Rowid;
	TextView tv_mw_r02SuccessInfo1;
	ListView lv_mw_r02RegularInspection;
	WT_MW00_R02_Adapter00 wT_MW00_R02_Adapter00;
	private ProgressDialog ProgressDialog;
	private ProgressDialog progress;


	private String jobNo, empId, workDt, carNo,bldgNo;//
	private String successSt,detailRmk;
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
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cl_mw00_r02);
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
		ll_mw_r02InputArea = (LinearLayout)findViewById(R.id.ll_mw_r02InputArea);
		ll_mw_r02InputArea.setVisibility(View.GONE);
		tv_mw_r02bldgInfo = (TextView) findViewById(R.id.tv_mw_r02bldgInfo);
		tv_mw_r02addr = (TextView) findViewById(R.id.tv_mw_r02addr);
		tv_mw_r02date = (TextView) findViewById(R.id.tv_mw_r02date);
		tv_mw_r02workNm = (TextView) findViewById(R.id.tv_mw_r02workNm);
		tv_mw_r02st = (TextView) findViewById(R.id.tv_mw_r02st);
		tv_mw_r02cs_fr = (TextView) findViewById(R.id.tv_mw_r02cs_fr);

		tv_mw_moveTm = (TextView)findViewById(R.id.tv_mw_moveTm);
		tv_mw_arriveTm = (TextView)findViewById(R.id.tv_mw_arriveTm);
		//tv_mw_r02startTm = (TextView)findViewById(R.id.tv_mw_r02startTm);

		tv_mw_r02completeTm = (TextView) findViewById(R.id.tv_mw_r02completeTm);
		btn_mw_r02jobComplete = (Button) findViewById(R.id.btn_mw_r02jobComplete);
		btn_mw_r02jobComplete.setVisibility(View.GONE);


		tv_mw_r02Rowid=(TextView) findViewById(R.id.tv_mw_r02Rowid);
		tv_mw_r02CheckDay1=(TextView) findViewById(R.id.tv_mw_r02CheckDay1);
		tv_mw_r02CheckDay1.setText(workDt);
		tv_mw_r02SuccessInfo1=(TextView) findViewById(R.id.tv_mw_r02SuccessInfo1);

		tv_mw_r02FailInfo1=(TextView) findViewById(R.id.tv_mw_r02FailInfo1);


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
		tv_mw_moveTm.setText(workTargetData.getMOVE_TM());
		tv_mw_arriveTm.setText(workTargetData.getARRIVE_TM());
		//tv_mw_r02startTm.setText(workTargetData.getSTART_TM());
		tv_mw_r02completeTm.setText(workTargetData.getCOMPLETE_TM());
		empId = workTargetData.getCS_EMP_ID();
		workDt = workTargetData.getWORK_DT();
		refControlNo = workTargetData.getREF_CONTR_NO();
		carNo = workTargetData.getCAR_NO();
		bldgNo = workTargetData.getBLDG_NO();

		new RegularInspectionAsync().execute();
	}





	private void progress(Boolean isActivated) {
		if (isActivated) {
			CL_MW00_R02.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CL_MW00_R02.this.progress.dismiss();
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
			CL_MW00_R02.this.ProgressDialog.dismiss();
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
			CL_MW00_R02.this.ProgressDialog = android.app.ProgressDialog.show(
					CL_MW00_R02.this, "작업대상", "작업대상목록 불러오는중");
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
			CL_MW00_R02.this.ProgressDialog.dismiss();

			wT_MW00_R02_Adapter00 = new WT_MW00_R02_Adapter00(context,wT_MW00_R02_ITEM00);
			lv_mw_r02RegularInspection.setAdapter(wT_MW00_R02_Adapter00);

			listViewHeightSet(wT_MW00_R02_Adapter00, lv_mw_r02RegularInspection);
			int rowId = wT_MW00_R02_ITEM00.size() + 1;
			tv_mw_r02Rowid.setText(rowId + "차 검사일");

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			CL_MW00_R02.this.ProgressDialog = android.app.ProgressDialog.show(
					CL_MW00_R02.this, "정기검사 입회", "입회내역을 불러오는중입니다.");
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

}