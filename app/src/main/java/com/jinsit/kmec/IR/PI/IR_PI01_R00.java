package com.jinsit.kmec.IR.PI;

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
import android.content.DialogInterface.OnDismissListener;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_PI01_R00 extends Activity implements OnClickListener,
		OnItemClickListener {

	private Context context;
	private TextView tv_pi_bldgNm;
	private TextView btn_pi_bldgNm;

	private TextView tv_pi_buildingNo;
	private TextView tv_pi_buildingName;
	private TextView tv_pi_departmentName;
	private TextView tv_pi_inqueryDate;
	private ListView lv_pi_partsClaimCondition;

	private TextView et_inqueryStartDate;
	private TextView btn_startCalendar;
	private TextView et_inqueryEndDate;
	private TextView btn_endCalendar;
	private TextView btn_inquery;

	// POP1
	private EasyJsonList ejl01;
	private EasyJsonList ejl02;
	private EasyJsonMap ej01;

	private List<IR_PI01_R00_Item> itemList00;
	private IR_PI01_R01P_Item01 item01;
	private List<IR_PI01_R01P_Item02> itemList02;

	private ProgressDialog progress;
	private ListAdapter adpater01;

	private DateUtil dateUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi01_r00);
		activityInit();
	}
	protected void activityInit() {

		getInstances();
		context = this;
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("부품청구현황");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		dateUtil = new DateUtil();
		currentSelectedListViewItem = null;
		this.setInqueryStartDate(dateUtil.getCurrentLongDate());
		this.setInqueryEndDate(dateUtil.getCurrentLongDate());
		itemList00 = new ArrayList<IR_PI01_R00_Item>();
		item01 = new IR_PI01_R01P_Item01();
		itemList02 = new ArrayList<IR_PI01_R01P_Item02>();
		this.bldgNo = "";
	}
	protected void getInstances() {

		tv_pi_bldgNm = (TextView) findViewById(R.id.tv_pi_bldgNm);
		btn_pi_bldgNm = (TextView) findViewById(R.id.btn_pi_bldgNm);

		et_inqueryStartDate = (TextView)findViewById(R.id.et_inqueryStartDate);
		et_inqueryEndDate = (TextView)findViewById(R.id.et_inqueryEndDate);
		btn_startCalendar = (TextView)findViewById(R.id.btn_startCalendar);
		btn_endCalendar = (TextView)findViewById(R.id.btn_endCalendar);
		btn_inquery= (TextView)findViewById(R.id.btn_inquery);

		tv_pi_buildingNo = (TextView) findViewById(R.id.tv_pi_buildingNo);
		tv_pi_buildingName = (TextView) findViewById(R.id.tv_pi_buildingName);
		tv_pi_departmentName = (TextView) findViewById(R.id.tv_pi_departmentName);
		tv_pi_inqueryDate = (TextView) findViewById(R.id.tv_pi_inqueryDate);
		lv_pi_partsClaimCondition = (ListView) findViewById(R.id.lv_pi_partsClaimCondition);
		setEvents();
	}
	protected void setEvents() {

		btn_pi_bldgNm.setOnClickListener(this);
		btn_startCalendar.setOnClickListener(this);
		btn_endCalendar.setOnClickListener(this);
		btn_inquery.setOnClickListener(this);
		lv_pi_partsClaimCondition.setOnItemClickListener(this);
		this.et_inqueryStartDate.setOnClickListener(this);
		this.et_inqueryEndDate.setOnClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_pi_bldgNm:
				searchBldg();
				break;
			case R.id.btn_inquery:
				if(this.bldgNo == null || this.bldgNo.equals("") ){
					AlertView.showAlert("건물명을 조회 해주세요", context);
					return;
				}
				this.setInqueryDate(this.inqueryStartDate, this.inqueryEndDate);
				progress(true);
				new selectPartRequest().execute("bagicWorkTime");
				break;
			case R.id.btn_startCalendar:
				getCalendar(0);
				break;
			case R.id.btn_endCalendar:
				getCalendar(1);
				break;
			case R.id.et_inqueryStartDate:
				getCalendar(0);
				break;
			case R.id.et_inqueryEndDate:
				getCalendar(1);
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (adpater01 != null && position > -1) {
			this.currentSelectedListViewItem = (IR_PI01_R00_Item) adpater01
					.getItem(position);
			progress(true);
			new selectDetailsOnRequest().execute("bagicWorkTime");
		} else {
			this.currentSelectedListViewItem = null;
		}

	}

	private void searchBldg() {
		final CM_SearchBldg eBldg = new CM_SearchBldg(this);
		eBldg.show();
		eBldg.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

				String no = eBldg.getBldgNo();
				if (no.equals("") || no == null) {
				} else {
					setBldgNo(eBldg.getCsDeptNm(), eBldg.getBldgNo(), eBldg.getBldgNm());
				}
			}

		});

	}

	public class selectPartRequest extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectPartRequest.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("bldgNo",
							IR_PI01_R00.this.bldgNo));
					arguments.add(new BasicNameValuePair("partsDtFr",
							IR_PI01_R00.this.inqueryStartDate));
					arguments.add(new BasicNameValuePair("partsDtTo",
							IR_PI01_R00.this.inqueryEndDate));

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
					itemList00.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList00.add(new IR_PI01_R00_Item(ejl01.getValue(i,
								"PARTS_DT"), ejl01.getValue(i, "PARTS_NO"),
								ejl01.getValue(i, "REP_ST")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList00.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				adpater01 = new IR_PI01_R00_Adapter(context,
						R.layout.ir_pi01_r00_adapter, itemList00);
				IR_PI01_R00.this.lv_pi_partsClaimCondition
						.setAdapter(adpater01);
			}
		}
	}// end of SelectData inner-class

	public class selectDetailsOnRequest extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectDetailsOnRequest.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("partsNo",
							IR_PI01_R00.this.currentSelectedListViewItem
									.getPartsNo()));
					arguments.add(new BasicNameValuePair("selTp", "1"));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					arguments.clear();
					arguments.add(new BasicNameValuePair("partsNo",
							IR_PI01_R00.this.currentSelectedListViewItem
									.getPartsNo()));
					arguments.add(new BasicNameValuePair("selTp", "2"));
					returnJson02 = http.getPost(param_url_01, arguments, true);

					try {
						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataList"));
						ejl01 = new EasyJsonList(
								returnJson02.getJSONArray("dataList"));
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
					itemList02.clear();
					item01 = null;
					item01 = new IR_PI01_R01P_Item01(ej01.getValue("PARTS_DT"),
							ej01.getValue("BLDG_NO"), ej01.getValue("BLDG_NM"),
							ej01.getValue("REF_CONTR_NO"),
							ej01.getValue("REP_ST"), ej01.getValue("PARTS_CD"),
							ej01.getValue("MAT_DUE_DT"),
							ej01.getValue("APPR_DT"), ej01.getValue("MAT_DT"),
							ej01.getValue("MAT_APPR_DT"));
					int jsonSize01 = returnJson02.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList02.add(new IR_PI01_R01P_Item02(ejl01.getValue(
								i, "S_NO"), ejl01.getValue(i, "CAR_NO"), ejl01
								.getValue(i, "ITEM_NM"), ejl01.getValue(i,
								"PL_QTY")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				IR_PI01_R01P p01 = new IR_PI01_R01P(context, item01, itemList02);
				progress(false);
				p01.show();
			}
		}
	}// end of SelectData inner-class

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_PI01_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_PI01_R00.this.progress.dismiss();
		}
	}

	private void getCalendar(int fromTo) {

		Intent intent = new Intent(IR_PI01_R00.this,
				com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
		startActivityForResult(intent, fromTo);
	}

	/**
	 * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
	 *
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					this.setInqueryStartDate(bundle.getString("dateSelected"));
					break;
				} else {
					break;
				}
			case 1:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					this.setInqueryEndDate(bundle.getString("dateSelected"));
					break;
				} else {
					break;
				}

		}

	}

	private IR_PI01_R00_Item currentSelectedListViewItem;

	private String inqueryStartDate;
	private void setInqueryStartDate(String inqueryStartDate) {
		this.et_inqueryStartDate.setText(inqueryStartDate);
		this.inqueryStartDate = inqueryStartDate;
	}

	private String inqueryEndDate;
	public void setInqueryEndDate(String inqueryEndDate) {
		this.et_inqueryEndDate.setText(inqueryEndDate);
		this.inqueryEndDate = inqueryEndDate;
	}

	private String bldgNo;
	private void setBldgNo(String departmentName, String bldgNo, String bldgNm){
		this.departmentName = departmentName;
		this.bldgName = bldgNm;
		this.bldgNo = bldgNo;
		this.tv_pi_bldgNm.setText(bldgNm);
		this.tv_pi_buildingNo.setText(bldgNo);
		this.tv_pi_buildingName.setText(bldgNm);
		this.tv_pi_departmentName.setText(departmentName);
	}
	private String bldgName;
	private String departmentName;

	private String inqueryDate;

	private void setInqueryDate(String startDate, String endDate) {
		String date = startDate + " ~ " + endDate;
		this.tv_pi_inqueryDate.setText(date);
		this.inqueryDate = date;
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
	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
}
