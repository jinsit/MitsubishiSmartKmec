package com.jinsit.kmec.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class HomeNaviPreference{

	//Field
	private SharedPreferences naviPreference;
	//Constructor
	public HomeNaviPreference(Context context) {
		super();
		naviPreference = context.getSharedPreferences("naviPreference", 0);
	}

	public void setHide(boolean isHide){
		SharedPreferences.Editor editor = naviPreference.edit();
			editor.putBoolean("isHide"	, isHide);
			editor.commit();
	}
	public boolean isHide(){
		boolean isHide;
		isHide = naviPreference.getBoolean("isHide", false);
		
		return isHide;
	}
	

	

	
};