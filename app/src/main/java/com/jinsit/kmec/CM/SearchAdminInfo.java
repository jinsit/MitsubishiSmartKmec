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

public class SearchAdminInfo extends Dialog implements OnClickListener,
		OnItemClickListener, OnDismissListener {

	public SearchAdminInfo(Context c, String bldgNo) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
		pBldgNo = bldgNo;
	}
	private ProgressDialog ProgressDialog;
	ArrayList<SearchAdminInfoResData> adminInfoData;
	private EasyJsonList ejl;
	TextView  btn_popClose;

	ListView lv_search_admin;
	SearchAdminInfoAdapter searchAdminListAdapter;
	Context context;
	public String returnStr = "";
	String pBldgNo;
	public String getReturnStr() {
		return returnStr;
	}

	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_search_admininfo);
		TextView tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		tv01_popTitle.setText("관리자 정보");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		lv_search_admin = (ListView) findViewById(R.id.lv_search_admin);
		lv_search_admin.setOnItemClickListener(this);
		adminInfoSearch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
		}

	}

	private void adminInfoSearch() {
		// TODO Auto-generated method stub
		new AdminInfoSearch().execute();
	}

	public class AdminInfoSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			SearchAdminInfo.this.ProgressDialog =
					android.app.ProgressDialog.show(context, "조회중","조회중 입니다.");

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {


				adminInfoData = new ArrayList<SearchAdminInfoResData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cm/searchAdministratorInfo.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("pBldgNo", pBldgNo));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					adminInfoData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						adminInfoData.add(new SearchAdminInfoResData(ejl.getValue(i,"CLIENT_NM"),
								ejl.getValue(i,"MOBILE"),
								ejl.getValue(i,"MAIL_ADDR"),
								ejl.getValue(i, "PHONE")));
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
			SearchAdminInfo.this.ProgressDialog.dismiss();
			searchAdminListAdapter = new SearchAdminInfoAdapter(context, adminInfoData);
			lv_search_admin.setAdapter(searchAdminListAdapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		SearchAdminInfoResData resData = adminInfoData.get(position);
		returnStr = resData.getCLIENT_NM();

		Log.e("hogi", resData.getCLIENT_NM());
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		setReturnStr(returnStr);

	}
}
