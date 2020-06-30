package com.jinsit.kmec.DM.DM;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

/**
 * @deprecated OT/근태조회로 바뀌면서 DM_DM02_R01로 변경함
 * @author 원성민
 *
 */
public class DM_DM02_R00 extends Activity implements OnClickListener,
		OnItemClickListener, OnItemSelectedListener {

	private Context context;


	private Spinner sp_dm_attendCd;
	private TextView btn_dm_save;
	private TextView et_dm_date;

	private EditText et_dm_rmk;
	private TextView btn_dm_calendar;


	private ProgressDialog progress;
	private CommonSession commonSession;



	private EasyJsonList ejl01;

	private List<DM_DM02_R00_ITEM01> itemList01;

	private DM_DM02_R00_Adapter01 adapter01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm02_r00);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;
		commonSession = new CommonSession(context);

		//타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("근태 등록");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		itemList01 = new ArrayList<DM_DM02_R00_ITEM01>();
		currentSelectedItem01 = null;
		searchAttendanceType();
		this.setAttendDate(commonSession.getWorkDt());
	}

	protected void getInstances() {
		sp_dm_attendCd = (Spinner) findViewById(R.id.sp_dm_attendCd);
		btn_dm_save = (TextView) findViewById(R.id.btn_dm_save);
		btn_dm_calendar = (TextView) findViewById(R.id.btn_dm_calendar);
		et_dm_date = (TextView) findViewById(R.id.et_dm_date);
		et_dm_rmk = (EditText) findViewById(R.id.et_dm_rmk);

		setEvents();
	}

	protected void setEvents() {
		btn_dm_save.setOnClickListener(this);
		btn_dm_calendar.setOnClickListener(this);
		et_dm_date.setOnClickListener(this);
		sp_dm_attendCd.setOnItemSelectedListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_dm_calendar:
				getCalendar(0);
				break;
			case R.id.et_dm_date:
				getCalendar(0);
				break;
			case R.id.btn_dm_save:
				if(this.currentSelectedItem01 == null){
					AlertView.showAlert("작업구분을 선택해주세요", context);
					return;
				}
				this.progress(true);
				new insertAttendanceRequest().execute("bagicWorkTime");
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if(position > -1)
		{
			currentSelectedItem01 = itemList01.get(position);
		}
		else
		{
			currentSelectedItem01 = null;
		}
	}


	public void searchAttendanceType(){
		progressInquery(true);
		new selectAttendanceType().execute("bagicWorkTime");

	}
	public class selectAttendanceType extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectAttendanceType.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
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
					itemList01.add(new DM_DM02_R00_ITEM01("", "작업구분선택",""));
					int jsonSize01 = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList01.add(new DM_DM02_R00_ITEM01(ejl01.getValue(i,
								"SUB_CD"), ejl01.getValue(i, "TITLE")
								, ejl01.getValue(i, "ORD_SQ")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progressInquery(false);
				adapter01 = new DM_DM02_R00_Adapter01(context,
						R.layout.dm_dm02_r00_adapter01, itemList01);
				DM_DM02_R00.this.sp_dm_attendCd.setAdapter(adapter01);
			}

		}
	}// end of SelectData inner-class

	public class insertAttendanceRequest extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/insertAttendanceRequest.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId",commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("attendDt",DM_DM02_R00.this.getAttendDate()));
					arguments.add(new BasicNameValuePair("attendCd",DM_DM02_R00.this.currentSelectedItem01.getSubCd()));
					arguments.add(new BasicNameValuePair("attendTm","08:30"));
					arguments.add(new BasicNameValuePair("leaveTm","17:30"));
					arguments.add(new BasicNameValuePair("workTm","540"));
					arguments.add(new BasicNameValuePair("remark",DM_DM02_R00.this.getRemark()));
					arguments.add(new BasicNameValuePair("userId",commonSession.getEmpId()));
					returnJson01 = http.getPost(param_url_01, arguments, true);
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

			String resultFg = "0";
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {

				try {
					resultFg = returnJson01.getString("dataString");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(resultFg.equals("1"))
				{
					AlertView.showAlert("근태 등록 했습니다.", context);
					AlertView.showAlert("근태 등록 했습니다.", context, new android.content.DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
							DM_DM02_R00.this.finish();
						}
					});
				}
				else
				{
					AlertView.showAlert("근태 등록 실패 했습니다.", context);
				}

			}


		}
	}// end of SelectData inner-class

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
							   long id) {
		// TODO Auto-generated method stub
		if (position < 1) {
			this.currentSelectedItem01 = null;
		} else {
			this.currentSelectedItem01 = itemList01.get(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		this.currentSelectedItem01 = null;

	}

	private void progressInquery(Boolean isActivated) {
		if (isActivated) {
			DM_DM02_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			DM_DM02_R00.this.progress.dismiss();
		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			DM_DM02_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "등로 중입니다.");
		} else {
			DM_DM02_R00.this.progress.dismiss();
		}
	}

	private void getCalendar(int fromTo) {

		Intent intent = new Intent(DM_DM02_R00.this,
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
					this.setAttendDate(bundle
							.getString("dateSelected"));
					break;
				} else {
					break;
				}
			default:
				break;
		}
	}

	private DM_DM02_R00_ITEM01 currentSelectedItem01;
	private String attendDate;
	public String getAttendDate() {
		return  this.et_dm_date.getText().toString();
	}

	public void setAttendDate(String registerDate) {
		this.attendDate = registerDate;
		this.et_dm_date.setText(registerDate);
	}

	private String remark;
	private String getRemark()
	{
		return this.et_dm_rmk.getText().toString();
	}
	private void setRemark(String remark)
	{
		this.remark = remark;
		this.et_dm_rmk.setText(remark);
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
	private boolean isFirstHide = false;
	private void navigationInit(){
		testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
		homeNavi = new HomeNavigation(context, null);
		homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		if(!isFirstHide){
			naviPref.setHide(true);
			isFirstHide = true;
		}
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


	
	
