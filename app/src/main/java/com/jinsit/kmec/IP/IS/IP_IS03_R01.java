package com.jinsit.kmec.IP.IS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CalendarGridNoDataActivity;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;

public class IP_IS03_R01 extends Activity{


	//UI-Instances
	private Context context;
	private ListView lv01_is03_jobList;

	//related adapter
	private List<IP_IS03_R01_Item01> itemList01;
	private IP_IS03_R01_Adapter01 adapter01;

	//http
	JSONObject returnJson;
	EasyJsonList ejl01;
	EasyJsonMap ejm01;

	//utils
	private ProgressDialog progress;
	private Map<String, String> argMap;
	private CommonSession cs;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_is03_r01);

		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("점검계획수정");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();

		Intent intent = getIntent();
		intent.getStringExtra("csEmpId");
		intent.getStringExtra("workDt");
		progress(true);
		new Database().execute( "selectJobList"
				,intent.getStringExtra("csEmpId")
				,intent.getStringExtra("workDt")
		);
		setEvents();
	}


	private void getInstances(){
		context = this;
		argMap = new HashMap<String, String>();
		itemList01 = new ArrayList<IP_IS03_R01_Item01>();
		lv01_is03_jobList = (ListView) findViewById(R.id.lv01_is03_jobList);
	}
	private void setEvents(){
		setConfig();
	}
	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}
	private class Database extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			crud( params[0],params[1],params[2] );
			return params[0];
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
			progress(false);
		}
	}



	private void crud(String div, String v1, String v2){

		if(div.equals("selectJobList")){

			GetHttp http = new GetHttp();
			String url = WebServerInfo.getUrl()+"ip/selectJobList.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", v1));
			arguments.add(new BasicNameValuePair("workYMD", v2));
			returnJson =  http.getPost(url, arguments, true);

			try {
				ejl01 = new EasyJsonList(returnJson.getJSONArray("dataList"));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else if(div.equals("updateInspectionInfo")){

			GetHttp http = new GetHttp();
			String url = WebServerInfo.getUrl()+"ip/updateInspectionInfo.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", argMap.get("csEmpId")));
			arguments.add(new BasicNameValuePair("presentWorkYMD", argMap.get("presentWorkYMD")));
			arguments.add(new BasicNameValuePair("jobNo", argMap.get("jobNo")));
			arguments.add(new BasicNameValuePair("newWorkYMD", argMap.get("newWorkYMD")));
			arguments.add(new BasicNameValuePair("userId", argMap.get("userId")));

			returnJson =  http.getPost(url, arguments, true);

			try {
				ejm01 = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void setData(String div){

		if(div.equals("selectJobList")){

			try {

				itemList01.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for(int i=0; i<jsonSize; i++){
					itemList01.add(new IP_IS03_R01_Item01(
							ejl01.getValue(i, "WORK_DT")
							, ejl01.getValue(i, "BLDG_NM")
							, ejl01.getValue(i, "JOB_ST_NM")
							, ejl01.getValue(i, "CS_TM_FR")
							, ejl01.getValue(i, "T_CNT").equals("0") ? "" : "점검상황"
							, ejl01.getValue(i, "T_CNT").equals("0") ? "" : ejl01.getValue(i, "Y_CNT")
							, ejl01.getValue(i, "T_CNT").equals("0") ? "" : "/"+ejl01.getValue(i, "T_CNT")
							, ejl01.getValue(i, "CAR_NO"))
					);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}


			adapter01 = new IP_IS03_R01_Adapter01(context, R.layout.ip_is03_r01_adapter01, itemList01);
			lv01_is03_jobList.setAdapter(adapter01);


			lv01_is03_jobList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
										int index, long arg3) {

					try {
						argMap.clear();
						argMap.put("csEmpId"		, ejl01.getValue(index, "CS_EMP_ID"));
						argMap.put("presentWorkYMD" , ejl01.getValue(index, "WORK_DT"));
						argMap.put("jobNo"			, ejl01.getValue(index, "JOB_NO"));
						argMap.put("userId"			, ejl01.getValue(index, "CS_EMP_ID"));

						Intent intent = new Intent(context, CalendarGridNoDataActivity.class);
						startActivityForResult(intent, 99);

					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			});

		}else if(div.equals("updateInspectionInfo")){

			try {

				boolean isError = ejm01.getValue("errorCd").equals("0") ? false : true;
				if(!isError){

					String rtn = ejm01.getValue("RTN");
					if(rtn.equals("1")){
						AlertView.alert(context, "", "성공적으로 수정되었습니다.", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								refreshThis();
							}
						});
					}else if(rtn.equals("0")){
						alert("수정되지 않았습니다.", context);
					}

				}else if(isError){
					alert(ejm01.getValue("errorMsg"), context);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}//end of setData()

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode) {
			case 99:
				if (resultCode == RESULT_OK) {
					Bundle bundle   = data.getExtras();
					String newWorkYMD  = bundle.getString("dateSelected");

					argMap.put("newWorkYMD", newWorkYMD);
					String message  = "기존일자 : " + argMap.get("presentWorkYMD") +"\n"
							+ "수정일자 : " + newWorkYMD +"\n\n"
							+ "수정하시겠습니까?"
							;

					AlertView.confirm(context, "", message, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							progress(true);
							new Database().execute("updateInspectionInfo","","");

						}
					});

				}
				break;

		}//end of switch()

	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent intent = new Intent(this,IP_IS03_R00.class);
		intent.putExtra("from", "IP_IS03_R01");
		setResult(88, intent);
		finish();
	}


	private void refreshThis(){
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			IP_IS03_R01.this.progress =
					android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		}else{
			IP_IS03_R01.this.progress.dismiss();
		}
	}

};