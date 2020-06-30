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

public class IR_PI01_R01P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	///////////

	private IR_PI01_R01P_Item01 item01;
	private List<IR_PI01_R01P_Item02> itemList02;


	private TextView tv_pi_partsDt;
	private TextView tv_pi_bldgNo;
	private TextView tv_pi_bldgNm;
	private TextView tv_pi_refContrNo;
	private TextView tv_pi_repSt;
	private TextView tv_pi_partsCd;
	private TextView tv_pi_matDueDt;
	private TextView tv_pi_apprDt;
	private TextView tv_pi_matDt;
	private TextView tv_pi_matApprDt;
	private ListView lv_pi_partList;

	private ListAdapter adpater01;

	public IR_PI01_R01P(Context context, IR_PI01_R01P_Item01 item01,
						List<IR_PI01_R01P_Item02> itemList02) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item01 = item01;
		this.itemList02 = itemList02;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi01_r01p);
		getInstances();
		this.tv01_popTitle.setText("청구상세내역");

		this.tv_pi_partsDt.setText(this.item01.getPartsDt());
		this.tv_pi_bldgNo.setText(this.item01.getBldgNo());
		this.tv_pi_bldgNm.setText(this.item01.getBldgNm());
		this.tv_pi_refContrNo.setText(this.item01.getRefContrNo());
		this.tv_pi_repSt.setText(this.item01.getRepSt());
		this.tv_pi_partsCd.setText(this.item01.getPartsCd());
		this.tv_pi_matDueDt.setText(this.item01.getMatDueDt());
		this.tv_pi_apprDt.setText(this.item01.getApprDt());
		this.tv_pi_matDt.setText(this.item01.getMatDt());
		this.tv_pi_matApprDt.setText(this.item01.getMatApprDt());
		adpater01 = new IR_PI01_R01P_Adapter(context,
				R.layout.ir_pi01_r01p_adapter, itemList02);
		this.lv_pi_partList.setAdapter(adpater01);
	}

	protected void getInstances() {

		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv_pi_partsDt = (TextView) findViewById(R.id.tv_pi_partsDt);
		tv_pi_bldgNo = (TextView) findViewById(R.id.tv_pi_bldgNo);
		tv_pi_bldgNm = (TextView) findViewById(R.id.tv_pi_bldgNm);
		tv_pi_refContrNo = (TextView) findViewById(R.id.tv_pi_refContrNo);
		tv_pi_repSt = (TextView) findViewById(R.id.tv_pi_repSt);
		tv_pi_partsCd = (TextView) findViewById(R.id.tv_pi_partsCd);
		tv_pi_matDueDt = (TextView) findViewById(R.id.tv_pi_matDueDt);
		tv_pi_apprDt = (TextView) findViewById(R.id.tv_pi_apprDt);
		tv_pi_matDt = (TextView) findViewById(R.id.tv_pi_matDt);
		tv_pi_matApprDt = (TextView) findViewById(R.id.tv_pi_matApprDt);
		lv_pi_partList = (ListView) findViewById(R.id.lv_pi_partList);
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
