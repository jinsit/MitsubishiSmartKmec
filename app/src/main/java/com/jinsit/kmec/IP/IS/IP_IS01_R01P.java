package com.jinsit.kmec.IP.IS;

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
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IP.JS.IP_JS00_R00;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IP_IS01_R01P extends AlertDialog {


	//uiInstances
	private Context context;
	private TextView tv01_is_p01_bldgNo;
	private TextView tv02_is_p01_bldgNm;
	private TextView tv03_is_p01_contrTpCd;
	private TextView tv04_is_p01_contrTpNm;
	private TextView tv05_is_p01_jobFigureStatus;
	private TextView tv06_is_p01_endVal;
	private TextView tv07_is_p01_kind01;
	private TextView tv08_is_p01_kind02;
	private TextView tv09_is_p01_kind03;
	private ListView lv01_is_p01;
	private TextView btn_popClose;
	private TextView tv01_popTitle;

	//arguments
	Map argMap;

	//Http
	private EasyJsonMap ejm;
	private EasyJsonList ejl;

	//utils
	private ProgressDialog progress;

	//adapter
	private JSONObject returnJson;
	private List<IP_IS01_R01P_Item01> itemList;
	private ListAdapter adpater;



	protected IP_IS01_R01P(Context context, Map argMap) {
		super(context);
		this. argMap = argMap;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_is01_r01p);
		getInstances();
		progress(true);
		new SelectData().execute( argMap.get("selTp").toString()
				,argMap.get("csEmpId").toString()
				,argMap.get("workMm").toString()
				,argMap.get("bldgNo").toString()
		);
		new SelectData().execute( "2" //전체
				,argMap.get("csEmpId").toString()
				,argMap.get("workMm").toString()
				,argMap.get("bldgNo").toString()
		);
		tv01_popTitle.setText("상세내역");
	}

	private void getInstances(){

		context 					= getContext();
		itemList 					= new ArrayList<IP_IS01_R01P_Item01>();
		tv01_is_p01_bldgNo			= (TextView) findViewById(R.id.tv01_is_p01_bldgNo);
		tv02_is_p01_bldgNm			= (TextView) findViewById(R.id.tv02_is_p01_bldgNm);
		tv03_is_p01_contrTpCd		= (TextView) findViewById(R.id.tv03_is_p01_contrTpCd);
		tv04_is_p01_contrTpNm		= (TextView) findViewById(R.id.tv04_is_p01_contrTpNm);
		tv05_is_p01_jobFigureStatus = (TextView) findViewById(R.id.tv05_is_p01_jobFigureStatus);
		tv06_is_p01_endVal			= (TextView) findViewById(R.id.tv06_is_p01_endVal);
		tv07_is_p01_kind01			= (TextView) findViewById(R.id.tv07_is_p01_kind01);
		tv08_is_p01_kind02			= (TextView) findViewById(R.id.tv08_is_p01_kind02);
		tv09_is_p01_kind03			= (TextView) findViewById(R.id.tv09_is_p01_kind03);
		lv01_is_p01					= (ListView) findViewById(R.id.lv01_is_p01);
		btn_popClose				= (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle				= (TextView) findViewById(R.id.tv01_popTitle);
		setEvents();
	}


	private void setEvents(){

		tv07_is_p01_kind01.setOnClickListener(listener);
		tv08_is_p01_kind02.setOnClickListener(listener);
		tv09_is_p01_kind03.setOnClickListener(listener);
		btn_popClose.setOnClickListener(listener);
	}


	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			tv07_is_p01_kind01.setBackgroundResource(R.drawable.tab_bttom_btn_on);
			tv08_is_p01_kind02.setBackgroundResource(R.drawable.tab_bttom_btn_on);
			tv09_is_p01_kind03.setBackgroundResource(R.drawable.tab_bttom_btn_on);
			tv07_is_p01_kind01.setEnabled(true);
			tv08_is_p01_kind02.setEnabled(true);
			tv09_is_p01_kind03.setEnabled(true);


			switch (v.getId()) {
				case R.id.tv07_is_p01_kind01:progress(true);
					tv07_is_p01_kind01.setBackgroundResource(R.drawable.tab_bttom_btn_off);
					tv07_is_p01_kind01.setEnabled(false);
					new SelectData().execute( "2"
							,argMap.get("csEmpId").toString()
							,argMap.get("workMm").toString()
							,argMap.get("bldgNo").toString()
					);
					break;
				case R.id.tv08_is_p01_kind02:progress(true);
					tv08_is_p01_kind02.setBackgroundResource(R.drawable.tab_bttom_btn_off);
					tv08_is_p01_kind02.setEnabled(false);
					new SelectData().execute( "3"
							,argMap.get("csEmpId").toString()
							,argMap.get("workMm").toString()
							,argMap.get("bldgNo").toString()
					);

					break;
				case R.id.tv09_is_p01_kind03:progress(true);
					tv09_is_p01_kind03.setBackgroundResource(R.drawable.tab_bttom_btn_off);
					tv09_is_p01_kind03.setEnabled(false);
					new SelectData().execute( "4"
							,argMap.get("csEmpId").toString()
							,argMap.get("workMm").toString()
							,argMap.get("bldgNo").toString()
					);
					break;
				case R.id.btn_popClose:
					cancel();
					break;
				default :
					System.out.println("[개발자Msg] out of range of CASE");
					break;
			}
		}
	};


	class SelectData extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {

			GetHttp http = new GetHttp();
			String url = WebServerInfo.getUrl()+"ip/selectDetailsOnInspectionMgt.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("selTp"   , params[0]));
			arguments.add(new BasicNameValuePair("csEmpId" , params[1]));
			arguments.add(new BasicNameValuePair("workMm"  , params[2]));
			arguments.add(new BasicNameValuePair("bldgNo"  , params[3]));

			//Http
			returnJson = http.getPost(url, arguments, true);

			//Map(header:1)
			if (params[0].equals("1")) {
				try {
					ejm = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//list(전체:2,계획:3,완료:4)
			}else{
				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return params[0];
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(result.equals("1")){
				setData(result);
			}else{
				setData(result);
			}
			progress(false);
		}

	}


	private void setData(String result){

		if(result.equals("1")){

			try {

				tv01_is_p01_bldgNo.setText(ejm.getValue("BLDG_NO"));
				tv02_is_p01_bldgNm.setText(ejm.getValue("BLDG_NM"));
				tv03_is_p01_contrTpCd.setText(ejm.getValue("CONTR_TP"));
				tv04_is_p01_contrTpNm.setText(ejm.getValue("CONTR_TP_NM"));
				tv05_is_p01_jobFigureStatus.setText(ejm.getValue("D_CNT")+"/"+ejm.getValue("T_CNT") + " ");
				tv06_is_p01_endVal.setText(ejm.getValue("END_VAL"));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{

			try{
				itemList.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for(int i=0; i<jsonSize; i++){
					itemList.add(new IP_IS01_R01P_Item01(  ejl.getValue(i, "CAR_NO")
									, ejl.getValue(i, "JOB_ST_NM")
									, ejl.getValue(i, "PLAN_WORK_DT")
									, ejl.getValue(i, "WORK_DT")
									, ejl.getValue(i, "EMP_NM")
							)
					);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			adpater = new IP_IS01_R01P_Adapter01(context, R.layout.ip_is01_r01p_adapter01, itemList);
			lv01_is_p01.setAdapter(adpater);

		}

	}


	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean shallActivated){
		if(shallActivated){
			IP_IS01_R01P.this.progress =
					android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		}else{
			IP_IS01_R01P.this.progress.dismiss();
		}
	}

};
