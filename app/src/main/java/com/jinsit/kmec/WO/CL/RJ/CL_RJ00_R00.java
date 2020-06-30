package com.jinsit.kmec.WO.CL.RJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_ReadPicture;
import com.jinsit.kmec.CM.SearchAdminInfo;
import com.jinsit.kmec.WO.WT.RJ.WT_RJ01_R00;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.StringUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class CL_RJ00_R00 extends Activity implements OnClickListener {
	//uiInstances
	private Context context;
	private EasyJsonMap ej01;
	private EasyJsonMap ejm03;

	private TextView tv01_rj_r00_date;
	private TextView tv02_rj_r00_workNm;
	private TextView tv03_rj_r00_carNo;
	private TextView tv04_rj_r00_addr;
	private TextView tv05_rj_r00_csDeptNm;
	private TextView tv06_rj_r00_majorEng_key;
	private TextView tv07_rj_r00_majorEngNm;
	private TextView tv08_rj_r00_majorEngPhone;
	private TextView tv09_rj_r00_minorEng_key;
	private TextView tv10_rj_r00_minorEngNm;
	private TextView tv11_rj_r00_minorEngPhone;
	private TextView tv12_rj_r00_orderNo_key;
	private TextView tv13_rj_r00_orderNo_value;
	private TextView tv14_rj_r00_status;
	private TextView tv15_rj_r00_timeToMove;
	private TextView tv17_rj_r00_timeToArrive;
	private TextView tv19_rj_r00_timeToComplete;
	private TextView tv20_rj_r00_beforeAfterPhoto;
	private TextView tv21_rj_r00_makeConfirmationSheet;
	private TextView btn01_rj_r00_adminInfo;
	private TextView btn02_rj_r00_register;
	private TextView tv15_rj_r00_csfr;

	//http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;

	//dto
	private Map<String, String> dbMap;
	private String engSt;
	private String engNm;

	//utils
	private ProgressDialog progress;
	private static final String READY 	   = "00";
	private static final String MOVING 	   = "11";
	private static final String ARRIVAL    = "31";
	private static final String COMPLETION = "39";


	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;


	private CommonSession commonSession;

	private String custSign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_rj00_r00);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업대상정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();

	}

	private void getInstances(){
		context								= this;
		dbMap								= new HashMap<String, String>();
		tv01_rj_r00_date					= (TextView) findViewById(R.id.tv01_rj_r00_date);
		tv02_rj_r00_workNm					= (TextView) findViewById(R.id.tv02_rj_r00_workNm);
		tv03_rj_r00_carNo					= (TextView) findViewById(R.id.tv03_rj_r00_carNo);
		tv04_rj_r00_addr					= (TextView) findViewById(R.id.tv04_rj_r00_addr);
		tv05_rj_r00_csDeptNm				= (TextView) findViewById(R.id.tv05_rj_r00_csDeptNm);
		tv06_rj_r00_majorEng_key			= (TextView) findViewById(R.id.tv06_rj_r00_majorEng_key);
		tv07_rj_r00_majorEngNm				= (TextView) findViewById(R.id.tv07_rj_r00_majorEngNm);
		tv08_rj_r00_majorEngPhone			= (TextView) findViewById(R.id.tv08_rj_r00_majorEngPhone);
		tv09_rj_r00_minorEng_key			= (TextView) findViewById(R.id.tv09_rj_r00_minorEng_key);
		tv10_rj_r00_minorEngNm				= (TextView) findViewById(R.id.tv10_rj_r00_minorEngNm);
		tv11_rj_r00_minorEngPhone			= (TextView) findViewById(R.id.tv11_rj_r00_minorEngPhone);
		tv12_rj_r00_orderNo_key				= (TextView) findViewById(R.id.tv12_rj_r00_orderNo_key);
		tv13_rj_r00_orderNo_value			= (TextView) findViewById(R.id.tv13_rj_r00_orderNo_value);
		tv14_rj_r00_status					= (TextView) findViewById(R.id.tv14_rj_r00_status);
		tv15_rj_r00_csfr                    = (TextView) findViewById(R.id.tv15_rj_r00_csfr);
		btn01_rj_r00_adminInfo				= (TextView) findViewById(R.id.btn01_rj_r00_adminInfo);
		tv15_rj_r00_timeToMove				= (TextView) findViewById(R.id.tv15_rj_r00_timeToMove);
		tv17_rj_r00_timeToArrive			= (TextView) findViewById(R.id.tv17_rj_r00_timeToArrive);
		tv19_rj_r00_timeToComplete			= (TextView) findViewById(R.id.tv19_rj_r00_timeToComplete);
		tv20_rj_r00_beforeAfterPhoto		= (TextView) findViewById(R.id.tv20_rj_r00_beforeAfterPhoto);
		tv21_rj_r00_makeConfirmationSheet	= (TextView) findViewById(R.id.tv21_rj_r00_makeConfirmationSheet);
		btn02_rj_r00_register				= (TextView) findViewById(R.id.btn02_rj_r00_register);
		btn02_rj_r00_register.setVisibility(View.GONE);
		setEvents();
	}

	private void setEvents(){

		btn01_rj_r00_adminInfo.setOnClickListener(this);
		tv20_rj_r00_beforeAfterPhoto.setOnClickListener(this);
		tv21_rj_r00_makeConfirmationSheet.setOnClickListener(this);
		setConfig();
	}
	private void setConfig(){
		dbMap.put("jobNo" , getIntent().getExtras().getString("jobNo") );
		dbMap.put("workDt", getIntent().getExtras().getString("workDt") );
		dbMap.put("bldgNo", "");
		dbMap.put("jobAct", "");

		commonSession = new CommonSession(this);
		setTitle("작업대상정보");
		CL_RJ00_R00.this.progress =
				android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		new Database().execute("selectDetailsForOverhaul");
		ActivityAdmin.getInstance().addActivity(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//관리자정보
			case R.id.btn01_rj_r00_adminInfo:
				showAdminInfo();
				break;

			//작업 전후 사진
			case R.id.tv20_rj_r00_beforeAfterPhoto:
				showBeforeAfterPhotos();
				break;
			case R.id.tv21_rj_r00_makeConfirmationSheet:
				makeConfirmationSheet();
				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}

	}




	private void showAdminInfo(){
		SearchAdminInfo sai = new SearchAdminInfo( context
				,dbMap.get("bldgNo").toString());
		sai.show();
	}

	private void showBeforeAfterPhotos(){
		CM_ReadPicture wo14 = new CM_ReadPicture(context, dbMap.get("jobNo").toString() ,dbMap.get("workDt").toString(),"2");
		wo14.show();
		wo14.inqueryImages();

	}

	private void makeConfirmationSheet(){


		Intent i01 =  new Intent(context, WT_RJ01_R00.class);
		try {
			i01.putExtra("workDt", ejm02.getValue("WORK_DT"));
			i01.putExtra("jobNo" , ejm02.getValue("JOB_NO"));
			i01.putExtra("workNm" , ejm02.getValue("WORK_NM"));
			i01.putExtra("jobSt" , engSt);
			i01.putExtra("custSign" , custSign);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("[개발자Msg]", "WT_RJ00_R00 \n makeConfirmationSheet() Occured");
		}
		startActivity(i01);


	}



	private class Database extends AsyncTask<String, Integer, String>{
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
	}
	private void crud(String div){

		if(div.equals("selectDetailsForOverhaul")){

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"ip/"+div+".do";

			CommonSession cs = new CommonSession(context);
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("csEmpId", cs.getEmpId() ));
			arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
			arguments.add(new BasicNameValuePair("jobNo", dbMap.get("jobNo") ));

			returnJson = http.getPost(argUrl, arguments, true);

			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}

			argUrl = WebServerInfo.getUrl()+"ip/selectCustomerApproval.do";
			arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("empId", cs.getEmpId() ));
			arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
			arguments.add(new BasicNameValuePair("jobNo", dbMap.get("jobNo") ));
			returnJson = http.getPost(argUrl, arguments, true);
			try {
				custSign = "";
				ejm03 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				custSign = ejm03.getValue("CUST_SIGN");
			} catch (JSONException e) {
				e.printStackTrace();
				custSign = "";
			}

		}

	}
	private void setData(String div){

		if(div.equals("selectDetailsForOverhaul")){

			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					tv01_rj_r00_date.setText(ejm02.getValue("WORK_DT"));
					tv02_rj_r00_workNm.setText(ejm02.getValue("WORK_NM"));
					tv03_rj_r00_carNo.setText(ejm02.getValue("CAR_NO"));
					tv04_rj_r00_addr.setText(ejm02.getValue("ADDR"));
					tv05_rj_r00_csDeptNm.setText(ejm02.getValue("CS_DETP_NM"));
					tv07_rj_r00_majorEngNm.setText(ejm02.getValue("MAIN_EMP_NM"));
					tv08_rj_r00_majorEngPhone.setText(ejm02.getValue("MAIN_EMP_PHONE"));
					tv10_rj_r00_minorEngNm.setText(ejm02.getValue("SUB_EMP_NM"));
					tv11_rj_r00_minorEngPhone.setText(ejm02.getValue("SUB_EMP_PHONE"));
					tv13_rj_r00_orderNo_value.setText(ejm02.getValue("PARTS_NO"));
					tv14_rj_r00_status.setText(ejm02.getValue("ST"));
					tv15_rj_r00_csfr.setText(ejm02.getValue("CS_FR"));
					engSt = ejm02.getValue("JOB_ST");
					engNm = ejm02.getValue("ST");
					dbMap.put( "bldgNo", ejm02.getValue("BLDG_NO") );
					dbMap.put( "refContrNo", ejm02.getValue("REF_CONTR_NO") );

					tv15_rj_r00_timeToMove.setText( ejm02.getValue("MOVE_TM") );
					tv17_rj_r00_timeToArrive.setText( ejm02.getValue("ARRIVE_TM") );
					tv19_rj_r00_timeToComplete.setText( ejm02.getValue("COMPLETE_TM") );


					if(tv02_rj_r00_workNm.getText().toString().equals("수리공사")){
						if(custSign != null && !custSign.equals("")){
							tv21_rj_r00_makeConfirmationSheet.setText(StringUtil.padLeft("작업완료 확인서 발행" , "등록") );
						}
						else{
							tv21_rj_r00_makeConfirmationSheet.setText("작업완료 확인서 발행");
						}
					}
					else{
						tv21_rj_r00_makeConfirmationSheet.setText(StringUtil.padLeft("작업완료 확인서 발행" , "등록") );
					}

				}else if(isError){
					showAlert(ejm01.getValue("errMsg"));

				}
			} catch (Exception e) {
				e.printStackTrace();
			}CL_RJ00_R00.this.progress.dismiss();

		}else if(div.equals("setEngineerStatus")){
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					new Database().execute("selectDetailsForOverhaul");
				}else if(isError){
					showAlert("<Procedure Error>\n"+ejm01.getValue("errMsg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}CL_RJ00_R00.this.progress.dismiss();
		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CL_RJ00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");

		} else {
			CL_RJ00_R00.this.progress.dismiss();
		}
	}

	public void showAlert(String message)
	{
		AlertView.showAlert(message, context,new android.content.DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});
	}
	public void showAlertYn(String message, android.content.DialogInterface.OnClickListener ocl){
		AlertView.confirmYN(context, "알림", message, ocl , new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}});
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