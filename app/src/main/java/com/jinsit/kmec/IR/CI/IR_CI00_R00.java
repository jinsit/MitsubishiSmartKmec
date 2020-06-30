package com.jinsit.kmec.IR.CI;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.CM_SearchBldg_Adapter;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_CI00_R00 extends Activity implements OnClickListener, OnItemClickListener {
	private Context context;

	private TextView btn_ci_bldgNm;
	private EditText et_ci_searchBox;

	///nfc
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	//////

	private EasyJsonMap ej01;
	private EasyJsonList ejl01;
	private ListView lv_ci_searchList;

	private ProgressDialog progress;
	private ArrayList<CM_SearchBldgInfo_ITEM01> itemList;
	private CommonSession commonSession;
	private CM_SearchBldg_Adapter adapter01;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci00_r00);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;
		commonSession = new CommonSession(context);

		// 타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("고객 정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		this.currentSelectedItem01 = null;
		itemList = new ArrayList<CM_SearchBldgInfo_ITEM01>();

		///nfc
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		ndefMsg = "";
		tagUid = "";
	}

	protected void getInstances() {
		btn_ci_bldgNm = (TextView) findViewById(R.id.btn_ci_bldgNm);
		et_ci_searchBox =  (EditText) findViewById(R.id.et_ci_searchBox);
		lv_ci_searchList = (ListView) findViewById(R.id.lv_ci_searchList);
		setEvents();
	}

	protected void setEvents() {
		btn_ci_bldgNm.setOnClickListener(this);
		lv_ci_searchList.setOnItemClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}

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
				new selectNfcInfo().execute("bagicWorkTime");
			}
		}

	}


	private void enableTagWriteMode() {
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
	}

	private void disableTagWriteMode() {
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_ci_bldgNm:
				searchBldg();

				break;
			default:
				break;
		}
	}



	private void goNextActivity() {
		Bundle extras = new Bundle();
		Intent intent = new Intent(context, IR_CI00_R00_F.class);
		extras.putSerializable("selectedItem", this.currentSelectedItem01);
		intent.putExtras(extras);
		startActivity(intent);
	}

	private void searchBldg() {
		if (et_ci_searchBox.getText().toString().equals("")) {
			AlertView.showAlert("검색어를 한 글자 이상 입력하셔야 합니다.", context);
		} else {
			progress(true);
			new searchBuildingName().execute();
		}

	}

	public class searchBuildingName extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				itemList.clear();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "cm/searchBuildingNames.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession
						.getEmpId()));
				arguments.add(new BasicNameValuePair("pBldgNm", et_ci_searchBox
						.getText().toString()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejl01 = new EasyJsonList(
							returnJson.getJSONArray("dataList"));
					itemList.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						itemList.add(new CM_SearchBldgInfo_ITEM01(ejl01
								.getValue(i, "BLDG_NO"), ejl01.getValue(i,
								"BLDG_NM"), ejl01.getValue(i, "ADDR"), ejl01
								.getValue(i, "CS_CD"), ejl01.getValue(i,
								"RUN_ST"), ejl01.getValue(i, "CLIENT_DEPT"),
								ejl01.getValue(i, "CLIENT_NM"), ejl01.getValue(
								i, "CLIENT_HP"), ejl01.getValue(i,
								"CLIENT_TEL"), ejl01.getValue(i,
								"CONTR_DT"), ejl01.getValue(i,
								"CONTR_DT_FR_TO"), ejl01.getValue(i,
								"FAULT_DT_FR_TO"), ejl01.getValue(i,
								"INS_DT_FR_TO"), ejl01.getValue(i,
								"EMP_NM_1"), ejl01.getValue(i,
								"EMP_1_HP"), ejl01.getValue(i,
								"EMP_NM_2"), ejl01.getValue(i,
								"EMP_2_HP"), ejl01.getValue(i,
								"CS_DETP_NM")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress(false);
			adapter01 = new CM_SearchBldg_Adapter(context,
					R.layout.cm_searchbldg_adapter, itemList);
			lv_ci_searchList.setAdapter(adapter01);
			KeyboardUtil.hideKeyboard(et_ci_searchBox, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);
		}
	}


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
						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
						bldgNo = ej01.getValue("BLDG_NO");

					} catch (JSONException e) {
						e.printStackTrace();
					}

					if(bldgNo.equals("")){
						return "NotRegister";
					}
					param_url_01 = WebServerInfo.getUrl()
							+ "cm/searchBuildingInDetail.do";
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("pBldgNo",bldgNo));
					returnJson01 = http.getPost(param_url_01, arguments, true);
					ej01 = new EasyJsonMap(
							returnJson01.getJSONObject("dataList"));

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
				currentSelectedItem01 = null;
				try {
					currentSelectedItem01 = new CM_SearchBldgInfo_ITEM01(ej01.getValue("BLDG_NO"),
							ej01.getValue( "BLDG_NM"),
							ej01.getValue("ADDR"),
							ej01.getValue("CS_CD"),
							ej01.getValue("RUN_ST"),
							ej01.getValue("CLIENT_DEPT"),
							ej01.getValue("CLIENT_NM"),
							ej01.getValue("CLIENT_HP"),
							ej01.getValue("CLIENT_TEL"),
							ej01.getValue("CONTR_DT"),
							ej01.getValue("CONTR_DT_FR_TO"),
							ej01.getValue("FAULT_DT_FR_TO"),
							ej01.getValue("INS_DT_FR_TO"),
							ej01.getValue("EMP_NM_1"),
							ej01.getValue("EMP_1_HP"),
							ej01.getValue("EMP_NM_2"),
							ej01.getValue("EMP_2_HP"),
							ej01.getValue("CS_DETP_NM"));

				} catch (JSONException e) {
					e.printStackTrace();
					currentSelectedItem01 = null;
				}
				progress(false);
				if (currentSelectedItem01 != null) {
					goNextActivity();
				}
			}
			else if(result.equals("NotRegister")){
				progress(false);
				AlertView.showAlert("등록된 태그가 아닙니다.", context);
			}
		}
	}// end of SelectData inner-class

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_CI00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_CI00_R00.this.progress.dismiss();
		}
	}

	public CM_SearchBldgInfo_ITEM01 currentSelectedItem01;


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			currentSelectedItem01 = itemList.get(position);
			goNextActivity();
		}
	}


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

}
