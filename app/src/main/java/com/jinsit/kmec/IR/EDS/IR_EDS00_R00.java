package com.jinsit.kmec.IR.EDS;

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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SearchDeptAdapter;
import com.jinsit.kmec.CM.SearchDeptResData;
import com.jinsit.kmec.IR.ES.IR_ES00_R01P_Item;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;


/**
 * @Package_Name	: com.jinsit.kmec.IR.EDS
 * @Class_Name		: IR_EDS00_R00.java
 * @Date			: 2016. 1. 26
 * @Developer		: 원성민
 * @Description_Kor	: 교육이수현황
 * @ps				: 작업자 현황과 화면구성이 똑같으니 뷰를 같이 쓰도록 한다.
 */
public class IR_EDS00_R00 extends Activity implements OnClickListener{


	//THIS
	private Context context;
	private ListView lv01_contentOfDept;
	private EditText et01_es_empNm;
	private TextView btn01_es_sch;
	private TextView btn_menu;
	private TextView tv_menutitle;
	private LinearLayout lin_inquery;

	private ArrayList<SearchDeptResData> eleData;
	private SearchDeptAdapter searchEleListAdapter;

	private String schType;

	//POP01 :: IR_ES00_R01P
	private EasyJsonList ejl;
	private IR_EDS00_R01P_Adapter adapter = null;
	private List<IR_ES00_R01P_Item> itemList;


	//UTILITIES
	private Boolean jsonDebuggingMode = true;
	private ProgressDialog progress;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_es00_r00);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("교육이수 현황");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();
		manageSchType(false);
	}


	private void getInstances() {
		context = this;
		itemList = new ArrayList<IR_ES00_R01P_Item>();
		lv01_contentOfDept = (ListView) findViewById(R.id.lv01_contentOfDept);
		et01_es_empNm = (EditText) findViewById(R.id.et01_es_empNm);
		btn01_es_sch = (TextView) findViewById(R.id.btn01_es_sch);
		btn_menu =(TextView)findViewById(R.id.btn_menu);
		tv_menutitle=(TextView)findViewById(R.id.tv_menutitle);
		lin_inquery = (LinearLayout)findViewById(R.id.lin_inquery);
		setEvents();
	}

	private void setEvents(){
		btn01_es_sch.setOnClickListener(clickListener);
		btn_menu.setOnClickListener(this);
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
			case R.id.btn_menu:
				popupMenu(v, R.menu.menu_ir_es00);
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
					case R.id.menu_es_department:
						schType = "1";
						tv_menutitle.setText("부서별조회");
						manageSchType(false);
						//부서리스트 조회 (DB)
						progress(true);
						new selectData().execute("deptList");
						break;
					case R.id.menu_es_indiviual:
						schType = "2";
						tv_menutitle.setText("개인별조회");
						lv01_contentOfDept.setAdapter(null);
						manageSchType(true);
						break;
					default:
						break;

				}
				return false;
			}
		});
		pop.show();
	}


	//[Event _ 부서List에서 click 시]
	OnItemClickListener itemClickListener =  new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			String deptCd = eleData.get(position).getDEPT_CD();
			String deptNm = eleData.get(position).getDEPT_NM();

			progress(true);
			new selectData().execute("engListByDeptCd",deptCd,"2y");

		}
	};

	//[Event _ 개인별조회에서 조회Button을 click 시]
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn01_es_sch:
					if(et01_es_empNm.getText().length() < 1){
						alert("이름을 입력하세요.", context);
						return;
					}
					KeyboardUtil.hideKeyboard(et01_es_empNm, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);
					progress(true);
					new selectData().execute("engList");

					break;
			}
		}
	};


	//[Method _ Data를 조회]
	public class selectData extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {

			//1. deptList
			if(params[0].equals("deptList")){

				eleData = new ArrayList<SearchDeptResData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ir/selectDeptList.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();

				JSONObject returnJson = getHttp.getPost(param_url, arguments, false);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );
					eleData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for(int i=0; i<jsonSize; i++){
						eleData.add(new SearchDeptResData(ejl.getValue(i, "DEPT_CD")
								,ejl.getValue(i, "DEPT_NM")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				return params[0];
				//2. engList
			}else if(params[0].equals("engListByDeptCd")){

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ir/selectEngineer.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", schType));
				arguments.add(new BasicNameValuePair("selCd", params[1]));
				arguments.add(new BasicNameValuePair("selNm", params[2]));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, false);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					itemList.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for(int i=0; i<jsonSize; i++){
						itemList.add(new IR_ES00_R01P_Item(   ejl.getValue(i, "DEPT_NM")
								, ejl.getValue(i, "EMP_NM")
								, ejl.getValue(i, "WORK_NM")
								, ejl.getValue(i, "NOW_BLDG"))
						);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				return params[0];

				//3. engList
			}else if(params[0].equals("engList")){
				String selCd = et01_es_empNm.getText().toString();

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ir/selectEngineer.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", schType));
				arguments.add(new BasicNameValuePair("selCd", selCd));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, jsonDebuggingMode);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );
					itemList.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for(int i=0; i<jsonSize; i++){
						itemList.add(new IR_ES00_R01P_Item(   ejl.getValue(i, "DEPT_NM")
								, ejl.getValue(i, "EMP_NM")
								, ejl.getValue(i, "WORK_NM")
								, ejl.getValue(i, "NOW_BLDG"))
						);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return params[0];
			}
			return "None";
		}

		@Override
		protected void onProgressUpdate(Integer... params) { };

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			//1. deptList
			if(result.equals("deptList")){

				progress(false);
				searchEleListAdapter = new SearchDeptAdapter(context, eleData);
				lv01_contentOfDept.setAdapter(searchEleListAdapter);
				lv01_contentOfDept.setOnItemClickListener(itemClickListener);

				//2. engList
			}else if(result.equals("engListByDeptCd")){

				progress(false);
				adapter = new IR_EDS00_R01P_Adapter( context, R.layout.ir_eds00_r01p_adapter, itemList );
				SimpleDialog sd = new SimpleDialog(context, "작업자선택", adapter, new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						try {
							IR_EDS00_R00P iR_Eds00_R00P = new IR_EDS00_R00P(
									context, ejl.getValue(position,
									"CS_EMP_ID"));
							iR_Eds00_R00P.show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
				sd.show();

				//3. engList
			}else if(result.equals("engList")){

				progress(false);
				adapter = new IR_EDS00_R01P_Adapter( context, R.layout.ir_eds00_r01p_adapter, itemList );
				lv01_contentOfDept.setAdapter(adapter);

				lv01_contentOfDept.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


						try {
							IR_EDS00_R00P iR_Eds00_R00P = new IR_EDS00_R00P(
									context, ejl.getValue(position,
									"CS_EMP_ID"));
							iR_Eds00_R00P.show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});


			}else if(result.equals("None")){
				alert("none", context);
			}
		}
	}//end of selectData inner class


	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			IR_EDS00_R00.this.progress =
					android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		}else{
			IR_EDS00_R00.this.progress.dismiss();
		}
	}

	private void manageSchType(Boolean goneOrVisible){
		if(goneOrVisible){
			lin_inquery.setVisibility(View.VISIBLE);
			et01_es_empNm.setVisibility(View.VISIBLE);
			btn01_es_sch.setVisibility(View.VISIBLE);
		}else{
			lin_inquery.setVisibility(View.GONE);
			et01_es_empNm.setVisibility(View.GONE);
			btn01_es_sch.setVisibility(View.GONE);
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


};
