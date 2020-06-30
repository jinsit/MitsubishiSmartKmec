package com.jinsit.kmec.CM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SearchEmp extends AlertDialog implements OnClickListener,
		OnItemClickListener {

	// uiInstances
	Context pContext;
	// Context context;
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	private EditText et01_cm_valueBox;
	private TextView btn01_cm_sch;
	private ListView lv01_cm_dataList;

	// dto
	private Map<String, String> paraMap;
	private Map<String, String> dataMap;

	// http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;
	private List<CM_SearchEmp_ITEM01> itemList01;
	private ListAdapter adapter01;
	private ProgressDialog progress;

	public CM_SearchEmp(Context context, Map<String, String> paraMap) {
		super(context);
		this.pContext = context;
		this.paraMap = paraMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cm_search_emp);
		getInstances();
	}

	private void getInstances() {

		// context = getContext();
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		et01_cm_valueBox = (EditText) findViewById(R.id.et01_cm_valueBox);
		btn01_cm_sch = (TextView) findViewById(R.id.btn01_cm_sch);
		lv01_cm_dataList = (ListView) findViewById(R.id.lv01_cm_dataList);
		itemList01 = new ArrayList<CM_SearchEmp_ITEM01>();
		dataMap = new HashMap<String, String>();
		setEvents();

	}

	private void setEvents() {
		btn_popClose.setOnClickListener(this);
		btn01_cm_sch.setOnClickListener(this);
		lv01_cm_dataList.setOnItemClickListener(this);
		setConfig();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
			case R.id.btn01_cm_sch:
				CM_SearchEmp.this.progress = android.app.ProgressDialog.show(
						pContext, "알림", "조회 중입니다.");
				new Database().execute("selectEmployee");
				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}

	}

	private void setConfig() {
		tv01_popTitle.setText("작업자조회");
	}

	private class Database extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			setData(result);
			KeyboardUtil.hideKeyboard(
					et01_cm_valueBox,
					(InputMethodManager) pContext
							.getSystemService(pContext.INPUT_METHOD_SERVICE))
					.sendEmptyMessageDelayed(0, 100);
			// /키보드 하이드용
			super.onPostExecute(result);
		}
	}

	private void crud(String div) {

		GetHttp http = null;
		String argUrl = "";

		if (div.equals("selectEmployee")) {

			http = new GetHttp();
			argUrl = WebServerInfo.getUrl() + "cm/" + div + ".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments
					.add(new BasicNameValuePair("svcTp", paraMap.get("svcTp")));
			arguments.add(new BasicNameValuePair("nameOrEmpId",
					et01_cm_valueBox.getText().toString()));

			returnJson = http.getPost(argUrl, arguments, true);

			try {
				ejm01 = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
				ejl01 = new EasyJsonList(returnJson.getJSONArray("dataList"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void setData(String div) {

		if (div.equals("selectEmployee")) {

			try {
				boolean isError = ejm01.getValue("errCd").equals("1") ? true
						: false;
				if (!isError) {

					itemList01.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						itemList01.add(new CM_SearchEmp_ITEM01(ejl01.getValue(
								i, "DEPT_NM"), ejl01.getValue(i, "CS_EMP_ID"),
								ejl01.getValue(i, "EMP_NM"), ejl01.getValue(i,
								"PHONE_1"), ejl01
								.getValue(i, "ISRT_DT"), ejl01
								.getValue(i, "LAST_JOB_TM"), ejl01
								.getValue(i, "LOGIN_DAY"), ejl01
								.getValue(i, "DEVICE_ST"), ejl01
								.getValue(i, "WORK_NM"), ejl01
								.getValue(i, "ENG_ST"), ejl01.getValue(
								i, "NOW_BLDG"), ejl01.getValue(i,
								"ADDR")));
					}

				} else if (isError) {
					alert(ejm01.getValue("errMsg"), pContext);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			adapter01 = (ListAdapter) new CM_SearchEmp_Adapter01(pContext,
					R.layout.cm_search_emp_adapter01, itemList01);
			lv01_cm_dataList.setAdapter(adapter01);
			CM_SearchEmp.this.progress.dismiss();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		try {
			dataMap.put("deptNm", ejl01.getValue(index, "DEPT_NM"));
			dataMap.put("csEmpId", ejl01.getValue(index, "CS_EMP_ID"));
			dataMap.put("empNm", ejl01.getValue(index, "EMP_NM"));
			dataMap.put("phone1", ejl01.getValue(index, "PHONE_1"));
			dataMap.put("isrtDt", ejl01.getValue(index, "ISRT_DT"));
			dataMap.put("lastJobTm", ejl01.getValue(index, "LAST_JOB_TM"));
			dataMap.put("loginDay", ejl01.getValue(index, "LOGIN_DAY"));
			dataMap.put("deviceSt", ejl01.getValue(index, "DEVICE_ST"));
			dataMap.put("workNm", ejl01.getValue(index, "WORK_NM"));
			dataMap.put("engSt", ejl01.getValue(index, "ENG_ST"));
			dataMap.put("nowBldg", ejl01.getValue(index, "NOW_BLDG"));
			dataMap.put("addr", ejl01.getValue(index, "ADDR"));
			dismiss();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// utils
	public Map getDataMap() {
		return this.dataMap;
	}

	private void alert(String msg, Context context) {
		AlertView.showAlert(msg, context);
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SearchEmp.this.progress = android.app.ProgressDialog.show(
					pContext, "알림", "조회 중입니다.");
		} else {
			CM_SearchEmp.this.progress.dismiss();
		}
	}

};
