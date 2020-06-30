package com.jinsit.kmec.CM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SelectDept extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener {

	public SelectDept(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
	}
	private ProgressDialog ProgressDialog;
	ArrayList<SearchDeptResData> deptData;
	private EasyJsonList ejl;
	TextView btn_cancel,btn_popClose;

	TextView tv01_popTitle;
	ListView lv_searchList;
	public SelectDeptAdapter selectDepListAdapter;
	Context context;
	public String deptNm = "";
	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String deptCd = "";

	public String getDeptCd() {
		return deptCd;
	}

	public void setDeptCd(String depCd) {
		this.deptCd = depCd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_select_dept);

		btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		tv01_popTitle.setText("팀/지구 조회");
		btn_popClose = (TextView)findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		lv_searchList = (ListView) findViewById(R.id.lv_select_dept);
		lv_searchList.setOnItemClickListener(this);
		elevSearch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_popClose:
				adapterInit();
				cancel();
				break;

		}

	}

	void adapterInit(){
		deptData = new 	ArrayList<SearchDeptResData>();
		selectDepListAdapter = new SelectDeptAdapter(context, deptData);
	}

	private void elevSearch() {
		// TODO Auto-generated method stub
		new ElevatorSearch().execute();
	}

	public class ElevatorSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SelectDept.this.ProgressDialog =
					android.app.ProgressDialog.show(context, "알림","조회 중 입니다...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				deptData = new ArrayList<SearchDeptResData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cm/searchDepartment.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					deptData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						deptData.add(new SearchDeptResData(ejl.getValue(i,"DEPT_CD"),
								ejl.getValue(i, "DEPT_NM")));
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
			SelectDept.this.ProgressDialog.dismiss();
			selectDepListAdapter = new SelectDeptAdapter(context, deptData);
			lv_searchList.setAdapter(selectDepListAdapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		SearchDeptResData resData = deptData.get(position);
		deptCd = resData.getDEPT_CD();
		deptNm = resData.getDEPT_NM();
		Log.e("hogi", resData.getDEPT_NM());
		selectDepListAdapter.setChecked(position);
		selectDepListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		this.setDeptCd(deptCd);
		this.setDeptNm(deptNm);

	}
}
