package com.jinsit.kmec.WO.WT.RI;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class CommentPreference{

	//Field
	private SharedPreferences commentPreference;
	//Constructor
	public CommentPreference(Context context) {
		super();
		commentPreference = context.getSharedPreferences("commentPreference", 0);
	}
	public void setComment(String comment){
		SharedPreferences.Editor editor = commentPreference.edit();
		//editor.putString("isHide"	, isHide);
		editor.putString("comment", comment);
		editor.commit();
	}
	
	public String getComment(){
		String comment = null;
		comment = commentPreference.getString("comment", comment);
		return comment;
		
	}
	
	

	

	
};