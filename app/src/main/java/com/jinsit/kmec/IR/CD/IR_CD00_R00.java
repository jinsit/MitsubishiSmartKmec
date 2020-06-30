package com.jinsit.kmec.IR.CD;

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
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SearchDept;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_CD00_R00 extends Activity implements OnClickListener,OnItemClickListener
{
	GetHttp getHttp;
	private ProgressDialog ProgressDialog;
	Context context;
	ArrayList<IR_CD02_R00_ITEM01> rfResDataLow;
	ArrayList<IR_CD02_R00_ITEM00> rfResData;
	ArrayList<IR_CD01_R00_ITEM> plResData;
	ArrayList<IR_CD03_R00_ITEM> asResData;
	IR_CD01_R00_Adapter plListAdapter;
	IR_CD02_R00_Adapter01 rfListAdapter;
	IR_CD03_R00_Adapter asListAdapter;
	IR_CD00_R00ReqData reqData;

	private EasyJsonList ejl;
	//---Widget---///
	LinearLayout ll_adminDept, ll_failure, ll_searchDate;

	ImageView iv_cbsStartTime,iv_cbsEndTime;
	TextView tv_cbsDept;
	EditText et_cbsRepeatFailure;
	ListView lv_cbsData;


	private TextView et_inqueryStartDate;
	private TextView btn_startCalendar;
	private TextView et_inqueryEndDate;
	private TextView btn_endCalendar;

	private TextView btn_inquery;
	private TextView tv_menutitle;
	private TextView btn_menu;
	private ImageView iv_asInquery, iv_cbsDept;



	//---widget---///

	//---variable---///
	String deptCode = "";  //부서코드:params(selCD)
	String deptNm = "";  //부서명:params
	//String recevDtFr ="";  //fromTime: params(recevDtFr)
	//String recevDtTo ="";  //toTime: params(recevDtTo)

	String countSt="";
	String rCount="";
	String csCarNo="";
	String csBldgNo="";
	private static final int PL=0,RF=1,AS=2;
	int searchStatus =18;
	private static final int CALENDAR_FROM = 1;
	private static final int CALENDAR_TO = 2;
	private static final String ORDER_CD ="01";//고장유형: 고장 fix
	private static final String SEL_TP = "1"; //조회구분: 점검부서
	private static final String CHK_ORDER ="2";//고장대상: 전체
	private static final String AS_TP = "0"; //조회구분: 점검부서

	//---variable---///
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_cd00_r00);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("CBS DATA");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		activityInit();

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
	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		ll_adminDept = (LinearLayout) findViewById(R.id.ll_adminDept);
		ll_failure = (LinearLayout) findViewById(R.id.ll_failure);
		ll_searchDate = (LinearLayout)findViewById(R.id.ll_searchDate);

		tv_cbsDept = (TextView) findViewById(R.id.tv_cbsDept);
		tv_cbsDept.setOnClickListener(this);
		iv_cbsDept = (ImageView)findViewById(R.id.iv_cbsDept);
		iv_cbsDept.setOnClickListener(this);

		et_cbsRepeatFailure = (EditText) findViewById(R.id.et_cbsRepeatFailure);
		lv_cbsData = (ListView) findViewById(R.id.lv_cbsData);
		lv_cbsData.setOnItemClickListener(this);


		this.iv_asInquery = (ImageView)findViewById(R.id.iv_asInquery);
		this.iv_asInquery.setOnClickListener(this);
		this.et_inqueryStartDate = (TextView)findViewById(R.id.et_inqueryStartDate);
		this.btn_startCalendar = (TextView)findViewById(R.id.btn_startCalendar);
		this.et_inqueryEndDate = (TextView)findViewById(R.id.et_inqueryEndDate);
		this.btn_endCalendar = (TextView)findViewById(R.id.btn_endCalendar);
		this.btn_inquery = (TextView)findViewById(R.id.btn_inquery);
		this.btn_menu = (TextView)findViewById(R.id.btn_menu);
		this.tv_menutitle = (TextView)findViewById(R.id.tv_menutitle);

		this.btn_startCalendar.setOnClickListener(this);
		this.btn_endCalendar.setOnClickListener(this);
		this.btn_inquery.setOnClickListener(this);
		this.btn_menu.setOnClickListener(this);
		this.et_inqueryStartDate.setOnClickListener(this);
		this.et_inqueryEndDate.setOnClickListener(this);

		this.et_inqueryStartDate.setOnClickListener(this);
		this.et_inqueryEndDate.setOnClickListener(this);

		ll_adminDept.setVisibility(View.VISIBLE);
		ll_failure.setVisibility(View.GONE);
		ll_searchDate.setVisibility(View.VISIBLE);
		et_inqueryStartDate.setText(DateUtil.nowDate());
		et_inqueryEndDate.setText(DateUtil.nowDate());

		setConfig();
	}
	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.tv_cbsDept:
				//searchDept();
				break;
			case R.id.iv_cbsDept:
				searchDept();
				break;

			case R.id.btn_menu:
				navigationPopUp(v, R.menu.navipop_ir_cbs);
				break;
			case R.id.btn_startCalendar:
				getCalendar(CALENDAR_FROM);
				break;
			case R.id.btn_endCalendar:
				getCalendar(CALENDAR_TO);
				break;
			case R.id.btn_inquery:
				selectCbsData();
				break;
			case R.id.iv_asInquery:
				selectCbsData();
				break;
			case R.id.et_inqueryStartDate:
				getCalendar(CALENDAR_FROM);
				break;
			case R.id.et_inqueryEndDate:
				getCalendar(CALENDAR_TO);
				break;
			default:
				break;
		}
	};

	private void selectCbsData() {
		// TODO Auto-generated method stub
		switch(searchStatus){
			case PL:

				if (deptCode.equals("") ||getInqueryStartDate().equals("")||getInqueryEndDate().equals("")) {
					Toast.makeText(context, "모든정보를 입력해주세요.", Toast.LENGTH_SHORT)
							.show();
				} else {
					new PassengerLockAsync().execute();

				}
				break;
			case RF:

				if (deptCode.equals("") ||getInqueryStartDate().equals("")||getInqueryEndDate().equals("")||getRcount().equals("")) {
					Toast.makeText(context, "모든정보를 입력해주세요.", Toast.LENGTH_SHORT)
							.show();
				} else {
					new RepeatedFailureLowAsync().execute();

				}

				break;
			case AS:
				if (deptCode.equals("")) {
					Toast.makeText(context, "모든정보를 입력해주세요.", Toast.LENGTH_SHORT)
							.show();
				} else {
					new AsIncompleteAsync().execute();

				}

				break;

			case 18:
				SimpleDialog sm01 = new SimpleDialog(context, "알림","조회구분을 선택해 주세요.");
				sm01.show();

				break;

		}
	}

	private String getRcount(){
		return 	et_cbsRepeatFailure.getText().toString();
	}
	private void searchDept() {
		final SearchDept searchDept = new SearchDept(context);
		searchDept.show();
		searchDept.deptSearch();
		searchDept.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				deptNm = searchDept.getDeptNm();
				if(deptNm.equals("")){
					tv_cbsDept.setText("부서를 조회해 주세요.");
					deptCode = searchDept.getDeptCd();
				}else{
					tv_cbsDept.setText(deptNm);
					deptCode = searchDept.getDeptCd();
				}
			}

		});

	}

	private void navigationPopUp(View v, int menu) {
		final PopupMenu pop = new PopupMenu(context, v);

		pop.getMenuInflater().inflate(menu, pop.getMenu());
		pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
					case R.id.menu_ir_cbs_PL:

						ll_adminDept.setVisibility(View.VISIBLE);
						ll_failure.setVisibility(View.GONE);
						ll_searchDate.setVisibility(View.VISIBLE);


						tv_menutitle.setText(item.getTitle());
						iv_asInquery.setVisibility(View.GONE);
						searchStatus=PL;
						listViewInit();

						break;
					case R.id.menu_ir_cbs_RF:

						ll_adminDept.setVisibility(View.VISIBLE);
						ll_failure.setVisibility(View.VISIBLE);
						ll_searchDate.setVisibility(View.VISIBLE);


						tv_menutitle.setText(item.getTitle());
						iv_asInquery.setVisibility(View.GONE);
						searchStatus=RF;
						listViewInit();
						selectRfDetail();

						break;
					case R.id.menu_ir_cbs_AS:
						ll_adminDept.setVisibility(View.VISIBLE);
						ll_failure.setVisibility(View.GONE);
						ll_searchDate.setVisibility(View.GONE);

						tv_menutitle.setText(item.getTitle());
						searchStatus=AS;
						iv_asInquery.setVisibility(View.VISIBLE);
						listViewInit();
						break;

				}
				return false;
			}
		});
		pop.show();
	}

	private void selectRfDetail() {
		final String rfItem[] = { "부위반복고장", "호기반복고장" };
		new AlertDialog.Builder(context).setTitle("다발고장 유형선택")
				.setCancelable(false)
				.setItems(rfItem, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								countSt="3";
								tv_menutitle.append(" (" + rfItem[which] + ")");
								break;
							case 1:
								countSt="2";
								tv_menutitle.append(" (" + rfItem[which] + ")");
								break;
						}

					}
				}).show();
	}

	public class PassengerLockAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_CD00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "조회 중 입니다...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				plResData = new ArrayList<IR_CD01_R00_ITEM>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cd/selectPassengerLock.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", SEL_TP));
				arguments.add(new BasicNameValuePair("selCd", deptCode));
				arguments.add(new BasicNameValuePair("recevDtFr", getInqueryStartDate()));
				arguments.add(new BasicNameValuePair("recevDtTo", getInqueryEndDate()));
				arguments.add(new BasicNameValuePair("countSt", ORDER_CD));
				arguments.add(new BasicNameValuePair("rCount", CHK_ORDER));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					plResData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						plResData.add(new IR_CD01_R00_ITEM(ejl.getValue(i,
								"RECEV_TM"), ejl.getValue(i, "BLDG_NO"), ejl
								.getValue(i, "BLDG_NM"), ejl.getValue(i,
								"CAR_NO"), ejl.getValue(i, "CAR_CD"),ejl.getValue(i, "DONG_CAR_NO"), ejl
								.getValue(i, "MODEL_NM"), ejl.getValue(i,
								"CS_DEPT_NM"), ejl.getValue(i, "EMP_NM_1"),ejl.getValue(i, "EMP_1_HP")
								,ejl.getValue(i, "EMP_NM_2"),
								ejl.getValue(i, "EMP_2_HP"), ejl.getValue(i,
								"ARRIVE_TM"), ejl.getValue(i, "COMPLETE_TM"),
								ejl.getValue(i, "RESCUE_TM"), ejl.getValue(i,
								"EX_ORDER_CD"), ejl.getValue(i,
								"STATUS_CD"), ejl.getValue(i,
								"CBS_CD_1"), ejl
								.getValue(i, "CBS_CD_2"), ejl.getValue(
								i, "CBS_CD_3"), ejl.getValue(i,
								"FAULT_CD"),
								ejl.getValue(i, "PROC_CD"), ejl.getValue(i,
								"DUTY_CD"), ejl.getValue(i,
								"ORDER_DESC"),ejl.getValue(i, "10")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 조회이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			IR_CD00_R00.this.ProgressDialog.dismiss();
			plListAdapter = new IR_CD01_R00_Adapter(context, plResData);
			lv_cbsData.setAdapter(plListAdapter);

		}
	}


	public class RepeatedFailureAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_CD00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "조회중 입니다...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				rfResData = new ArrayList<IR_CD02_R00_ITEM00>();

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cd/selectRepeatedFailure.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", reqData.getSelTp()));
				arguments.add(new BasicNameValuePair("selCd", reqData.getSelCd()));
				arguments.add(new BasicNameValuePair("recevDtFr", reqData.getRecevDtFr()));
				arguments.add(new BasicNameValuePair("recevDtTo", reqData.getRecevDtTo()));
				arguments.add(new BasicNameValuePair("countSt", reqData.getCountSt()));
				arguments.add(new BasicNameValuePair("rCount", reqData.getrCount()));
				arguments.add(new BasicNameValuePair("csBldgNo", reqData.getCsBldgNo()));
				arguments.add(new BasicNameValuePair("csCarNo", reqData.getCsCarNo()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					rfResData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						rfResData.add(new IR_CD02_R00_ITEM00(ejl.getValue(i,
								"BLDG_NO"), ejl.getValue(i, "BLDG_NM"), ejl
								.getValue(i, "CAR_NO"), ejl.getValue(i,
								"DONG_CAR_NO"), ejl.getValue(i, "CAR_CD"), ejl
								.getValue(i, "MODEL_NM"), ejl.getValue(i,
								"RECEV_TM"), ejl.getValue(i, "ARRIVE_TM"), ejl
								.getValue(i, "COMPLETE_TM"), ejl.getValue(i,
								"RESERV_TM"), ejl.getValue(i, "CONTACT_CD"),
								ejl.getValue(i, "RECEV_DESC"), ejl.getValue(i,
								"STATUS_CD"), ejl.getValue(i,
								"CBS_CD_1"), ejl
								.getValue(i, "CBS_CD_2"), ejl.getValue(
								i, "CBS_CD_3"), ejl.getValue(i,
								"FAULT_CD"),
								ejl.getValue(i, "PROC_CD"), ejl.getValue(i,
								"DUTY_CD"), ejl.getValue(i,
								"RECEV_NO")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 조회이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			IR_CD00_R00.this.ProgressDialog.dismiss();
			IR_CD02_R00P cd02p = new IR_CD02_R00P(context, reqData, rfResData);
			cd02p.show();

		}
	}

	public class RepeatedFailureLowAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_CD00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "조회 중 입니다...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				rfResDataLow = new ArrayList<IR_CD02_R00_ITEM01>();

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cd/selectRepeatedFailure.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", SEL_TP));
				arguments.add(new BasicNameValuePair("selCd", deptCode));
				arguments.add(new BasicNameValuePair("recevDtFr", getInqueryStartDate()));
				arguments.add(new BasicNameValuePair("recevDtTo", getInqueryEndDate()));
				arguments.add(new BasicNameValuePair("countSt", countSt));
				arguments.add(new BasicNameValuePair("rCount", getRcount()));
				arguments.add(new BasicNameValuePair("csBldgNo", csBldgNo));
				arguments.add(new BasicNameValuePair("csCarNo", csCarNo));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					rfResDataLow.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						rfResDataLow.add(new IR_CD02_R00_ITEM01(ejl.getValue(i,
								"BLDG_NO"), ejl.getValue(i, "BLDG_NM"), ejl
								.getValue(i, "CAR_NO"), ejl.getValue(i,
								"DONG_CAR_NO"), ejl.getValue(i, "CAR_CD"), ejl
								.getValue(i, "MODEL_NM"), ejl.getValue(i,
								"RST_CNT")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 조회이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			IR_CD00_R00.this.ProgressDialog.dismiss();
			rfListAdapter = new IR_CD02_R00_Adapter01(context, rfResDataLow);
			lv_cbsData.setAdapter(rfListAdapter);

		}
	}
	public class AsIncompleteAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_CD00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "조회 중 입니다...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				asResData = new ArrayList<IR_CD03_R00_ITEM>();

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cd/selectAsIncomplete.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", AS_TP));
				arguments.add(new BasicNameValuePair("selCd", deptCode));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					asResData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						asResData.add(new IR_CD03_R00_ITEM(ejl.getValue(i,
								"RECEV_TM"), ejl.getValue(i, "ORDER_NM"), ejl
								.getValue(i, "BLDG_NO"), ejl.getValue(i,
								"BLDG_NM"), ejl.getValue(i, "CAR_NO"), ejl
								.getValue(i, "DONG_CAR_NO"), ejl.getValue(i,
								"CAR_CD"), ejl.getValue(i, "MODEL_NM"), ejl
								.getValue(i, "RECEV_DESC"), ejl.getValue(i,
								"MOVE_TM"), ejl.getValue(i, "ARRIVE_TM"),
								ejl.getValue(i, "RESERV_TM"), ejl.getValue(i,
								"CS_EMP_NM"), ejl.getValue(i,
								"PHONE_1")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 조회이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			IR_CD00_R00.this.ProgressDialog.dismiss();
			asListAdapter = new IR_CD03_R00_Adapter(context, asResData);
			lv_cbsData.setAdapter(asListAdapter);

		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		if(parent.getAdapter()== plListAdapter){
			Log.e("ParentApdater", parent.getId() + "plListAdapter");
			IR_CD01_R00_ITEM mData = plResData.get(position);
			IR_CD01_R00P cd01 = new IR_CD01_R00P(context, mData);
			cd01.show();

		}else if(parent.getAdapter()==rfListAdapter){

			String bdlg =  rfResDataLow.get(position).getBLDG_NO();
			String car =  rfResDataLow.get(position).getCAR_NO();
			int intCountSt = Integer.valueOf(countSt)+2;
			String syntax = String.valueOf(intCountSt);
			reqData =
					new IR_CD00_R00ReqData(SEL_TP,deptCode,getInqueryStartDate(),getInqueryEndDate(),syntax,getRcount(),bdlg,car);
			new RepeatedFailureAsync().execute();

		}else if(parent.getAdapter()==asListAdapter){
			Log.e("ParentApdater", parent.getId() + "asListAdapter");
			IR_CD03_R00_ITEM mData = asResData.get(position);
			IR_CD03_R00P cd03 = new IR_CD03_R00P(context, mData);
			cd03.show();

		}
	}


	private void listViewInit(){
		lv_cbsData.setAdapter(null);
	}

	private void getCalendar(int fromTo) {

		Intent intent = new Intent(IR_CD00_R00.this,
				com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
		startActivityForResult(intent, fromTo);

	}

	/**
	 * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
	 *
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case CALENDAR_FROM:
				if (resultCode == RESULT_OK) {

					Bundle bundle = data.getExtras();
					this.setInqueryStartDate(bundle.getString("dateSelected"));
					break;

				}
			case CALENDAR_TO:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					this.setInqueryEndDate(bundle.getString("dateSelected"));
					break;

				}
		}

	}



	public String getInqueryStartDate() {
		return this.et_inqueryStartDate.getText().toString();
	}
	public void setInqueryStartDate(String inqueryStartDate) {
		this.et_inqueryStartDate.setText(inqueryStartDate);
	}

	public String getInqueryEndDate() {
		return this.et_inqueryEndDate.getText().toString();
	}

	public void setInqueryEndDate(String inqueryEndDate) {
		this.et_inqueryEndDate.setText(inqueryEndDate);
	}
}