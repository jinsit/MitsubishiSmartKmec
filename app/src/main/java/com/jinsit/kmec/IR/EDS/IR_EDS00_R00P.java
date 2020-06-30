package com.jinsit.kmec.IR.EDS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ListViewUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IR_EDS00_R00P extends AlertDialog implements
		android.view.View.OnClickListener, OnItemClickListener {

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////

	private ListView lv_lawEds;
	private ListView lv_kmecEds;
	private ListView lv_overseaEds;

	private String empId;

	private List<IR_EDS00_R00P_ITEM00> itemList01;
	private List<IR_EDS00_R00P_ITEM01> itemList02;
	private List<IR_EDS00_R00P_ITEM01> itemList03;

	protected IR_EDS00_R00P(Context context, String empId) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.empId = empId;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_eds00_r00p);
		activityInit();
	}

	protected void activityInit() {
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		this.tv01_popTitle.setText("상세정보");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		this.btn_popClose.setOnClickListener(this);

		this.lv_lawEds = (ListView)findViewById(R.id.lv_lawEds);
		this.lv_kmecEds = (ListView)findViewById(R.id.lv_kmecEds);
		this.lv_overseaEds = (ListView)findViewById(R.id.lv_overseaEds);
		this.lv_lawEds.setOnItemClickListener(this);
		this.lv_kmecEds.setOnItemClickListener(this);
		this.lv_overseaEds.setOnItemClickListener(this);

		new SelectEducationStatusAsync().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// /title 위젯
			case R.id.btn_popClose:
				this.dismiss();
				break;
			// //////////////////
			default:
				break;
		}
	}


	private void setListData(List<IR_EDS00_R00P_ITEM00> itemList01, List<IR_EDS00_R00P_ITEM01> itemList02, List<IR_EDS00_R00P_ITEM01> itemList03){

		IR_EDS00_R00P_Adapter00 adapter01 = new IR_EDS00_R00P_Adapter00(context,  itemList01);
		IR_EDS00_R00P_Adapter01 adapter02 = new IR_EDS00_R00P_Adapter01(context,  itemList02);
		IR_EDS00_R00P_Adapter01 adapter03 = new IR_EDS00_R00P_Adapter01(context,  itemList03);
		this.lv_lawEds.setAdapter(adapter01);
		ListViewUtil.listViewHeightSet(adapter01, this.lv_lawEds);
		//this.lv_kmecEds.setAdapter(adapter02);
		//this.lv_overseaEds.setAdapter(adapter03);
		this.lv_kmecEds.setAdapter(adapter02);
		ListViewUtil.listViewHeightSet(adapter02, this.lv_kmecEds);
		this.lv_overseaEds.setAdapter(adapter03);
		ListViewUtil.listViewHeightSet(adapter03, this.lv_overseaEds);
	}


	public class SelectEducationStatusAsync extends AsyncTask<Void, Void, Boolean> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;
		JSONObject returnJson03;

		EasyJsonList ejl01;
		EasyJsonList ejl02;
		EasyJsonList ejl03;

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				String param_url_01 = WebServerInfo.getUrl()
						+ "ir/selectEducationStatus.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp","1"));
				arguments.add(new BasicNameValuePair("empId",empId));
				returnJson01 = http.getPost(param_url_01, arguments, true);


				arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp","2"));
				arguments.add(new BasicNameValuePair("empId",empId));
				returnJson02 = http.getPost(param_url_01, arguments, true);


				arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp","3"));
				arguments.add(new BasicNameValuePair("empId",empId));
				returnJson03 = http.getPost(param_url_01, arguments, true);



				try {
					ejl01 = new EasyJsonList(returnJson01.getJSONArray("dataList"));
					ejl02 = new EasyJsonList(returnJson02.getJSONArray("dataList"));
					ejl03 = new EasyJsonList(returnJson03.getJSONArray("dataList"));
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;

		}


		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {

				try {
					itemList01 = new ArrayList<IR_EDS00_R00P_ITEM00>();
					itemList02 = new ArrayList<IR_EDS00_R00P_ITEM01>();
					itemList03 = new ArrayList<IR_EDS00_R00P_ITEM01>();

					int jsonSize01 = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList01.add(new IR_EDS00_R00P_ITEM00(
								ejl01.getValue(i,"LICENSE_NO")
								,ejl01.getValue(i, "LICENSE_NM")
								,ejl01.getValue(i, "APPROVE_MON")
								,ejl01.getValue(i, "EDU_TO_DT")
								,ejl01.getValue(i, "NUM")));
					}
					int jsonSize02 = returnJson02.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize02; i++) {
						itemList02.add(new IR_EDS00_R00P_ITEM01(
								ejl02.getValue(i,"EDU_NM")
								, ejl02.getValue(i, "APPLY_CAR_CD")
								,ejl02.getValue(i, "EDU_TO_DT")
								,ejl02.getValue(i, "SCORE")
								,ejl02.getValue(i, "NUM")));
					}

					int jsonSize03 = returnJson03.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize03; i++) {
						itemList03.add(new IR_EDS00_R00P_ITEM01(
								ejl03.getValue(i,"EDU_NM")
								, ejl03.getValue(i, "APPLY_CAR_CD")
								,ejl03.getValue(i, "EDU_TO_DT")
								,ejl03.getValue(i, "SCORE")
								,ejl03.getValue(i, "NUM")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setListData(itemList01,itemList02,itemList03);

			}else{
				//실패했습니다.
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub

	}

}
