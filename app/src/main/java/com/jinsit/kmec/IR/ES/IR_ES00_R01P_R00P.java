package com.jinsit.kmec.IR.ES;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class IR_ES00_R01P_R00P extends Dialog {

	private List<String> argList;
	private List<Integer> viewIdList;
	private int layOutId;
	
	public IR_ES00_R01P_R00P(Context context, List<String> argList, int layOutId, List<Integer> viewIdList) {
		super(context);
		this.argList = argList;
		this.layOutId = layOutId;
		this.viewIdList = viewIdList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layOutId);
		
		int argListSize = argList.size();
		for(int i=0; i<argListSize; i++){
			((TextView) findViewById(viewIdList.get(i))).setText( argList.get(i) );
		}
	}
}