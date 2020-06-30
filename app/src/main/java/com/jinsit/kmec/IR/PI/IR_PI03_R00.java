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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
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

public class IR_PI03_R00 extends Activity implements OnClickListener,
		OnItemClickListener {

	private Context context;

	private TextView btn_pi_inquery;

	private TextView tv_menutitle;
	private TextView btn_menu;

	private EditText et_pi_inqueryText;

	private ListView lv_pi_partsStockCondition;

	// POP1
	private EasyJsonMap ej01;
	private EasyJsonList ejl01;

	private List<IR_PI03_R00_Item> itemList01;
	private IR_PI03_R01P_Item01 item01;
	private List<IR_PI03_R01P_Item02> itemList02;


	private ProgressDialog progress;
	private ListAdapter adpater01;

	private LinearLayout ll_pi_logisSpinner;
	private Spinner sp_pi_logis;
	private String logisCd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi03_r00);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("부품재고현황");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		selTp = "0";
		tv_menutitle.setText("아이템 No");

		itemList01 = new ArrayList<IR_PI03_R00_Item>();
		item01 = new IR_PI03_R01P_Item01();
		itemList02 = new ArrayList<IR_PI03_R01P_Item02>();
		this.currentSelectedListViewItem = null;
	}

	protected void getInstances() {
		btn_pi_inquery = (TextView) findViewById(R.id.btn_pi_inquery);

		tv_menutitle = (TextView)findViewById(R.id.tv_menutitle);
		btn_menu = (TextView)findViewById(R.id.btn_menu);

		et_pi_inqueryText = (EditText) findViewById(R.id.et_pi_inqueryText);
		lv_pi_partsStockCondition = (ListView) findViewById(R.id.lv_pi_partsStockCondition);

		ll_pi_logisSpinner = (LinearLayout)findViewById(R.id.ll_pi_logisSpinner);
		ll_pi_logisSpinner.setVisibility(View.GONE);
		sp_pi_logis = (Spinner)findViewById(R.id.sp_pi_logis);

		setEvents();
	}

	protected void setEvents() {
		btn_pi_inquery.setOnClickListener(this);
		btn_menu.setOnClickListener(this);
		lv_pi_partsStockCondition.setOnItemClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
		new PartsRequestPopUpSearch().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_pi_inquery:

				if(selTp == null || selTp.equals("")){
					progress(true);
					new selectPartStockWithLogis().execute("bagicWorkTime");
				}else{
					if(et_pi_inqueryText.getText().length() < 1){
						AlertView.showAlert("검색어를 한글자 이상 입력하셔야 합니다.", context);
						return;
					}
					progress(true);
					new selectPartStock().execute("bagicWorkTime");
				}

				break;
			case R.id.btn_menu:
				popupMenu(v, R.menu.menu_ir_pi03);
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (adpater01 != null && position > -1) {
			lv_pi_partsStockCondition.setEnabled(false);
			this.currentSelectedListViewItem = (IR_PI03_R00_Item) adpater01
					.getItem(position);
			progress(true);
			new selectDetailsOnStock().execute("bagicWorkTime");
			Handler h = new Handler();
			h.postDelayed(new ClickHandler(),1000);
		} else {
			this.currentSelectedListViewItem = null;
		}
	}
	class ClickHandler implements Runnable{
		//중복클릭 방지용 핸들러
		@Override
		public void run() {
			// TODO Auto-generated method stub
			lv_pi_partsStockCondition.setEnabled(true);
		}

	}
	private void popupMenu(final View v, final int menu) {
		final PopupMenu pop = new PopupMenu(context, v);
		pop.getMenuInflater().inflate(menu, pop.getMenu());
		pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
					case R.id.menu_pi_itemNo:
						selTp = "0";
						ll_pi_logisSpinner.setVisibility(View.GONE);
						tv_menutitle.setText("아이템 No");
						break;
					case R.id.menu_pi_itemNm:
						selTp = "1";
						ll_pi_logisSpinner.setVisibility(View.GONE);
						tv_menutitle.setText("아이템 이름");
						break;
					case R.id.menu_pi_size:
						selTp = "2";
						ll_pi_logisSpinner.setVisibility(View.GONE);
						tv_menutitle.setText("규격");
						break;

					case R.id.menu_pi_logis:
						selTp ="";
						ll_pi_logisSpinner.setVisibility(View.VISIBLE);
						tv_menutitle.setText("창고별");
						break;

					default:
						break;

				}
				return false;
			}
		});
		pop.show();
	}



	public class selectPartStock extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectPartStock.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp",
							IR_PI03_R00.this.selTp));
					arguments.add(new BasicNameValuePair("selText",
							IR_PI03_R00.this.et_pi_inqueryText.getText()
									.toString()));

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
						itemList01.add(new IR_PI03_R00_Item(ejl01.getValue(i,
								"ITEM_NO"), ejl01.getValue(i, "ITEM_NM"), ejl01
								.getValue(i, "DRAW_NO")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList01.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				KeyboardUtil.hideKeyboard(et_pi_inqueryText, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);
				adpater01 = new IR_PI03_R00_Adapter(context,
						R.layout.ir_pi03_r00_adapter, itemList01, false);
				IR_PI03_R00.this.lv_pi_partsStockCondition
						.setAdapter(adpater01);
			}
		}
	}// end of SelectData inner-class

	public class selectDetailsOnStock extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectDetailsOnStock01.do";
					String param_url_02 = WebServerInfo.getUrl()
							+ "ir/selectDetailsOnStock02.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","1"));
					arguments.add(new BasicNameValuePair("itemNo",
							IR_PI03_R00.this.currentSelectedListViewItem.getItemNo()));
					arguments.add(new BasicNameValuePair("stockIf","0"));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","2"));
					arguments.add(new BasicNameValuePair("itemNo",
							IR_PI03_R00.this.currentSelectedListViewItem.getItemNo()));
					arguments.add(new BasicNameValuePair("stockIf","0"));
					returnJson02 = http.getPost(param_url_02, arguments, true);

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
					item01 = new IR_PI03_R01P_Item01(ej01.getValue("ITEM_NO"),
							ej01.getValue("ITEM_NM"), ej01.getValue("DRAW_NO"),
							ej01.getValue("QTY"));
					int jsonSize01 = returnJson02.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList02.add(new IR_PI03_R01P_Item02(ejl01.getValue(i, "STOCK_CD"),
								ejl01.getValue(i, "STOCK_NM"),
								ejl01.getValue(i, "ITEM_NO"),
								ejl01.getValue(i,"ITEM_NM"),
								ejl01.getValue(i,"DRAW_NO"),
								ejl01.getValue(i,"QTY" ),
								ejl01.getValue(i, "IRR_QTY"),
								ejl01.getValue(i, "LOCATION")));
					}
					IR_PI03_R01P p01 = new IR_PI03_R01P(context, item01, itemList02);
					progress(false);
					p01.show();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
			}
		}
	}// end of SelectData inner-class


	ArrayList<IR_PI06_R00_Item> iR_PI06_R00_Item;
	private EasyJsonList ejl;
	public class PartsRequestPopUpSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				iR_PI06_R00_Item = new ArrayList<IR_PI06_R00_Item>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ir/selectPartsRequestPopUp.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("bldgNo", ""));
				arguments.add(new BasicNameValuePair("refTp", ""));
				arguments.add(new BasicNameValuePair("tp", "W"));
				arguments.add(new BasicNameValuePair("userId", ""));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					iR_PI06_R00_Item.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						iR_PI06_R00_Item.add(new IR_PI06_R00_Item(
								ejl.getValue(i,"CD"),
								ejl.getValue(i, "NM"),
								ejl.getValue(i, "TP")));
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
			ArrayAdapter<IR_PI06_R00_Item> adapter = new ArrayAdapter<IR_PI06_R00_Item>(context, android.R.layout.simple_dropdown_item_1line, iR_PI06_R00_Item);
			sp_pi_logis.setAdapter(adapter);

			sp_pi_logis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
					//successSt = tv_mw_r02SuccessInfo1.getItemAtPosition(position).toString();
					logisCd = iR_PI06_R00_Item.get(position).getCD();
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});

		}
	}


	public class selectPartStockWithLogis extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectPartStockWithLogis.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("whCd",
							IR_PI03_R00.this.logisCd));
					arguments.add(new BasicNameValuePair("selText",
							IR_PI03_R00.this.et_pi_inqueryText.getText()
									.toString()));

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
						itemList01.add(new IR_PI03_R00_Item(ejl01.getValue(i,
								"ITEM_NO"), ejl01.getValue(i, "ITEM_NM"), ejl01
								.getValue(i, "DRAW_NO"),
								ejl01.getValue(i, "QTY"),
								ejl01.getValue(i, "STOCK_CD")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList01.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				KeyboardUtil.hideKeyboard(et_pi_inqueryText, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);
				adpater01 = new IR_PI03_R00_Adapter(context,
						R.layout.ir_pi03_r00_adapter, itemList01, true);
				IR_PI03_R00.this.lv_pi_partsStockCondition
						.setAdapter(adpater01);

			}
		}
	}// end of SelectData inner-class


	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_PI03_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_PI03_R00.this.progress.dismiss();
		}
	}

	private IR_PI03_R00_Item currentSelectedListViewItem;

	private String selTp;



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
