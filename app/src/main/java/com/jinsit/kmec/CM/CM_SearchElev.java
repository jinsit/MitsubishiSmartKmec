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
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SearchElev extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener {


	public CM_SearchElev(Context c, String bldgNo, int selTp) {
		super(c);
		// TODO Auto-generated constructor stub
		this.context = c;
		this.pBldgNo = bldgNo;
		this.selTp = selTp;
	}

	public CM_SearchElev(Context c, String bldgNo) {
		super(c);
		// TODO Auto-generated constructor stub
		this.context = c;
		this.pBldgNo = bldgNo;
		this.selTp = 1;
	}
	ArrayList<CM_SearchElev_ITEM01> itemList01;
	private EasyJsonList ejl;
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private TextView btn_cm_ok;

	private ListView lv_cm_searchElev;
	private CM_SearchElev_Adapter01 adapter01;;

	private ProgressDialog progress;
	private int selTp;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_searchelev);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("호기조회");
		returnStrList = new ArrayList<String>();
		if(this.selTp ==3 )
		{
			this.btn_cm_ok.setVisibility(View.VISIBLE);
		}
		else
		{
			this.btn_cm_ok.setVisibility(View.GONE);
		}
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle =(TextView) findViewById(R.id.tv01_popTitle);
		lv_cm_searchElev = (ListView) findViewById(R.id.lv_cm_searchElev);
		btn_cm_ok = (TextView)findViewById(R.id.btn_cm_ok);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		lv_cm_searchElev.setOnItemClickListener(this);
		btn_cm_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				returnStrList.clear();
				returnStr = "";
				dismiss();
				break;
			case R.id.btn_cm_ok:
				if (this.selTp == 3) {
					returnStrList.clear();
					if (adapter01 != null) {
						for (CM_SearchElev_ITEM01 item : adapter01
								.getSelectedData()) {
							returnStrList.add(item.getCarNo());
						}
					}
				}
				dismiss();
				break;
			default:
				break;
		}
	}

	public void elevSearch() {
		// TODO Auto-generated method stub
		progress(true);
		new searchElevator().execute();
	}

	public class searchElevator extends AsyncTask<Void, Void, Void> {

		JSONObject returnJson01;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {


				itemList01 = new ArrayList<CM_SearchElev_ITEM01>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cm/searchElevator.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("pBldgNo",pBldgNo));
				returnJson01 = getHttp.getPost(param_url, arguments, true);
				try {
					ejl = new EasyJsonList(returnJson01.getJSONArray("dataList"));
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
			itemList01.clear();
			try {
				int jsonSize = returnJson01.getJSONArray("dataList").length();
				for (int i = 0; i < jsonSize; i++) {
					itemList01.add(new CM_SearchElev_ITEM01(ejl.getValue(i,"BLDG_NO"),
							ejl.getValue(i,"CAR_NO"),
							ejl.getValue(i,"DONG_CAR_NO"),
							ejl.getValue(i, "MODEL_NM")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			progress(false);
			adapter01 = new CM_SearchElev_Adapter01(context, selTp ,itemList01);
			lv_cm_searchElev.setAdapter(adapter01);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			currentSelectedItem01 = itemList01.get(position);
			if (this.selTp == 1) {
				returnStr = currentSelectedItem01.getCarNo();
				returnModelNm = currentSelectedItem01.getModelNm();
				dismiss();
			} else if(this.selTp == 2) {
				CM_SearchElevInfo cm01 = new CM_SearchElevInfo(context,currentSelectedItem01);
				cm01.show();
			} else if(this.selTp == 3) {
				CM_SearchElev_ITEM01 item = itemList01.get(position);
				adapter01.setChecked(position);
				adapter01.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SearchElev.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_SearchElev.this.progress.dismiss();
		}
	}
	private CM_SearchElev_ITEM01 currentSelectedItem01;
	private String pBldgNo;
	private String returnStr;
	public String getReturnStr()
	{
		return returnStr;
	}
	private String returnModelNm;
	public String getReturnModelNm()
	{
		return returnModelNm;
	}
	private ArrayList<String> returnStrList;
	public ArrayList<String> getReturnStrList()
	{
		return returnStrList;
	}
}
