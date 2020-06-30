package com.jinsit.kmec.IR.CI;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

public class IR_CI05_R01P extends AlertDialog implements
		android.view.View.OnClickListener {


	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
///////////

	private TextView tab_ci_contrDetail;
	private TextView tab_ci_carContrDetail;
	private LinearLayout lin_ci_contrDetail;
	private LinearLayout lin_ci_carContrDetail;

	private TextView tv_ci_bldgNo;
	private TextView tv_ci_deptNm;
	private TextView tv_ci_bldgNm;
	private TextView tv_ci_csContrNo;
	private TextView tv_ci_csNm;
	private TextView tv_ci_runSt;
	private TextView tv_ci_contrDt;
	private TextView tv_ci_issueDt;
	private TextView tv_ci_expireDt;
	private TextView tv_ci_csCnt;
	private TextView tv_ci_resCnt;
	private TextView tv_ci_csPrc;
	private TextView tv_ci_resPrc;
	private TextView tv_ci_amt;
	private TextView tv_ci_rmk;

	private ListView lv_ci_carContrDetail;

	private IR_CI05_R01P_ITEM01 item;
	private List<IR_CI05_R01P_ITEM02> itemList;
	private IR_CI05_R01P_Adapter01 adapter01;


	private final static int TAB_CONTRDETAIL =1;
	private final static int TAB_CARCONTRDETAIL =2;

	protected IR_CI05_R01P(Context context, IR_CI05_R01P_ITEM01 item,List<IR_CI05_R01P_ITEM02> itemList) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item = item;
		this.itemList = itemList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci05_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText("계약상세정보");
		// //////////////////

		this.lin_ci_contrDetail.setVisibility(View.VISIBLE);
		this.lin_ci_carContrDetail.setVisibility(View.GONE);

		tv_ci_bldgNo.setText(item.getBldgNo());
		tv_ci_deptNm.setText(item.getDeptNm());
		tv_ci_bldgNm.setText(item.getBldgNm());
		tv_ci_csContrNo.setText(item.getCsContrNo());
		tv_ci_csNm.setText(item.getCsNm());
		tv_ci_runSt.setText(item.getRunSt());
		tv_ci_contrDt.setText(item.getContrDt());
		tv_ci_issueDt.setText(item.getIssueDt());
		tv_ci_expireDt.setText(item.getExpireDt());
		tv_ci_csCnt.setText(item.getCsCnt());
		tv_ci_resCnt.setText(item.getResCnt());
		tv_ci_csPrc.setText(SysUtil.makeStringWithComma(item.getCsPrc(),true));
		tv_ci_resPrc.setText(SysUtil.makeStringWithComma(item.getResPrc(),true));
		tv_ci_amt.setText(SysUtil.makeStringWithComma(item.getAmt(),true));


		tv_ci_rmk.setText(item.getRmk());

		adapter01 = new IR_CI05_R01P_Adapter01(context,
				R.layout.ir_ci05_r01p_adapter01, this.itemList);
		lv_ci_carContrDetail.setAdapter(adapter01);
		btnSelect(1);

	}
	protected void getInstances() {
		///title 위젯
		tv01_popTitle= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		////////////////////

		tab_ci_contrDetail = (TextView)findViewById(R.id.tab_ci_contrDetail);
		tab_ci_carContrDetail = (TextView)findViewById(R.id.tab_ci_carContrDetail);
		lin_ci_contrDetail = (LinearLayout)findViewById(R.id.lin_ci_contrDetail);
		lin_ci_carContrDetail = (LinearLayout)findViewById(R.id.lin_ci_carContrDetail);
		lv_ci_carContrDetail = (ListView) findViewById(R.id.lv_ci_carContrDetail);


		tv_ci_bldgNo = (TextView) findViewById(R.id.tv_ci_bldgNo);
		tv_ci_deptNm= (TextView) findViewById(R.id.tv_ci_deptNm);
		tv_ci_bldgNm= (TextView) findViewById(R.id.tv_ci_bldgNm);
		tv_ci_csContrNo= (TextView) findViewById(R.id.tv_ci_csContrNo);
		tv_ci_csNm= (TextView) findViewById(R.id.tv_ci_csNm);
		tv_ci_runSt= (TextView) findViewById(R.id.tv_ci_runSt);
		tv_ci_contrDt= (TextView) findViewById(R.id.tv_ci_contrDt);
		tv_ci_issueDt= (TextView) findViewById(R.id.tv_ci_issueDt);
		tv_ci_expireDt= (TextView) findViewById(R.id.tv_ci_expireDt);
		tv_ci_csCnt= (TextView) findViewById(R.id.tv_ci_csCnt);
		tv_ci_resCnt= (TextView) findViewById(R.id.tv_ci_resCnt);
		tv_ci_csPrc= (TextView) findViewById(R.id.tv_ci_csPrc);
		tv_ci_resPrc= (TextView) findViewById(R.id.tv_ci_resPrc);
		tv_ci_amt= (TextView) findViewById(R.id.tv_ci_amt);
		tv_ci_rmk= (TextView) findViewById(R.id.tv_ci_rmk);
		setEvents();
	}

	protected void setEvents() {
		///title 위젯
		this.btn_popClose.setOnClickListener(this);
		////////////////////
		tab_ci_contrDetail.setOnClickListener(this);
		tab_ci_carContrDetail.setOnClickListener(this);
	}


	private void btnSelect(int idx) {

		tab_ci_contrDetail.setBackgroundResource(R.drawable.tab_ci05_contrdetail_off);
		tab_ci_carContrDetail.setBackgroundResource(R.drawable.tab_ci05_cardetail_off);

		tab_ci_contrDetail.setEnabled(true);
		tab_ci_carContrDetail.setEnabled(true);

		switch (idx) {
			case TAB_CONTRDETAIL:
				tab_ci_contrDetail.setBackgroundResource(R.drawable.tab_ci05_contrdetail_on);
				tab_ci_contrDetail.setEnabled(false);
				break;
			case TAB_CARCONTRDETAIL:
				tab_ci_carContrDetail.setBackgroundResource(R.drawable.tab_ci05_cardetail_on);
				tab_ci_carContrDetail.setEnabled(false);
				break;

		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			///title 위젯
			case R.id.btn_popClose:
				this.dismiss();
				break;
			////////////////////
			case R.id.tab_ci_contrDetail:
				tv01_popTitle.setText("계약상세정보");
				btnSelect(1);
				this.lin_ci_carContrDetail.setVisibility(View.GONE);
				this.lin_ci_contrDetail.setVisibility(View.VISIBLE);
				break;
			case R.id.tab_ci_carContrDetail:
				btnSelect(2);
				tv01_popTitle.setText("호기계약정보");
				this.lin_ci_contrDetail.setVisibility(View.GONE);
				this.lin_ci_carContrDetail.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
	}



}
