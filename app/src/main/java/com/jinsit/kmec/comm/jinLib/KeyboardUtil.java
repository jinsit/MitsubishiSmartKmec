package com.jinsit.kmec.comm.jinLib;

import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtil {
	
	public static Handler hideKeyboard(EditText et,  InputMethodManager in)
	{
		final EditText editText = et;
		final InputMethodManager imm = in;
		return  new Handler() {
			@Override
			public void handleMessage(Message msg) {
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			}
		};
	
	}
	
	
}
