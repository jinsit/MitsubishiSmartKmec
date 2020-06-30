package com.jinsit.kmec.IR.CD;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IR_CD02_R00P extends AlertDialog implements OnItemClickListener, android.view.View.OnClickListener
{
	GetHttp getHttp;
	private ProgressDialog ProgressDialog;
	Context context;

	ArrayList<IR_CD02_R00_ITEM00> rfResData;
	IR_CD00_R00ReqData reqData;

	IR_CD02_R00_Adapter00 rfListAdapter;

	private EasyJsonList ejl;
	//---Widget---///
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	ListView lv_cbsData;
	String title;

	protected IR_CD02_R00P(Context context, IR_CD00_R00ReqData item, ArrayList<IR_CD02_R00_ITEM00> items) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.reqData =item;
		this.rfResData = items;
		//new RepeatedFailureAsync().execute();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_cd02_r01p);

		activityInit();

	}

	private void activityInit() {
		// TODO Auto-generated method stub

		tv01_popTitle= (TextView) findViewById(R.id.tv01_popTitle);
		title = "호기반복고장";
		if(reqData.getCountSt().equals("5")){
			title =  "부위반복고장";
		}
		tv01_popTitle.setText(title);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		this.btn_popClose.setOnClickListener(this);
		lv_cbsData = (ListView) findViewById(R.id.lv_cbsRFData);
		lv_cbsData.setOnItemClickListener(this);

		TextView bldgNo = (TextView)findViewById(R.id.tv_cbs02HeaderBldgNo);
		TextView bldgNm = (TextView)findViewById(R.id.tv_cbs02HeaderBldgNm);
		TextView carNo = (TextView)findViewById(R.id.tv_cbs02HeaderCarNo);
		bldgNo.setText(rfResData.get(0).getBLDG_NO());
		bldgNm.setText(rfResData.get(0).getBLDG_NM());
		carNo.setText(rfResData.get(0).getCAR_NO());

		rfListAdapter = new IR_CD02_R00_Adapter00(context, rfResData);
		lv_cbsData.setAdapter(rfListAdapter);
		//new RepeatedFailureAsync().execute();
	}





	public class RepeatedFailureAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_CD02_R00P.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "조회중 입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				rfResData = new ArrayList<IR_CD02_R00_ITEM00>();

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cd/selectRepeatedFailure.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", reqData.getSelTp()));
				arguments.add(new BasicNameValuePair("selCd", reqData.getSelCd()));
				arguments.add(new BasicNameValuePair("recevDtFr", reqData.getRecevDtFr()));
				arguments.add(new BasicNameValuePair("recevDtTo", reqData.getRecevDtTo()));
				arguments.add(new BasicNameValuePair("countSt", reqData.getCountSt()));
				arguments.add(new BasicNameValuePair("rCount", reqData.getrCount()));
				arguments.add(new BasicNameValuePair("csBldgNo", reqData.getCsBldgNo()));
				arguments.add(new BasicNameValuePair("csCarNo", reqData.getCsCarNo()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					rfResData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						rfResData.add(new IR_CD02_R00_ITEM00(ejl.getValue(i,
								"BLDG_NO"), ejl.getValue(i, "BLDG_NM"), ejl
								.getValue(i, "CAR_NO"), ejl.getValue(i,
								"DONG_CAR_NO"), ejl.getValue(i, "CAR_CD"), ejl
								.getValue(i, "MODEL_NM"), ejl.getValue(i,
								"RECEV_TM"), ejl.getValue(i, "ARRIVE_TM"), ejl
								.getValue(i, "COMPLETE_TM"), ejl.getValue(i,
								"RESERV_TM"), ejl.getValue(i, "CONTACT_CD"),
								ejl.getValue(i, "RECEV_DESC"), ejl.getValue(i,
								"STATUS_CD"), ejl.getValue(i,
								"CBS_CD_1"), ejl
								.getValue(i, "CBS_CD_2"), ejl.getValue(
								i, "CBS_CD_3"), ejl.getValue(i,
								"FAULT_CD"),
								ejl.getValue(i, "PROC_CD"), ejl.getValue(i,
								"DUTY_CD"), ejl.getValue(i,
								"RECEV_NO")));
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
			IR_CD02_R00P.this.ProgressDialog.dismiss();


		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		IR_CD02_R00_ITEM00 mData = rfResData.get(position);
		IR_CD02_R01P cd01 = new IR_CD02_R01P(context, mData);
		cd01.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			///title 위젯
			case R.id.btn_popClose:
				this.dismiss();
				break;
			////////////////////
			default:
				break;
		}
	}

}