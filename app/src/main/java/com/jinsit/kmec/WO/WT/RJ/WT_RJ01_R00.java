package com.jinsit.kmec.WO.WT.RJ;

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
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class WT_RJ01_R00 extends Activity
						 implements OnClickListener{

	
	//uiInstances
	private Context context;
	private Activity activity;
	private TextView btn01_rj01_r00_showPhotos;
	private TextView btn01_rj01_r00_showDetailsAndList;
	private TextView btn01_rj01_r00_closeThisActivity;
	private TextView tv01_rj01_r00_bldgNo;
	private TextView tv02_rj01_r00_bldgNm;
	private TextView tv03_rj01_r00_carNo;
	private TextView tv04_rj01_r00_rsContrNm;
	private TextView tv05_rj01_r00_partNo;
	private TextView tv06_rj01_r00_refContrNo;
	private TextView tv07_rj01_r00_codeNm;
	
	//dto
	private Map<String, String> dbMap;
	private Map<String, String> nextPopMap;

	//http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;
	private ProgressDialog progress;
	private String custSign;
	String workNm,jobSt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_rj01_r00);
		getInstances();
	}
	
	private void getInstances(){
		context								= this;
		activity							= this;
		nextPopMap							= new HashMap<String, String>();
		dbMap								= new HashMap<String, String>();
		btn01_rj01_r00_showPhotos			= (TextView) findViewById(R.id.btn01_rj01_r00_showPhotos);
		btn01_rj01_r00_showDetailsAndList	= (TextView) findViewById(R.id.btn01_rj01_r00_showDetailsAndList);
		btn01_rj01_r00_closeThisActivity	= (TextView) findViewById(R.id.btn01_rj01_r00_closeThisActivity);		
		tv01_rj01_r00_bldgNo				= (TextView) findViewById(R.id.tv01_rj01_r00_bldgNo);
		tv02_rj01_r00_bldgNm				= (TextView) findViewById(R.id.tv02_rj01_r00_bldgNm);
		tv03_rj01_r00_carNo					= (TextView) findViewById(R.id.tv03_rj01_r00_carNo);
		tv04_rj01_r00_rsContrNm				= (TextView) findViewById(R.id.tv04_rj01_r00_rsContrNm);
		tv05_rj01_r00_partNo				= (TextView) findViewById(R.id.tv05_rj01_r00_partNo);
		tv06_rj01_r00_refContrNo			= (TextView) findViewById(R.id.tv06_rj01_r00_refContrNo);
		tv07_rj01_r00_codeNm				= (TextView) findViewById(R.id.tv07_rj01_r00_codeNm);
		setEvent();
	}
	
	private void setEvent(){
		btn01_rj01_r00_showPhotos.setOnClickListener(this);
		btn01_rj01_r00_showDetailsAndList.setOnClickListener(this);
		btn01_rj01_r00_closeThisActivity.setOnClickListener(this);
		setConfig();
	}
	
	private void setConfig(){
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업완료 확인서");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		CommonSession cs = new CommonSession(context);
		Intent i01 = getIntent();
		String workDt = i01.getStringExtra("workDt");
		String jobNo = i01.getStringExtra("jobNo");
		workNm = i01.getStringExtra("workNm");
		jobSt = i01.getStringExtra("jobSt");
		custSign = i01.getStringExtra("custSign");
		
		//final declared dbMap. 
		//It doesn't have no more than those keys in it.
		dbMap.put("selTp" ,"3");			
		dbMap.put("empId" ,cs.getEmpId());  nextPopMap.put("empId" , cs.getEmpId());
		dbMap.put("workDt",workDt);			nextPopMap.put("workDt", workDt);
		dbMap.put("jobNo" ,jobNo);			nextPopMap.put("jobNo" , jobNo);
		WT_RJ01_R00.this.progress = 
				  android.app.ProgressDialog.show(context, "알림","변경 중입니다...");
		new Database().execute("selectJobConfirmationSheet");
		ActivityAdmin.getInstance().addActivity(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn01_rj01_r00_showPhotos:
			CM_ReadPicture wo14 = new CM_ReadPicture(context, dbMap.get("jobNo").toString() ,dbMap.get("workDt").toString(),"2");
			wo14.show();
			wo14.inqueryImages();
			break;
		case R.id.btn01_rj01_r00_showDetailsAndList:
			WT_RJ01_R01P sd = new WT_RJ01_R01P(context, nextPopMap);
			sd.show();
			break;
		case R.id.btn01_rj01_r00_closeThisActivity:
			if(workNm.equals("수리공사")){
				//수리공사일 경우
				if(custSign != null && !custSign.equals("")){
					WT_RJ01_R03P wt01 = new WT_RJ01_R03P(context,tv01_rj01_r00_bldgNo.getText().toString(), 
							tv06_rj01_r00_refContrNo.getText().toString(),
							nextPopMap.get("workDt").toString(), 
							nextPopMap.get("jobNo").toString(),
						 	"",custSign,activity);
				wt01.show();
				}
				else{
				AlertView.confirm(context, "알림", "고객승인을 받으시겠습니까?", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						WT_RJ01_R03P wt01 = new WT_RJ01_R03P(context,tv01_rj01_r00_bldgNo.getText().toString(), 
									tv06_rj01_r00_refContrNo.getText().toString(),
									nextPopMap.get("workDt").toString(), 
									nextPopMap.get("jobNo").toString(),
								 	"",custSign, activity);
						wt01.show();
					}
				});
				}
			}else{
				//수리공사가 아닌경우
				if(jobSt.equals("39")){
					finish();
				}else{
					AlertView.confirm(context, "알림", "완료하시겠습니까?", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Bundle b = new Bundle();
							b.putBoolean("confirmCode", true);
							// Add the set of extended data to the intent and start it
							Intent intent = new Intent();
							intent.putExtras(b);
							setResult(activity.RESULT_OK, intent);
							finish();
						
						}
					});	
				}
				
				
			}
			
			
			
			break;
		default:
			Log.e("[개발자Msg]", "out of case");
			break;
		}
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
		
		if(div.equals("selectJobConfirmationSheet")){
			
			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"ip/"+div+".do";
			
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("selTp" , dbMap.get("selTp").toString()));
			arguments.add(new BasicNameValuePair("empId" , dbMap.get("empId").toString()));
			arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
			arguments.add(new BasicNameValuePair("jobNo" , dbMap.get("jobNo").toString()));
			
			returnJson = http.getPost(argUrl, arguments, true);
			
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setData(String div){
		if(div.equals("selectJobConfirmationSheet")){
		
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					tv01_rj01_r00_bldgNo.setText( ejm02.getValue("BLDG_NO") );
					tv02_rj01_r00_bldgNm.setText( ejm02.getValue("BLDG_NM") );
					tv03_rj01_r00_carNo.setText( ejm02.getValue("CAR_NO") );
					tv04_rj01_r00_rsContrNm.setText( ejm02.getValue("RS_CONTR_NM") );
					tv05_rj01_r00_partNo.setText (ejm02.getValue("PARTS_NO") );
					tv06_rj01_r00_refContrNo.setText( ejm02.getValue("REF_CONTR_NO") );
					tv07_rj01_r00_codeNm.setText( ejm02.getValue("CODE_NM") );
				}else if(isError){
					alert(ejm01.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			WT_RJ01_R00.this.progress.dismiss();
		}
	}
	
	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
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
		
};
