package com.jinsit.kmec.comm.jinLib;

import java.util.List;

import com.jinsit.kmec.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SimpleDialogDetail extends AlertDialog {

	private List<String> argList;
	private List<Integer> viewIdList;
	private int layOutId;

	private TextView tv01_popTitle;
	private TextView btn_popClose;

	public SimpleDialogDetail(Context context, int layOutId, List<Integer> viewIdList, List<String> argList) {
		super(context);
		this.argList = argList;
		this.layOutId = layOutId;
		this.viewIdList = viewIdList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layOutId);

		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		tv01_popTitle.setText("상세정보");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);

		int argListSize = argList.size();
		for(int i=0; i<argListSize; i++){
			((TextView) findViewById(viewIdList.get(i))).setText( argList.get(i) );
		}

		//Button btn = (Button) findViewById( viewIdList.get(argListSize) );
		btn_popClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});

	}

};
