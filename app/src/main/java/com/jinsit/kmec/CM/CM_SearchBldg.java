package com.jinsit.kmec.CM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SearchBldg extends Dialog implements OnClickListener,
		OnItemClickListener, OnDismissListener {

	public CM_SearchBldg(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private ProgressDialog progress;

	private TextView btn_cm_searchBldg;

	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private EditText et_cm_searchBox;
	private ListView lv_cm_searchList;

	private CM_SearchBldg_Adapter adapter01;
	private ArrayList<CM_SearchBldgInfo_ITEM01> itemList;

	private EasyJsonList ejl01;

	private CommonSession commonSession;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_searchbldg);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		commonSession = new CommonSession(context);
		itemList = new ArrayList<CM_SearchBldgInfo_ITEM01>();
		tv01_popTitle.setText("건물조회");
		//this.et_cm_searchBox.setText("63");
		currentSelectedItem = null;
	}

	protected void getInstances() {
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);

		btn_cm_searchBldg = (TextView) findViewById(R.id.btn_cm_searchBldg);
		et_cm_searchBox = (EditText) findViewById(R.id.et_cm_searchBox);
		lv_cm_searchList = (ListView) findViewById(R.id.lv_cm_searchList);
		setEvents();
	}

	protected void setEvents() {
		btn_cm_searchBldg.setOnClickListener(this);
		btn_popClose.setOnClickListener(this);
		lv_cm_searchList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cm_searchBldg:
				bldgSearch();
				break;
			case R.id.btn_popClose:
				dismiss();
				break;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			currentSelectedItem = itemList.get(position);
			dismiss();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	private void bldgSearch() {
		// TODO Auto-generated method stub
		if (et_cm_searchBox.getText().toString().equals("")) {
			AlertView.showAlert("검색어를 한글자 이상 입력하셔야 합니다.", context);
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
				arguments.add(new BasicNameValuePair("pBldgNm", et_cm_searchBox
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
			if(itemList.size() == 0){
				AlertView.showAlert("조회된 데이타가 없습니다", context);
			}
			adapter01 = new CM_SearchBldg_Adapter(context,
					R.layout.cm_searchbldg_adapter, itemList);
			lv_cm_searchList.setAdapter(adapter01);
			KeyboardUtil.hideKeyboard(et_cm_searchBox, (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);

		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SearchBldg.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_SearchBldg.this.progress.dismiss();
		}
	}

	private CM_SearchBldgInfo_ITEM01 currentSelectedItem;

	public CM_SearchBldgInfo_ITEM01 getCurrentSelectedItem() {
		return currentSelectedItem;
	}

	public String getCsDeptNm() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getCsDetpNm();
	}

	public String getBldgAddr() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getAddr();
	}

	public String getBldgNm() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getBldgNm();
	}

	public String getBldgNo() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getBldgNo();
	}

}
