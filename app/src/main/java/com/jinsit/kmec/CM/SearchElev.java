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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SearchElev extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener {

	public SearchElev(Context c, String bldgNo) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
		pBldgNo = bldgNo;
	}
	String pBldgNo;
	private ProgressDialog ProgressDialog;
	ArrayList<SearchElevResData> elevData;
	private EasyJsonList ejl;
	Button btn_searchElev, btn_cancel;

	ListView lv_searchElev;
	SearchElevAdapter searchElevListAdapter;
	Context context;
	public String returnStr = "";

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
		setContentView(R.layout.dialog_search_elev);

		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		lv_searchElev = (ListView) findViewById(R.id.lv_search_elev);
		lv_searchElev.setOnItemClickListener(this);
		elevSearch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
		}

	}

	private void elevSearch() {
		// TODO Auto-generated method stub
		new ElevatorSearch().execute();
	}

	public class ElevatorSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SearchElev.this.ProgressDialog =
					android.app.ProgressDialog.show(context, "알림","조회중 입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {


				elevData = new ArrayList<SearchElevResData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cm/searchElevator.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("pBldgNo",pBldgNo));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					elevData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						elevData.add(new SearchElevResData(ejl.getValue(i,"BLDG_NO"),
								ejl.getValue(i,"CAR_NO"),
								ejl.getValue(i,"DONG_CAR_NO"),
								ejl.getValue(i, "MODEL_NM")));
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
			SearchElev.this.ProgressDialog.dismiss();
			searchElevListAdapter = new SearchElevAdapter(context, elevData);
			lv_searchElev.setAdapter(searchElevListAdapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		SearchElevResData resData = elevData.get(position);
	/*	DetailDialog dd = new DetailDialog(context, resData);
		dd.show();
		SimpleDialog sd = new SimpleDialog(context, "title", searchElevListAdapter, new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}});
		sd.show();*/

		returnStr = resData.getCAR_NO();
		//Log.e("hogi", resData.getCAR_NO());
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		setReturnStr(returnStr);

	}
}
