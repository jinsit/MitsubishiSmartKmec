package com.jinsit.kmec.IR.CI;


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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IR_CI04_R00 extends Fragment implements OnItemClickListener,
		OnClickListener {
	private Context context;
	private Activity activity;

	private TextView et_inqueryStartDate;
	private TextView btn_startCalendar;
	private TextView et_inqueryEndDate;
	private TextView btn_endCalendar;

	private TextView btn_inquery;
	///////////////////////////////////////////



	///4
	private ListView lv_ci04_constructionHistory;
	private IR_CI04_R00_Adapter01 adapter01;
	private List<IR_CI04_R00_ITEM01> itemList01;
	private List<IR_CI04_R01P_ITEM01> itemList02;

	//////




	private android.app.ActionBar aBar;

	private CM_SearchBldgInfo_ITEM01 item01;


	private EasyJsonList ejl01;
	private EasyJsonMap ej01;





	private ProgressDialog progress;

	/**
	 *
	 */
	public IR_CI04_R00(CM_SearchBldgInfo_ITEM01 item) {
		super();
		// TODO Auto-generated constructor stub
		this.item01 = item;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ir_ci04_r00, null);
		activityInit(view);
		return view;
	}

	protected void activityInit(View view) {
		context = this.activity;
		getInstances(view);
		setEvents();

		// 타이틀 바
		aBar = this.activity.getActionBar();
		aBar.setTitle("공사이력");
		aBar.setDisplayShowHomeEnabled(false);

		this.setInqueryStartDate(DateUtil.nowDate());
		this.setInqueryEndDate(DateUtil.nowDate());

		itemList01 = new ArrayList<IR_CI04_R00_ITEM01>();
		itemList02 = new ArrayList<IR_CI04_R01P_ITEM01>();

	}

	protected void getInstances(View view) {
		////4////
		lv_ci04_constructionHistory = (ListView)view.findViewById(R.id.lv_ci04_constructionHistory);
		/////////////


		/////공통 조회 부분////
		this.et_inqueryStartDate = (TextView)view.findViewById(R.id.et_inqueryStartDate);
		this.btn_startCalendar = (TextView)view.findViewById(R.id.btn_startCalendar);
		this.et_inqueryEndDate  = (TextView)view.findViewById(R.id.et_inqueryEndDate);
		this.btn_endCalendar = (TextView)view.findViewById(R.id.btn_endCalendar);
		this.btn_inquery = (TextView)view.findViewById(R.id.btn_inquery);
		///////
		setEvents();
	}

	protected void setEvents() {

		// ///공통 조회 부분////
		this.btn_startCalendar.setOnClickListener(this);
		this.btn_endCalendar.setOnClickListener(this);
		this.btn_inquery.setOnClickListener(this);
		this.et_inqueryStartDate.setOnClickListener(this);
		this.et_inqueryEndDate.setOnClickListener(this);
		// /////

		// //4////
		lv_ci04_constructionHistory.setOnItemClickListener(this);
		// ///////////

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle extras;
		switch (v.getId()) {
			// / coom_dialog_inquery_startend_date 위젯
			case R.id.btn_startCalendar:
				getCalendar(0);
				break;
			case R.id.btn_endCalendar:
				getCalendar(1);
				break;
			case R.id.btn_inquery:
				ci04Inquery();
				break;
			case R.id.et_inqueryStartDate:
				getCalendar(0);
				break;
			case R.id.et_inqueryEndDate:
				getCalendar(1);
				break;
			// ///////////////////////

			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			this.currentSelectedItem01 = itemList01.get(position);
			progress(true);
			new selectEquipmentDetail().execute("bagicWorkTime");

		} else {
			this.currentSelectedItem01 = null;
		}
	}


	public class selectEquipmentHistory extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectEquipmentHistory.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("bldgNo",
							IR_CI04_R00.this.item01.getBldgNo()));
					arguments.add(new BasicNameValuePair("contrDtFr",
							IR_CI04_R00.this.getInqueryStartDate()));
					arguments.add(new BasicNameValuePair("contrDtTo",
							IR_CI04_R00.this.getInqueryEndDate()));
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
						itemList01.add(new IR_CI04_R00_ITEM01(ejl01.getValue(i,"RS_CONTR_NO"),
								ejl01.getValue(i, "CONTR_DT"),
								ejl01.getValue(i, "RS_NM"),
								ejl01.getValue(i, "CAR_NO"),
								ejl01.getValue(i, "CAR_NM")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList01.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				adapter01 = new IR_CI04_R00_Adapter01(context,
						R.layout.ir_ci04_r00_adapter02, itemList01);
				IR_CI04_R00.this.lv_ci04_constructionHistory.setAdapter(adapter01);
			}
		}
	}// end of SelectData inner-class

	public class selectEquipmentDetail extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectEquipmentDetail.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("rsContrNo",
							IR_CI04_R00.this.currentSelectedItem01.getRsContrNo()));
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
					itemList02.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList02.add(new IR_CI04_R01P_ITEM01(ejl01.getValue(i,"CAR_NO"),
								ejl01.getValue(i,"DONG_CAR_NO"),
								ejl01.getValue(i,"MODEL_NM"),
								ejl01.getValue(i,"ITEM_NO"),
								ejl01.getValue(i,"ITEM_NM"),
								ejl01.getValue(i,"QTY"),
								ejl01.getValue(i,"COMP_DT"),
								ejl01.getValue(i,"MNG_NO")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				IR_CI04_R01P ir01 = new IR_CI04_R01P(context,itemList02);
				ir01.show();
			}
		}
	}// end of SelectData inner-class

	private void ci04Inquery()
	{
		progress(true);
		new selectEquipmentHistory().execute("bagicWorkTime");
	}



	private void getCalendar(int fromTo) {
		Intent intent = new Intent(this.activity, com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
		startActivityForResult(intent, fromTo);
	}

	/**
	 * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
	 *
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == -1) {
					Bundle bundle = data.getExtras();
					this.setInqueryStartDate(bundle
							.getString("dateSelected"));
					break;
				} else {
					break;
				}

			case 1:
				if (resultCode == -1) {
					Bundle bundle = data.getExtras();
					this.setInqueryEndDate(bundle
							.getString("dateSelected"));
					break;
				} else {
					break;
				}
			default:
				break;
		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_CI04_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_CI04_R00.this.progress.dismiss();
		}
	}

	/// coom_dialog_inquery_startend_date 위젯
	private String inqueryStartDate;
	public String getInqueryStartDate() {
		return  this.et_inqueryStartDate.getText().toString();
	}

	public void setInqueryStartDate(String inqueryStartDate) {
		this.inqueryStartDate = inqueryStartDate;
		this.et_inqueryStartDate.setText(inqueryStartDate);
	}
	private String inqueryEndDate;
	public String getInqueryEndDate() {
		return  this.et_inqueryEndDate.getText().toString();
	}

	public void setInqueryEndDate(String inqueryEndDate) {
		this.inqueryEndDate = inqueryEndDate;
		this.et_inqueryEndDate.setText(inqueryEndDate);
	}
	///////////////////////////


	private IR_CI04_R00_ITEM01 currentSelectedItem01;



}
