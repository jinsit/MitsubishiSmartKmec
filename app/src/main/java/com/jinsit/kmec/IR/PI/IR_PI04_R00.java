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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_PI04_R00 extends Activity implements OnClickListener {

	private Context context;

	private TextView btn_pi_inquery;
	private EditText et_pi_inqueryText;

	private TextView tv_menutitle;
	private TextView btn_menu;

	private ListView lv_pi_partsSellingCondition;

	private OnClickListener onClickListener;
	// POP1
	private EasyJsonList ejl01;
	private List<IR_PI04_R00_Item> itemList;

	private ProgressDialog progress;
	private ListAdapter adpater01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi04_r00);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("부품판가현황");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		selTp = "1";
		tv_menutitle.setText("아이템 No");
		itemList = new ArrayList<IR_PI04_R00_Item>();
	}

	protected void getInstances() {
		btn_pi_inquery = (TextView) findViewById(R.id.btn_pi_inquery);
		btn_menu = (TextView) findViewById(R.id.btn_menu);
		tv_menutitle = (TextView) findViewById(R.id.tv_menutitle);
		et_pi_inqueryText = (EditText) findViewById(R.id.et_pi_inqueryText);
		lv_pi_partsSellingCondition = (ListView) findViewById(R.id.lv_pi_partsSellingCondition);
		setEvents();
	}

	protected void setEvents() {
		btn_pi_inquery.setOnClickListener(this);
		btn_menu.setOnClickListener(this);
		onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_pi_adapter_image:
						TextView button = (TextView)v;
						itemNo = button.getTag().toString();

						progress(true);
						new selectItemImage().execute("bagicWorkTime");

						break;
				}
			}
		};
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_pi_inquery:
				if(et_pi_inqueryText.getText().length() < 1){
					AlertView.showAlert("검색어를 한글자 이상 입력하셔야 합니다.", context);
					return;
				}
				progress(true);
				new selectSellingPrice().execute("bagicWorkTime");
				break;
			case R.id.btn_menu:
				popupMenu(v, R.menu.menu_ir_pi03);
			default:
				break;
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
						selTp = "1";
						tv_menutitle.setText("아이템 No");
						break;
					case R.id.menu_pi_itemNm:
						selTp = "2";
						tv_menutitle.setText("아이템 이름");
						break;
					case R.id.menu_pi_size:
						selTp = "3";
						tv_menutitle.setText("규격");
					default:
						break;

				}
				return false;
			}
		});
		pop.show();
	}

	public class selectSellingPrice extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectSellingPrice.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp",
							IR_PI04_R00.this.selTp));
					arguments.add(new BasicNameValuePair("selText",
							IR_PI04_R00.this.et_pi_inqueryText.getText()
									.toString()));
					arguments.add(new BasicNameValuePair("drawNo",""));

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
					itemList.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList.add(new IR_PI04_R00_Item(
								ejl01.getValue(i,"ITEM_NO"),
								ejl01.getValue(i, "ITEM_NM"),
								ejl01.getValue(i, "SIZE"),
								ejl01.getValue(i, "APPLY_DT"),
								ejl01.getValue(i, "LIFE_CYCLE"),
								ejl01.getValue(i, "WORKER"),
								ejl01.getValue(i, "WORK_TM"),
								ejl01.getValue(i, "UNIT_PRC"),
								ejl01.getValue(i, "BUY_PRC"),
								ejl01.getValue(i, "LT"),
								ejl01.getValue(i, "RMK"),
								ejl01.getValue(i, "ISRT_USR_ID"),
								ejl01.getValue(i, "UPDT_USR_ID"),
								ejl01.getValue(i, "IMG_CHK")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				KeyboardUtil.hideKeyboard(et_pi_inqueryText, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);

				adpater01 = new IR_PI04_R00_Adapter(context,
						R.layout.ir_pi04_r00_adapter, itemList,onClickListener);
				IR_PI04_R00.this.lv_pi_partsSellingCondition
						.setAdapter(adpater01);
			}
		}
	}// end of SelectData inner-class



	public class selectItemImage extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectItemImage.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("itemNo",itemNo));
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
				String img = "";
				try {
					img = ejl01.getValue(0, "IMG1");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress(false);



				try {
					Bitmap bm = DataConvertor.Base64Bitmap(img);
					IR_PI04_R01P ir01 = new IR_PI04_R01P(context, bm);
					ir01.show();
				}
				catch(Exception ex){
					Toast.makeText(context, "이미지 불러오기를 실패했습니다.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}// end of SelectData inner-class

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_PI04_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_PI04_R00.this.progress.dismiss();
		}
	}



	private String selTp;
	private String itemNo;


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
