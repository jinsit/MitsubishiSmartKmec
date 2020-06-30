package com.jinsit.kmec.IR.TI;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_TI01_R00 extends Activity implements OnItemClickListener {
	private Context context;

	private ListView lv_ti_divisionClass;

	private IR_TI00_R00_ITEM01 item01;
	private List<IR_TI01_R00_ITEM01> itemList01;
	private IR_TI01_R00_Adapter01 adapter01;

	private EasyJsonList ejl01;

	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ti01_r00);
		activityInit();
	}

	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;

		// 타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("기술 정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		Object selectedItem = (Object) this.getIntent().getExtras()
				.getSerializable("selectedItem");
		if (selectedItem.getClass().getName()
				.equals("com.jinsit.kmec.IR.TI.IR_TI00_R00_ITEM01")) {
			item01 = (IR_TI00_R00_ITEM01) selectedItem;
		} else {
			item01 = new IR_TI00_R00_ITEM01();
		}


		itemList01 = new ArrayList<IR_TI01_R00_ITEM01>();
		this.currentSelectedItem01 = null;
		inqueryDivisionClass();

	}

	protected void getInstances() {
		lv_ti_divisionClass = (ListView) findViewById(R.id.lv_ti_divisionClass);
		setEvents();
	}

	protected void setEvents() {
		lv_ti_divisionClass.setOnItemClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if(position > -1)
		{
			this.currentSelectedItem01 = itemList01.get(position);
			Bundle extras = new Bundle();
			Intent intent = new Intent(context, IR_TI02_R00.class);
			extras.putSerializable("selectedItem", this.currentSelectedItem01 );
			intent.putExtra("docuHNm", item01.getDocuHNm());
			intent.putExtras(extras);
			startActivity(intent);
		}
		else {
			this.currentSelectedItem01 = null;
		}
	}


	public class selectDivisionClass extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectDivisionClass.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("docuHCd",
							IR_TI01_R00.this.item01.getDocuHCd()));
					arguments.add(new BasicNameValuePair("docuText", ""));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
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

				try {
					itemList01.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList01.add(new IR_TI01_R00_ITEM01(ejl01.getValue(i,"DOCU_H_CD"),
								ejl01.getValue(i, "DOCU_M_CD"),
								ejl01.getValue(i, "DOCU_M_NM")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				IR_TI01_R00.this.progress.dismiss();
				adapter01 = new IR_TI01_R00_Adapter01(context,
						R.layout.ir_ti01_r00_adapter01, itemList01);
				IR_TI01_R00.this.lv_ti_divisionClass
						.setAdapter(adapter01);
			}
		}
	}// end of SelectData inner-class

	private void inqueryDivisionClass()
	{
		IR_TI01_R00.this.progress = android.app.ProgressDialog.show(
				context, "알림", "조회 중입니다.");
		new selectDivisionClass().execute("bagicWorkTime");
	}


	private IR_TI01_R00_ITEM01 currentSelectedItem01;

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
		}else{
			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
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

};