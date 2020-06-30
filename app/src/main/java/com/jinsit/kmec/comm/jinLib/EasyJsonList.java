package com.jinsit.kmec.comm.jinLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class EasyJsonList {

	//Field
	JSONArray jsonList;

	//Constructor
	public EasyJsonList() {
	}
	public EasyJsonList(JSONArray jsonArray) {
		this.jsonList = jsonArray;
	}

	//Method
	public String getValue(int index, String str) throws JSONException{
		String retStr = "";
		JSONObject obj = (JSONObject) jsonList.get(index);
		try{
			if(!obj.isNull(str)){
				retStr=obj.getString(str);
			}
		}catch(Exception e){
			Log.v("[개발자Msg]", "[EasyJsonList] exception occurs as below");
			Log.v("", e.toString());
		}

		return retStr;
	};

	@Override
	public String toString() {
		return "EasyJsonList [jsonList=" + jsonList + "]";
	}

	public int getLength(){
		return jsonList.length();
	}



	//backup
	/*
	public String getValue(int index, String str) throws JSONException{
		String retStr = "";
		JSONObject obj = (JSONObject) jsonList.get(index);
		try{
			if(obj.getString(str)!=null)retStr=obj.getString(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return retStr;
	};
	*/
};
