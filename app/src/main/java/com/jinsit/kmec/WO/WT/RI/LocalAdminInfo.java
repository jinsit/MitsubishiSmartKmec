package com.jinsit.kmec.WO.WT.RI;

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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SearchAdminInfoAdapter;
import com.jinsit.kmec.CM.SearchAdminInfoResData;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class LocalAdminInfo extends Dialog implements OnClickListener,
		OnItemClickListener {

	public LocalAdminInfo(Context c, String bldgNo) {
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
	LocalAdminInfo_Adapter localAdminInfo_Adapter;
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
		//new AdminInfoSearch().execute();
		LocalAdminInfoSearch();
	}

	
	private void LocalAdminInfoSearch(){
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = new DatabaseRawQuery().selectAdminInfo(pBldgNo);
				Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고 
				mCursor.moveToFirst();
				localAdminInfo_Adapter =new LocalAdminInfo_Adapter(context,
						R.layout.dialog_search_admininfo, mCursor, new String[] {
								"CLIENT_NM", "MOBILE", "PHONE" },
						null, 0);
				
				lv_search_admin.setAdapter(localAdminInfo_Adapter);
	}
	
	
	private class AdminInfoSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			  LocalAdminInfo.this.ProgressDialog = 
					  android.app.ProgressDialog.show(context, "로그인","로그인 중 입니다...");
			 
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
			LocalAdminInfo.this.ProgressDialog.dismiss();
			searchAdminListAdapter = new SearchAdminInfoAdapter(context, adminInfoData);
			lv_search_admin.setAdapter(searchAdminListAdapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		dismiss();
	}


}
