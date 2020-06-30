package com.jinsit.kmec.HM.MP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class HM_MP00_R01P extends AlertDialog
		implements android.view.View.OnClickListener{

	//uiInstances
	private Context context;
	private ListView lv01_mp00_r01_dataList;
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	//http
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;
	private JSONObject returnJson;

	//utils
	private ListAdapter adapter01;
	private ProgressDialog progress;
	private List<HM_MP00_R01_Item01> itemList01;
	private CommonSession session;

	//dto
	private Map paraMap;

	public HM_MP00_R01P(Context context, Map paraMap) {
		super(context);
		this.context = context;
		this.paraMap = paraMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hm_mp00_r01p);
		getInstances();
	}

	private void getInstances(){

		session				   = new CommonSession(context);
		itemList01			   = new ArrayList<HM_MP00_R01_Item01>();
		tv01_popTitle		   = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose	  	   = (TextView) findViewById(R.id.btn_popClose);
		lv01_mp00_r01_dataList = (ListView) findViewById(R.id.lv01_mp00_r01_dataList);
		setEvents();
	}
	private void setEvents(){
		btn_popClose.setOnClickListener(this);
		setConfig();
	}
	private void setConfig(){
		tv01_popTitle.setText("준비물 목록");
		new Database().execute("selectToolList");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}

	}


	private class Database extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
		}
	}
	private void crud(String div){

		if(div.equals("selectToolList")){

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"hm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("csEmpId", session.getEmpId() ));
			arguments.add(new BasicNameValuePair("workDt" , paraMap.get("workDt").toString() ));

			returnJson = http.getPost(argUrl, arguments, true);

			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejl01 = new EasyJsonList( returnJson.getJSONArray("dataList") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	private void setData(String div){

		if(div.equals("selectToolList")){

			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){

					itemList01.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for(int i=0; i<jsonSize; i++){
						itemList01.add(new HM_MP00_R01_Item01(  ejl01.getValue(i, "CS_TOOLS") ) );
					}

					adapter01 = new HM_MP00_R01_Adapter01(context, R.layout.hm_mp00_r01p_adapter01, itemList01);
					lv01_mp00_r01_dataList.setAdapter(adapter01);

				}else if(isError){
					alert(ejm01.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
};
