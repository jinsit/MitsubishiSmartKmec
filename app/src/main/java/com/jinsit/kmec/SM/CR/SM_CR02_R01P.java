package com.jinsit.kmec.SM.CR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SM_CR02_R01P extends AlertDialog
		implements android.view.View.OnClickListener{

	//uiInstances
	private Context context;
	private TextView tv01_popTitle;
	private TextView	 btn_popClose;
	private TextView tv01_cr02_bldgNm;
	private TextView tv02_cr02_carNo;
	private TextView tv03_cr02_dateToShip;
	private TextView tv04_cr02_dateToEnd;
	private TextView tv05_cr02_dateToOccur;
	private TextView tv06_cr02_claimDegree;
	private TextView tv07_cr02_dateToFix;
	private TextView tv08_cr02_svcType;
	private TextView tv09_cr02_status;
	private TextView tv10_cr02_expectedReason;
	private TextView tv11_cr02_demands;
	private TextView tv12_cr02_sthSpecial;
	private TextView btn02_cr02_seePhotos;

	//http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;

	//dto
	private Map<String, String> paraMap;

	protected SM_CR02_R01P(Context context, Map<String, String> paraMap) {
		super(context);
		this.paraMap = paraMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sm_cr02_r01p);
		getInstances();
		setConfig();
		new Database().execute("selectDetailsForClaim");
	}

	private void getInstances(){

		context 					= getContext();
		tv01_popTitle 				= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose 				= (TextView) findViewById(R.id.btn_popClose);
		tv01_cr02_bldgNm			= (TextView) findViewById(R.id.tv01_cr02_bldgNm_value);
		tv02_cr02_carNo				= (TextView) findViewById(R.id.tv02_cr02_carNo_value);
		tv03_cr02_dateToShip		= (TextView) findViewById(R.id.tv03_cr02_dateToShip_value);
		tv04_cr02_dateToEnd			= (TextView) findViewById(R.id.tv04_cr02_dateToEnd_value);
		tv05_cr02_dateToOccur		= (TextView) findViewById(R.id.tv05_cr02_dateToOccur_value);
		tv06_cr02_claimDegree		= (TextView) findViewById(R.id.tv06_cr02_claimDegree);
		tv07_cr02_dateToFix			= (TextView) findViewById(R.id.tv07_cr02_dateToFix_value);
		tv08_cr02_svcType			= (TextView) findViewById(R.id.tv08_cr02_svcType);
		tv09_cr02_status			= (TextView) findViewById(R.id.tv09_cr02_status_value);
		tv10_cr02_expectedReason	= (TextView) findViewById(R.id.tv10_cr02_expectedReason_value);
		tv11_cr02_demands			= (TextView) findViewById(R.id.tv11_cr02_demands_value);
		tv12_cr02_sthSpecial		= (TextView) findViewById(R.id.tv12_cr02_sthSpecial_value);
		btn02_cr02_seePhotos 		= (TextView) findViewById(R.id.btn02_cr02_seePhotos);
		setEvents();
	}

	private void setConfig(){
		tv01_popTitle.setText("Claim 상세조회");
		btn02_cr02_seePhotos.setEnabled(false);
	}

	private void setEvents(){
		btn_popClose.setOnClickListener(this);
		btn02_cr02_seePhotos.setOnClickListener(this);
	}

	private class Database extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return null;
		}
		protected void onPostExecute(String result) {
			setData(result);
		};
	}

	private void crud(String div){

		GetHttp http = null;
		String argUrl = "";

		if(div.equals("selectDetailsForClaim")){
			http = new GetHttp();
			argUrl = WebServerInfo.getUrl()+"sm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("ngNo", paraMap.get("ngNo") ));
			arguments.add(new BasicNameValuePair("ngSr", paraMap.get("ngSr") ));
//				arguments.add(new BasicNameValuePair("ngNo", "C100129-004" ));
//				arguments.add(new BasicNameValuePair("ngSr", "001" ));

			returnJson = http.getPost(argUrl, arguments, true);

			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void setData(String result){
		try {

			tv01_cr02_bldgNm.setText(ejm02.getValue("PROJECT_NM"));
			tv02_cr02_carNo.setText(ejm02.getValue("CAR_NO"));
			tv03_cr02_dateToShip.setText(ejm02.getValue("SHIP_DT"));
			tv04_cr02_dateToEnd.setText(ejm02.getValue("TRANS_DT"));
			tv05_cr02_dateToOccur.setText(ejm02.getValue("OCCUR_DT"));
			tv06_cr02_claimDegree.setText(ejm02.getValue("COMPLAINT_CD_NM"));
			tv07_cr02_dateToFix.setText(ejm02.getValue("DEMAND_DT"));
			tv08_cr02_svcType.setText(ejm02.getValue("PAY_TP_NM"));
			tv09_cr02_status.setText(ejm02.getValue("Q_DESC"));
			tv10_cr02_expectedReason.setText(ejm02.getValue("Q_CAUSE"));
			tv11_cr02_demands.setText(ejm02.getValue("Q_DEMAND"));
			tv12_cr02_sthSpecial.setText(ejm02.getValue("Q_REMARK"));

			boolean hasImgs = Integer.parseInt( ejm02.getValue("IMG_CNT") ) > 0 ? true : false;
			if(hasImgs){
				btn02_cr02_seePhotos.setText("사진보기 "+ejm02.getValue("IMG_CNT")+"장 첨부됨");
				btn02_cr02_seePhotos.setEnabled(hasImgs);
				paraMap.put("imgCnt", ejm02.getValue("IMG_CNT"));
				btn02_cr02_seePhotos.setEnabled(true);
			}else if(!hasImgs){
				btn02_cr02_seePhotos.setText("사진없음");
				btn02_cr02_seePhotos.setEnabled(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
			case R.id.btn02_cr02_seePhotos:
				SM_CR02_R02P d02 = new SM_CR02_R02P(context,paraMap);
				d02.show();
				d02.sch();
				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}
	}

};
