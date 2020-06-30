package com.jinsit.kmec.IR.CD;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_CD01_R00P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////

	private TextView tv_cbs_bldgNo, tv_cbs_bldgNm, tv_cbs_carNo,
			tv_cbs_csDeptNm, tv_cbs_empNm1, tv_cbs_empHp1, tv_cbs_empNm2,
			tv_cbs_empHp2, tv_cbs_recevTm, tv_cbs_arriveTm, tv_cbs_completeTm,
			tv_cbs_rescueTm, tv_cbs_statusCd, tv_cbs_cbsCd1, tv_cbs_cbsCd2,
			tv_cbs_cbsCd3, tv_cbs_faultCd, tv_cbs_procCd, tv_cbs_dutyCd,
			tv_cbs_orderDesc;

	private IR_CD01_R00_ITEM item;

	protected IR_CD01_R00P(Context context, IR_CD01_R00_ITEM item) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item = item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_cd01_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText("승객갇힘조회");
		// //////////////////
		tv_cbs_bldgNo.setText(item.getBLDG_NO());
		tv_cbs_bldgNm.setText(item.getBLDG_NM());
		tv_cbs_carNo.setText(item.getCAR_NO());
		tv_cbs_csDeptNm.setText(item.getCS_DEPT_NM());
		tv_cbs_empNm1.setText(item.getEMP_NM_1());
		tv_cbs_empHp1.setText(item.getEMP_1_HP());
		tv_cbs_empNm2.setText(item.getEMP_NM_2());
		tv_cbs_empHp2.setText(item.getEMP_2_HP());
		tv_cbs_recevTm.setText(item.getRECEV_TM());
		tv_cbs_arriveTm.setText(item.getARRIVE_TM());
		tv_cbs_completeTm.setText(item.getCOMPLETE_TM());
		tv_cbs_rescueTm.setText(item.getRESCUE_TM());
		tv_cbs_statusCd.setText(item.getSTATUS_CD());
		tv_cbs_cbsCd1.setText(item.getCBS_CD_1());
		tv_cbs_cbsCd2.setText(item.getCBS_CD_2());
		tv_cbs_cbsCd3.setText(item.getCBS_CD_3());
		tv_cbs_faultCd.setText(item.getFAULT_CD());
		tv_cbs_procCd.setText(item.getPROC_CD());
		tv_cbs_dutyCd.setText(item.getDUTY_CD());
		tv_cbs_orderDesc.setText(item.getORDER_DESC());
	}

	protected void getInstances() {
		// /title 위젯
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv_cbs_bldgNo = (TextView) findViewById(R.id.tv_cbs_bldgNo);
		tv_cbs_bldgNm = (TextView) findViewById(R.id.tv_cbs_bldgNm);
		tv_cbs_carNo = (TextView) findViewById(R.id.tv_cbs_carNo);
		tv_cbs_csDeptNm = (TextView) findViewById(R.id.tv_cbs_csDeptNm);
		tv_cbs_empNm1 = (TextView) findViewById(R.id.tv_cbs_empNm1);
		tv_cbs_empHp1 = (TextView) findViewById(R.id.tv_cbs_empHp1);
		tv_cbs_empNm2 = (TextView) findViewById(R.id.tv_cbs_empNm2);
		tv_cbs_empHp2 = (TextView) findViewById(R.id.tv_cbs_empHp2);
		tv_cbs_recevTm = (TextView) findViewById(R.id.tv_cbs_recevTm);
		tv_cbs_arriveTm = (TextView) findViewById(R.id.tv_cbs_arriveTm);
		tv_cbs_completeTm = (TextView) findViewById(R.id.tv_cbs_completeTm);
		tv_cbs_rescueTm = (TextView) findViewById(R.id.tv_cbs_rescueTm);
		tv_cbs_statusCd = (TextView) findViewById(R.id.tv_cbs_statusCd);
		tv_cbs_cbsCd1 = (TextView) findViewById(R.id.tv_cbs_cbsCd1);
		tv_cbs_cbsCd2 = (TextView) findViewById(R.id.tv_cbs_cbsCd2);
		tv_cbs_cbsCd3 = (TextView) findViewById(R.id.tv_cbs_cbsCd3);
		tv_cbs_faultCd = (TextView) findViewById(R.id.tv_cbs_faultCd);
		tv_cbs_procCd = (TextView) findViewById(R.id.tv_cbs_procCd);
		tv_cbs_dutyCd = (TextView) findViewById(R.id.tv_cbs_dutyCd);
		tv_cbs_orderDesc = (TextView) findViewById(R.id.tv_cbs_orderDesc);
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
