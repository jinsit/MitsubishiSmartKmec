package com.jinsit.kmec.IR.PI;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_PI03_R01P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	///////////
	private IR_PI03_R01P_Item01 item;
	private List<IR_PI03_R01P_Item02> itemList;

	private TextView tv_pi_itemNo;
	private TextView tv_pi_itemNm;
	private TextView tv_pi_drawNo;
	private TextView tv_pi_stockQty;
	private ListView lv_pi_stockDetailList;
	private ListAdapter adpater01;

	public IR_PI03_R01P(Context context, IR_PI03_R01P_Item01 item,
						List<IR_PI03_R01P_Item02> itemList) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item = item;
		this.itemList = itemList;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi03_r01p);
		activityInit();
	}

	protected void activityInit() {

		getInstances();
		this.tv01_popTitle.setText("재고상세정보");
		this.tv_pi_itemNo.setText(this.item.getItemNo());
		this.tv_pi_itemNm.setText(this.item.getItemNm());
		this.tv_pi_drawNo.setText(this.item.getDrawNo());
		this.tv_pi_stockQty.setText(this.item.getQty());
		adpater01 = new IR_PI03_R01P_Adapter(context,
				R.layout.ir_pi03_r01p_adapter, itemList);
		this.lv_pi_stockDetailList.setAdapter(adpater01);
	}

	protected void getInstances() {
		tv01_popTitle= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);

		tv_pi_itemNo = (TextView) findViewById(R.id.tv_pi_itemNo);
		tv_pi_itemNm = (TextView) findViewById(R.id.tv_pi_itemNm);
		tv_pi_drawNo = (TextView) findViewById(R.id.tv_pi_drawNo);
		tv_pi_stockQty = (TextView) findViewById(R.id.tv_pi_stockQty);
		lv_pi_stockDetailList = (ListView) findViewById(R.id.lv_pi_stockDetailList);
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