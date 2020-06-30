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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SearchElevInfo extends Dialog implements OnClickListener {


	public CM_SearchElevInfo(Context c, CM_SearchElev_ITEM01 item01) {
		super(c);
		// TODO Auto-generated constructor stub
		this.context = c;
		this.item01 = item01;
	}

	private CM_SearchElev_ITEM01 item01;
	private CM_SearchElevInfo_ITEM01 item02;
	private EasyJsonMap ej01;

	private TextView tv_cm_carNo;
	private TextView tv_cm_carCd;
	private TextView tv_dm_modelNm;
	private TextView tv_cm_makerCd;
	private TextView tv_cm_fstInspRsDt;
	private TextView tv_cm_expireDt;
	private TextView tv_cm_custPassDt;
	private TextView tv_cm_dongCarNo;
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private CM_SearchElev_Adapter01 adapter01;;

	private ProgressDialog progress;
	private int selTp;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_searchelevinfo);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("호기정보");
		searchElevInfo();

	}

	protected void getInstances() {
		tv_cm_carNo = (TextView) findViewById(R.id.tv_cm_carNo);
		tv_cm_carCd = (TextView) findViewById(R.id.tv_cm_carCd);
		tv_dm_modelNm = (TextView) findViewById(R.id.tv_dm_modelNm);
		tv_cm_makerCd = (TextView) findViewById(R.id.tv_cm_makerCd);
		tv_cm_fstInspRsDt = (TextView) findViewById(R.id.tv_cm_fstInspRsDt);
		tv_cm_expireDt = (TextView) findViewById(R.id.tv_cm_expireDt);
		tv_cm_custPassDt = (TextView) findViewById(R.id.tv_cm_custPassDt);
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView)findViewById(R.id.btn_popClose);
		tv_cm_dongCarNo = (TextView)findViewById(R.id.tv_cm_dongCarNo);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
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

	private void searchElevInfo() {
		this.progress(true);
		new elevatorSearch().execute("bagicWorkTime");
	}

	private void setElevInfo() {
		tv_cm_carNo.setText(item02.getCarNo());
		tv_cm_carCd .setText(item02.getCarCd());
		tv_dm_modelNm.setText(item02.getModelNm());
		tv_cm_makerCd.setText(item02.getMakerCd());
		tv_cm_fstInspRsDt.setText(item02.getFstInspRsDt());
		tv_cm_expireDt.setText(item02.getExpireDt());
		tv_cm_custPassDt.setText(item02.getCustPassDt());
		tv_cm_dongCarNo.setText(item02.getDongCarNo());
	}

	public class elevatorSearch extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			item02 = null;
			try {
				item02 = new CM_SearchElevInfo_ITEM01(ej01.getValue("CAR_NO")
						,ej01.getValue("DONG_CAR_NO")
						,ej01.getValue("CAR_CD")
						,ej01.getValue("MODEL_NM")
						,ej01.getValue("MAKER_CD")
						,ej01.getValue("FST_INSP_RS_DT")
						,ej01.getValue("EXPIRE_DT")
						,ej01.getValue("CUST_PASS_DT")
						,ej01.getValue("CS_PASS_DT")
				);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			progress(false);
			setElevInfo();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {

					item02 = new CM_SearchElevInfo_ITEM01();
					String param_url = WebServerInfo.getUrl()
							+ "cm/searchElevatorInfo.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("pBldgNo", item01
							.getBldgNo()));
					arguments.add(new BasicNameValuePair("pCarNo", item01
							.getCarNo()));
					returnJson01 = http.getPost(param_url, arguments, true);

					try {
						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} catch (Exception ex) {
				}
				return params[0];
			}
			return "None";
		}
	}


	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SearchElevInfo.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_SearchElevInfo.this.progress.dismiss();
		}
	}
	private CM_SearchElev_ITEM01 currentSelectedItem01;
	private String pBldgNo;
	public String returnStr = "";

	public String getReturnStr() {
		return returnStr;
	}

	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
	}

}
