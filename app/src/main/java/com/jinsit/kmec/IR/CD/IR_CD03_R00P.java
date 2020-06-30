package com.jinsit.kmec.IR.CD;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_CD03_R00P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////

	private TextView tv_cbs_bldgNo, tv_cbs_bldgNm, tv_cbs_carNo,
			tv_cbs_modelNm, tv_cbs_orderNm, tv_cbs_recevDesc, tv_cbs_recevTm,
			tv_cbs_moveTm, tv_cbs_arriveTm, tv_cbs_reservTm, tv_cbs_csEmpNm,
			tv_cbs_phone1;

	private IR_CD03_R00_ITEM item;

	protected IR_CD03_R00P(Context context, IR_CD03_R00_ITEM item) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item = item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_cd03_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText("A/S미처리 조회");
		// //////////////////

		tv_cbs_bldgNo.setText(item.getBLDG_NO());
		tv_cbs_bldgNm.setText(item.getBLDG_NM());
		tv_cbs_carNo.setText(item.getCAR_NO());
		tv_cbs_modelNm.setText(item.getMODEL_NM());
		tv_cbs_orderNm.setText(item.getORDER_NM());
		tv_cbs_recevDesc.setText(item.getRECEV_DESC());
		tv_cbs_recevTm.setText(item.getRECEV_TM());
		tv_cbs_moveTm.setText(item.getMOVE_TM());
		tv_cbs_arriveTm.setText(item.getARRIVE_TM());
		tv_cbs_reservTm.setText(item.getRESERV_TM());
		tv_cbs_csEmpNm.setText(item.getCS_EMP_NM());
		tv_cbs_phone1.setText(item.getPHONE1());

	}

	protected void getInstances() {
		// /title 위젯
		tv_cbs_bldgNo = (TextView) findViewById(R.id.tv_cbs_bldgNo);
		tv_cbs_bldgNm = (TextView) findViewById(R.id.tv_cbs_bldgNm);
		tv_cbs_carNo = (TextView) findViewById(R.id.tv_cbs_carNo);
		tv_cbs_modelNm = (TextView) findViewById(R.id.tv_cbs_modelNm);
		tv_cbs_orderNm = (TextView) findViewById(R.id.tv_cbs_orderNm);
		tv_cbs_recevDesc = (TextView) findViewById(R.id.tv_cbs_recevDesc);
		tv_cbs_recevTm = (TextView) findViewById(R.id.tv_cbs_recevTm);
		tv_cbs_moveTm = (TextView) findViewById(R.id.tv_cbs_moveTm);
		tv_cbs_arriveTm = (TextView) findViewById(R.id.tv_cbs_arriveTm);
		tv_cbs_reservTm = (TextView) findViewById(R.id.tv_cbs_reservTm);
		tv_cbs_csEmpNm = (TextView) findViewById(R.id.tv_cbs_csEmpNm);
		tv_cbs_phone1 = (TextView) findViewById(R.id.tv_cbs_phone1);

		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);

		setEvents();
	}

	protected void setEvents() {
		// /title 위젯
		this.btn_popClose.setOnClickListener(this);
		// //////////////////
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// /title 위젯
			case R.id.btn_popClose:
				this.dismiss();
				break;
			// //////////////////
			default:
				break;
		}
	}

}
