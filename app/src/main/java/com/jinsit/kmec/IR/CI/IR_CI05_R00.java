package com.jinsit.kmec.IR.CI;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class IR_CI05_R00 extends Fragment implements OnItemClickListener{
	private Context context;
	private Activity activity;

	private ListView lv_ci05_repaireSales;
	private IR_CI05_R00_Adapter01 adapter04;
	private IR_CI05_R01P_ITEM01 item04;
	private List<IR_CI05_R00_ITEM01> itemList05;
	private List<IR_CI05_R01P_ITEM02> itemList06;


	private android.app.ActionBar aBar;

	private CM_SearchBldgInfo_ITEM01 item01;


	private EasyJsonList ejl01;
	private EasyJsonMap ej01;





	private ProgressDialog progress;
	/**
	 *
	 */
	public IR_CI05_R00(CM_SearchBldgInfo_ITEM01 item) {
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
		View view = inflater.inflate(R.layout.ir_ci05_r00, null);
		activityInit(view);
		return view;

	}

	protected void activityInit(View view) {
		context = this.activity;
		getInstances(view);

		// 타이틀 바
		aBar = this.activity.getActionBar();
		aBar.setTitle("보수 영업");
		aBar.setDisplayShowHomeEnabled(false);




		itemList05 = new ArrayList<IR_CI05_R00_ITEM01>();
		itemList06 = new ArrayList<IR_CI05_R01P_ITEM02>();
		ci05Inquery();
	}

	protected void getInstances(View view) {
		/////////////
		////5////
		lv_ci05_repaireSales = (ListView)view.findViewById(R.id.lv_ci05_repaireSales);
		/////////////

		setEvents();
	}

	protected void setEvents() {

		////5////
		lv_ci05_repaireSales.setOnItemClickListener(this);
		/////////////
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			this.currentSelectedItem05 = itemList05.get(position);
			progress(true);
			new selectDetail().execute("bagicWorkTime");
		} else {
			this.currentSelectedItem05 = null;
		}
	}


	public class selectRepairSales extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectRepairSales.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("bldgNo",
							IR_CI05_R00.this.item01.getBldgNo()));
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
					itemList05.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList05.add(new IR_CI05_R00_ITEM01(ejl01.getValue(i,"BLDG_NO"),
								ejl01.getValue(i, "BLDG_NM"),
								ejl01.getValue(i, "CS_CONTR_NO"),
								ejl01.getValue(i, "CONTR_DT"),
								ejl01.getValue(i, "CS_NM"),
								ejl01.getValue(i, "RUN_ST"),
								ejl01.getValue(i, "CS_CD")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList05.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				adapter04 = new IR_CI05_R00_Adapter01(context,
						R.layout.ir_ci05_r00_adapter01, itemList05);
				IR_CI05_R00.this.lv_ci05_repaireSales.setAdapter(adapter04);
			}
		}
	}// end of SelectData inner-class

	public class selectDetail extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectContrInfo.do";
					String param_url_02 = WebServerInfo.getUrl()
							+ "ir/selectElevContrInfo.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("csContrNo",
							IR_CI05_R00.this.currentSelectedItem05.getCsContrNo()));
					returnJson01 = http.getPost(param_url_01, arguments, true);
					returnJson02 = http.getPost(param_url_02, arguments, true);

					try {

						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
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
					itemList06.clear();
					item04 =  new IR_CI05_R01P_ITEM01(ej01.getValue("BLDG_NO"),
							ej01.getValue("BLDG_NM"),
							ej01.getValue("DEPT_NM"),
							ej01.getValue("CS_CONTR_NO"),
							ej01.getValue("CS_NM"),
							ej01.getValue("RUN_ST"),
							ej01.getValue("CONTR_DT"),
							ej01.getValue("ISSUE_DT"),
							ej01.getValue("EXPIRE_DT"),
							ej01.getValue("CS_CNT"),
							ej01.getValue("RES_CNT"),
							ej01.getValue("CS_PRC"),
							ej01.getValue("RES_PRC"),
							ej01.getValue("AMT"),
							ej01.getValue("RMK"));

					int jsonSize = returnJson02.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList06.add(new IR_CI05_R01P_ITEM02(ejl01.getValue(i,"CAR_NO"),
								ejl01.getValue(i,"MODEL_NM"),
								ejl01.getValue(i,"P_CS_PRC"),
								ejl01.getValue(i,"RMK")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				IR_CI05_R01P ir01 = new IR_CI05_R01P(context, item04,itemList06);
				ir01.show();
			}
		}
	}// end of SelectData inner-class

	private void ci05Inquery()
	{
		progress(true);
		new selectRepairSales().execute("bagicWorkTime");
	}


	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_CI05_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_CI05_R00.this.progress.dismiss();
		}
	}

	private IR_CI05_R00_ITEM01 currentSelectedItem05;


}
