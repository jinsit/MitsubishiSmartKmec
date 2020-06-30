package com.jinsit.kmec.IP.IS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IP_IS02_R02P extends AlertDialog {

	private Context context;
	private Map paraMap;


	//uiInstances
	TextView tv01_popTitle;
	TextView btn_popClose;
	TextView tv01_is02_p02_workDt;
	TextView tv02_is02_p02_yCnt;
	TextView tv03_is02_p02_bldgNo_Nm;
	TextView tv04_is02_p02_carNo_CTN_ITBN;


	protected IP_IS02_R02P(Context context, Map paraMap) {
		super(context);
		this.context = context;
		this.paraMap = paraMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_is02_r02p);
		getInstances();
		tv01_popTitle.setText("작업대상정보");
		setData(paraMap);
	}

	private void getInstances(){

		context = getContext();
		tv01_popTitle 				 = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose 				 = (TextView) findViewById(R.id.btn_popClose);
		tv01_is02_p02_workDt		 = (TextView) findViewById(R.id.tv01_is02_p02_workDt);
		tv02_is02_p02_yCnt			 = (TextView) findViewById(R.id.tv02_is02_p02_yCnt);
		tv03_is02_p02_bldgNo_Nm		 = (TextView) findViewById(R.id.tv03_is02_p02_bldgNo_Nm);
		tv04_is02_p02_carNo_CTN_ITBN = (TextView) findViewById(R.id.tv04_is02_p02_carNo_CTN_ITBN);
		setEvents();
	}

	private void setEvents(){
		btn_popClose.setOnClickListener(listener);
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_popClose:
					cancel();
					break;
				default:
					System.out.println("[개발자Msg] out of range of CASE");
					break;
			}

		}
	};


	private void setData(Map paraMap){

		tv01_is02_p02_workDt.setText(paraMap.get("workDt").toString());
		tv02_is02_p02_yCnt.setText(paraMap.get("yCnt").toString());
		tv03_is02_p02_bldgNo_Nm.setText(  paraMap.get("bldgNo").toString()
				+ "\n" + paraMap.get("bldgNm").toString()
		);
		tv04_is02_p02_carNo_CTN_ITBN.setText( paraMap.get("carNo").toString() + "호기 "
				+ paraMap.get("contrTpNm").toString() + "  "
				+ paraMap.get("inspTimeBcNm").toString()
		);
	}

};
