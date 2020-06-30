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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.NT.IR_NT00_R01P;
import com.jinsit.kmec.IR.NT.IR_NT00_R01P.NoticeImageTask;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SearchDept extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener{
	public SearchDept(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
	}
	private ProgressDialog ProgressDialog;
	ArrayList<SearchDeptResData> deptData;
	private EasyJsonList ejl;
	Button btn_cancel;
	TextView btn_popClose;

	ListView lv_search_dept;
	SearchDeptAdapter searchDeptListAdapter;
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
		setContentView(R.layout.dialog_search_dept);
		TextView title = (TextView)findViewById(R.id.tv01_popTitle);
		title.setText("부서조회");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		lv_search_dept = (ListView) findViewById(R.id.lv_search_dept);
		lv_search_dept.setOnItemClickListener(this);


		//deptSearch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_popClose:
				dismiss();
				break;

		}

	}
	private void progress(Boolean isActivated){
		if(isActivated){
			SearchDept.this.ProgressDialog =
					android.app.ProgressDialog.show(getContext(), "알림","부서 조회중 입니다.");
		}else{
			SearchDept.this.ProgressDialog.dismiss();
		}
	}
	public void deptSearch() {
		// TODO Auto-generated method stub
		progress(true);
		new DepartmentSearch().execute();
	}


	public class DepartmentSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			SearchDept.this.ProgressDialog.dismiss();
			searchDeptListAdapter = new SearchDeptAdapter(context, deptData);
			lv_search_dept.setAdapter(searchDeptListAdapter);

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
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		this.setDeptCd(deptCd);
		this.setDeptNm(deptNm);

	}
}
