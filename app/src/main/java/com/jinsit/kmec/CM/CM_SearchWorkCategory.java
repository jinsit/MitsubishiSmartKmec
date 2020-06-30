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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SearchWorkCategory extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener {


	public CM_SearchWorkCategory(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
		this.context = c;
	}

	private TextView tv01_popTitle;
	private TextView btn_popClose;


	private ArrayList<CM_SearchWorkCategory_ITEM01> itemList01;
	private EasyJsonList ejl01;


	private ListView lv_cm_searchWorkCategory;
	private CM_SearchWorkCategory_Adapter01 adapter01;;

	private ProgressDialog progress;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_searchworkcategory);
		activityInit();
	}




	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("작업분류코드");
		this.itemList01 = new ArrayList<CM_SearchWorkCategory_ITEM01> ();

	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle =(TextView) findViewById(R.id.tv01_popTitle);
		lv_cm_searchWorkCategory = (ListView) findViewById(R.id.lv_cm_searchWorkCategory);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		lv_cm_searchWorkCategory.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
			default:
				break;
		}
	}

	public void searchWorkCategory() {
		// TODO Auto-generated method stub
		progress(true);
		new selectWorkCategory().execute("bagicWorkTime");
	}



	public class selectWorkCategory extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ip/selectWorkCategory.do";
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
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList01.add(new CM_SearchWorkCategory_ITEM01(ejl01.getValue(i,"WORK_CD"),
								ejl01.getValue(i, "WORK_NM"),
								ejl01.getValue(i, "CHECK1"),
								ejl01.getValue(i, "CHECK2"),
								ejl01.getValue(i, "CHECK3"),
								ejl01.getValue(i, "CHECK4")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				adapter01 = new CM_SearchWorkCategory_Adapter01(context,
						R.layout.cm_searchworkcategory_adapter01, itemList01);
				CM_SearchWorkCategory.this.lv_cm_searchWorkCategory
						.setAdapter(adapter01);
			}
		}
	}// end of SelectData inner-class


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			currentSelectedItem01 = itemList01.get(position);
			dismiss();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SearchWorkCategory.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_SearchWorkCategory.this.progress.dismiss();
		}
	}
	private CM_SearchWorkCategory_ITEM01 currentSelectedItem01;
	public CM_SearchWorkCategory_ITEM01 getSelectedItem()
	{
		return this.currentSelectedItem01;
	}




}
