package com.jinsit.kmec.WO.WT.RJ;

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
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.SysUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class WT_RJ01_R01P extends AlertDialog
						  implements android.view.View.OnClickListener{

	//uiInstances
	private Context context;
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	private TextView tv01_rj01_r01p_totalPrice;
	private ListView lv01_rj01_r01p_dataList;
	
	//dto
	private Map<String, String> dbMap;
	
	//http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;
	private EasyJsonList ejl01;
	
	//utils
	private ProgressDialog progress;
	private ListAdapter adapter01;
	private List<WT_RJ01_R01P_Item01> itemList01;
	
	
	public WT_RJ01_R01P(Context context, Map<String, String> paraMap) {
		super(context);
		this.context = context;
		this.dbMap = paraMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_rj01_r01p);
		getInstances();
	}
	
	private void getInstances(){
		
		itemList01					= new ArrayList<WT_RJ01_R01P_Item01>();
		tv01_popTitle	 			= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose 				= (TextView) 	 findViewById(R.id.btn_popClose);
		tv01_rj01_r01p_totalPrice	= (TextView) findViewById(R.id.tv01_rj01_r01p_totalPrice);
		lv01_rj01_r01p_dataList		= (ListView) findViewById(R.id.lv01_rj01_r01p_dataList);
		setEvents();
	}
	private void setEvents(){
		btn_popClose.setOnClickListener(this);
		setConfig();
	}
	private void setConfig(){
		tv01_popTitle.setText("공사상세내역");
		
//		WT_RJ01_R01P.this.progress = 
//				  android.app.ProgressDialog.show(context, "알림","변경 중입니다...");
		new Database().execute("selectJobConfirmationSheet01");
		new Database().execute("selectJobConfirmationSheet02");
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
		
		if(div.equals("selectJobConfirmationSheet01")){
			
			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"ip/selectJobConfirmationSheet.do";
			
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("selTp" , "1"));
			arguments.add(new BasicNameValuePair("empId" , dbMap.get("empId").toString()));
			arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
			arguments.add(new BasicNameValuePair("jobNo" , dbMap.get("jobNo").toString()));
			
			returnJson = http.getPost(argUrl, arguments, true);
			
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
		}else if(div.equals("selectJobConfirmationSheet02")){
			
			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"ip/selectJobConfirmationSheet.do";
			
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("selTp" , "2"));
			arguments.add(new BasicNameValuePair("empId" , dbMap.get("empId").toString()));
			arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
			arguments.add(new BasicNameValuePair("jobNo" , dbMap.get("jobNo").toString()));
			
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
		
		if(div.equals("selectJobConfirmationSheet01")){
		
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					tv01_rj01_r01p_totalPrice.setText(SysUtil.makeStringWithComma(ejm02.getValue("AMT"),true));//통화적용
				}else if(isError){
					alert(ejm01.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if(div.equals("selectJobConfirmationSheet02")){
			
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){

					itemList01.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for(int i=0; i<jsonSize; i++){
					      itemList01.add(new WT_RJ01_R01P_Item01(  ejl01.getValue(i, "CAR_NO2")
																 , ejl01.getValue(i, "ITEM_NO") 
																 , ejl01.getValue(i, "ITEM_NM")
																 , ejl01.getValue(i, "UNIT_PRC")
																 , ejl01.getValue(i, "QTY")
																 , ejl01.getValue(i, "UNIT")
																 , ejl01.getValue(i, "AMT")
															    )
										);
					}
					
					adapter01 = new WT_RJ01_R01P_Adapter01(context, R.layout.wt_rj01_r01p_adapter01, itemList01);
					lv01_rj01_r01p_dataList.setAdapter(adapter01);
					
				}else if(isError){
					alert(ejm01.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}//WT_RJ01_R01P.this.progress.dismiss();
		}
	}
	
	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	
};