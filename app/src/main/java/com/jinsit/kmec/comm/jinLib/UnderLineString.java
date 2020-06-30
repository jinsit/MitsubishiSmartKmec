package com.jinsit.kmec.comm.jinLib;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class UnderLineString {
	
	public static SpannableString getUnderLineString(String msg){
		SpannableString spanString = new SpannableString(msg);
		spanString.setSpan(new UnderlineSpan(), 0, msg.length(), 0);
			return spanString;
		
	}
}
