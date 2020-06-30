package com.jinsit.kmec.IR.PI;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_PI02_R01P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	///////////
	private IR_PI02_R00_Item item;

	private TextView tv_pi_outDt;
	private TextView tv_pi_bldgNm;
	private TextView tv_pi_bldgNo;
	private TextView tv_pi_partsNo;
	private TextView tv_pi_apprDt;
	private TextView tv_pi_itemNo;
	private TextView tv_pi_itemNm;
	private TextView tv_pi_size;
	private TextView tv_pi_outQty;
	private TextView tv_pi_plQty;


	public IR_PI02_R01P(Context context, IR_PI02_R00_Item item) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item = item;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi02_r01p);
		getInstances();
		this.tv01_popTitle.setText("출고상세내역");

		this.tv_pi_outDt.setText(this.item.getOutDt());
		this.tv_pi_bldgNm.setText(this.item.getBldgNm());
		this.tv_pi_bldgNo.setText(this.item.getBldgNo());
		this.tv_pi_partsNo.setText(this.item.getPartsNo());
		this.tv_pi_apprDt.setText(this.item.getApprDt());
		this.tv_pi_itemNo.setText(this.item.getItemNo());
		this.tv_pi_itemNm.setText(this.item.getItemNm());
		this.tv_pi_size.setText(this.item.getSize());
		this.tv_pi_outQty.setText(this.item.getOutQty());
		this.tv_pi_plQty.setText(this.item.getPlQty());
	}

	protected void getInstances() {
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv_pi_outDt = (TextView) findViewById(R.id.tv_pi_outDt);
		tv_pi_bldgNm = (TextView) findViewById(R.id.tv_pi_bldgNm);
		tv_pi_bldgNo = (TextView) findViewById(R.id.tv_pi_bldgNo);
		tv_pi_partsNo = (TextView) findViewById(R.id.tv_pi_partsNo);
		tv_pi_apprDt = (TextView) findViewById(R.id.tv_pi_apprDt);
		tv_pi_apprDt = (TextView) findViewById(R.id.tv_pi_apprDt);
		tv_pi_itemNo = (TextView) findViewById(R.id.tv_pi_itemNo);
		tv_pi_itemNm = (TextView) findViewById(R.id.tv_pi_itemNm);
		tv_pi_size = (TextView) findViewById(R.id.tv_pi_size);
		tv_pi_outQty = (TextView) findViewById(R.id.tv_pi_outQty);
		tv_pi_plQty = (TextView) findViewById(R.id.tv_pi_plQty);
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