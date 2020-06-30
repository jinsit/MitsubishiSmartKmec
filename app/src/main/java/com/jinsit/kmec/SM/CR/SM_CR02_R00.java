package com.jinsit.kmec.SM.CR;

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
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchEmp;
import com.jinsit.kmec.CM.SearchDept;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;


public class SM_CR02_R00 extends Activity
		implements  OnClickListener
		,OnItemClickListener{


	//uiInstances
	Context context;
	private TextView tv01_cr02_dept;
	private TextView tv03_cr02_dept;
	private TextView tv02_cr02_register;
	private TextView tv04_cr02_register;
	private TextView tv05_cr02_sch;

	private ListView lv01_cr02_dataList;

	//http
	private Map<String,String> argMap;
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;
	private List<SM_CR02_R00_Item01> itemList01;
	private ListAdapter adapter01;

	//utils
	private ProgressDialog progress;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sm_cr02_r00);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("Claim 조회");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();
	}


	private void getInstances(){
		context 					= this;
		itemList01 					= new ArrayList<SM_CR02_R00_Item01>();
		argMap						= new HashMap<String, String>();
		tv01_cr02_dept		 		= (TextView) findViewById(R.id.tv01_cr02_dept);
		tv03_cr02_dept				= (TextView)findViewById(R.id.tv03_cr02_dept);
		tv02_cr02_register	 		= (TextView) findViewById(R.id.tv02_cr02_register);
		tv04_cr02_register 		= (TextView)findViewById(R.id.tv04_cr02_register);
		tv05_cr02_sch		 		= (TextView) findViewById(R.id.tv05_cr02_sch);
		lv01_cr02_dataList			= (ListView) findViewById(R.id.lv01_cr02_dataList);
		setEvents();
	}
	private void setEvents(){
		tv03_cr02_dept.setOnClickListener(this);
		tv04_cr02_register.setOnClickListener(this);
		tv05_cr02_sch.setOnClickListener(this);
		lv01_cr02_dataList.setOnItemClickListener(this);
		setConfig();
	}
	private void setConfig(){

		//finally declared keys
		argMap.put("csDeptCd","");
		argMap.put("csEmpId","");
		ActivityAdmin.getInstance().addActivity(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv03_cr02_dept:
				popDept();
				break;
			case R.id.tv04_cr02_register:
				popEmpList();
				break;
			case R.id.tv05_cr02_sch:
			/*
			if( StringUtils.hasBlank(tv01_cr02_dept)
					|| tv01_cr02_dept.getText().toString().equals("부서를 조회하세요.") ){
				alert("부서를 조회하세요", context);
			}else if( StringUtils.hasBlank(tv02_cr02_register)
					|| tv02_cr02_register.getText().toString().equals("등록자를 조회하세요.")  ){
				alert("등록자를 조회하세요", context);
			}else{
				SM_CR02_R00.this.progress =
						android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
				new Database().execute("selectClaimList");
			}
			*/
				String dept = tv01_cr02_dept.getText().toString();
				String regi = tv02_cr02_register.getText().toString();
				boolean isDefaultDeptValue = dept.equals("부서를 조회하세요.") ? true : false;
				boolean isDefaultRegiValue = regi.equals("등록자를 조회하세요.") ? true : false;

				if(isDefaultDeptValue && isDefaultRegiValue){
					AlertView.alert(context, "알림", "부서 또는 등록자를 조회하세요.", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							lv01_cr02_dataList.setAdapter(null);
						}
					});
				}else{
					SM_CR02_R00.this.progress =
							android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
					new Database().execute("selectClaimList");
				}

				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}
	}


	//부서조회창를 Pop시킵니다.
	private void popDept(){
		final SearchDept sd01 = new SearchDept(context);
		sd01.show();
		sd01.deptSearch();
		sd01.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(sd01.getDeptNm().equals("")){
					tv01_cr02_dept.setText("부서를 조회하세요.");
					argMap.put("csDeptCd", "");
				}else{
					tv01_cr02_dept.setText(sd01.getDeptNm());
					argMap.put("csDeptCd", sd01.getDeptCd());
				}

			}
		});
	}
	//등록자조회창을 Pop시킵니다.
	private void popEmpList(){
		Map<String, String> argMap_ = new HashMap<String, String>();
		argMap_.put("svcTp", "2");
		final CM_SearchEmp sd02 = new CM_SearchEmp(context,argMap_);
		sd02.show();
		sd02.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		sd02.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		sd02.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				Map sd02Map = sd02.getDataMap();
				if(sd02Map.size() > 0){
					tv02_cr02_register.setText(sd02Map.get("empNm").toString());
					argMap.put("csEmpId", sd02Map.get("csEmpId").toString());

				}else{
					tv02_cr02_register.setText("등록자를 조회하세요.");
					argMap.put("csEmpId", "");
				}
			}
		});
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

		GetHttp http = null;
		String argUrl = "";

		if(div.equals("selectClaimList")){
			http = new GetHttp();
			argUrl = WebServerInfo.getUrl()+"sm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.clear();
			System.out.println("argMap "+argMap);
			arguments.add(new BasicNameValuePair("csDeptCd", argMap.get("csDeptCd").toString() ));
			arguments.add(new BasicNameValuePair("csEmpId" , argMap.get("csEmpId").toString() ));

			returnJson = http.getPost(argUrl, arguments, true);

			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejl01 = new EasyJsonList( returnJson.getJSONArray("dataList") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}


	private void setData(String div){

		if(div.equals("selectClaimList")){

			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){

					itemList01.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for(int i=0; i<jsonSize; i++){
						itemList01.add(new SM_CR02_R00_Item01(  ejl01.getValue(i, "NG_NO")
										, ejl01.getValue(i, "PROJECT_NO")
										, ejl01.getValue(i, "PROJECT_NM")
										, ejl01.getValue(i, "EMP_ID")
										, ejl01.getValue(i, "EMP_NM")
								)
						);
					}

				}else if(isError){
					alert(ejm01.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			adapter01 = new SM_CR02_R00_Adapter01(context, R.layout.sm_cr02_r00_adapter01, itemList01);
			lv01_cr02_dataList.setAdapter(adapter01);
			SM_CR02_R00.this.progress.dismiss();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		try {
			argMap.put("ngNo", ejl01.getValue(index, "NG_NO") );
			argMap.put("ngSr", ejl01.getValue(index, "NG_SR") );
		} catch (Exception e) {
			Log.e("[개발자Msg]", "onItemClick exception");
		}
		new SM_CR02_R01P(context, argMap).show();
	}


	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			SM_CR02_R00.this.progress =
					android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		}else{
			SM_CR02_R00.this.progress.dismiss();
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
	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
	//----------------------내비게이션 영역--------------------------------------//


};
