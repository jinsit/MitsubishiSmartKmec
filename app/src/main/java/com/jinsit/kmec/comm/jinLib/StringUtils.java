package com.jinsit.kmec.comm.jinLib;

import android.widget.TextView;

public class StringUtils {

	
	public static boolean hasBlank(TextView tv){
		String trimedStr = (tv.getText().toString() ).trim();
		return trimedStr.isEmpty() ? true : false;
	}
	
	public static boolean hasBlank(String str){
		return ( str.trim() ).isEmpty() ? true : false;
	}
	
	
	
};
