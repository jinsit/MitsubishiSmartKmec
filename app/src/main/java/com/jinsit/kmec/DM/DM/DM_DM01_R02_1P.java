package com.jinsit.kmec.DM.DM;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class DM_DM01_R02_1P extends AlertDialog implements
		android.view.View.OnClickListener {


	private Context context;
	private TextView btn_dm_close;
	private TextView tv_dm_otRmk;
	private DM_DM01_R00_ITEM03 item;

	private TextView tv01_popTitle;
	private TextView btn_popClose;


	public  DM_DM01_R02_1P(Context context, DM_DM01_R00_ITEM03 item) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item =item;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm01_r02_1p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		this.tv_dm_otRmk.setText(this.item.getOtRemark());
		this.tv01_popTitle.setText("비고/상세");
	}
	protected void getInstances() {

		tv_dm_otRmk = (TextView) findViewById(R.id.tv_dm_otRmk);

		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle =(TextView) findViewById(R.id.tv01_popTitle);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				this.dismiss();
				break;
			default:
				break;
		}
	}



}
