package com.jinsit.kmec.CM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.HM.MP.HM_MP00_R00;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_GateKeeper{

	private EasyJsonMap ejm01;
	private JSONObject returnJson;
	
	
	public String checkIfLogInOut(){
		String answer = null;
		answer = crud("checkIfLoginOrNot");
		return answer;
	}
	
	private String crud(String div){
		
		String result = null;
		
		if(div.equals("checkIfLoginOrNot")){
			
			GetHttp http = new GetHttp();
			String url = WebServerInfo.getUrl()+"cm/"+div+".do";
			
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("usrId" , ""));
			arguments.add(new BasicNameValuePair("workDt", ""));
			
			//Http
			returnJson = http.getPost(url, arguments, true);
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				boolean isError = ejm01.getValue("errorCd").equals("0") ? false : true;
				if(!isError){
					result = ejm01.getValue("RTN");
				}else if(isError){
					result = ejm01.getValue("errorMsg");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
};