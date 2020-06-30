package com.jinsit.kmec.WO.WT.TS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 고장수리
 */
public class WO_TS00_R03P extends AlertDialog implements OnClickListener, OnDismissListener, OnItemClickListener{

	public WO_TS00_R03P(Context context, String codeSt) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.codeSt = codeSt;
	}

	private Context context;
	private String codeSt;
	
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	
	
	private EasyJsonList ejl01;

	private ListView lv_fr_troubleCd;

	private List<WO_TS00_R03P_ITEM01> itemList01;
	private ProgressDialog progress;
	private WO_TS00_R03P_Adapter01 adapter01;
	private static String TAB_TROUBLE ="01";
	private static String TAB_SERVICE ="00";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_ts00_r03p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		if(codeSt.equals(TAB_TROUBLE)){
			tv01_popTitle.setText("고장 구분 코드");
		}else{
			tv01_popTitle.setText("일반 서비스 코드");
			
		}
		itemList01 = new ArrayList<WO_TS00_R03P_ITEM01>();
		currentselectedItem01 = null;
		
		
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		lv_fr_troubleCd = (ListView)findViewById(R.id.lv_fr_troubleCd);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		lv_fr_troubleCd.setOnItemClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_popClose:
			currentselectedItem01 =null;
			dismiss();
			break;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position > -1){
		currentselectedItem01 = itemList01.get(position);
		dismiss();
		}
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	


	public class selectTroubleServiceCode extends AsyncTask<String, Integer, String> {
		
		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		
		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ip/selectTroubleServiceCode.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("codeSt",codeSt));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					Log.v("RetrunJson",returnJson01.toString());
					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
						Log.v("RetrunJson",e.toString());
						
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
						itemList01.add(new WO_TS00_R03P_ITEM01(ejl01.getValue(i,"CODE_CD"), 
								ejl01.getValue(i, "CODE_NM")
								));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				adapter01 = new WO_TS00_R03P_Adapter01(context,
						R.layout.wo_ts00_r03p_adapter01, itemList01);
				WO_TS00_R03P.this.lv_fr_troubleCd
						.setAdapter(adapter01);
			}
		}
	}// end of SelectData inner-class
	
	public void inqueryTroubleServiceCode()
	{
		progress(true);
		new selectTroubleServiceCode().execute("bagicWorkTime");
	}
	
	private void progress(Boolean isActivated) {
		if (isActivated) {
			WO_TS00_R03P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WO_TS00_R03P.this.progress.dismiss();
		}
	}
	
	private WO_TS00_R03P_ITEM01 currentselectedItem01;

	public WO_TS00_R03P_ITEM01 getCurrentselectedItem(){
		return this.currentselectedItem01;
	}
	
}
