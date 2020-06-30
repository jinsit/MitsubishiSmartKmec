package com.jinsit.kmec.IR.CI;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_CI03_R01P  extends AlertDialog implements
		android.view.View.OnClickListener {


	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
///////////

	private TextView tv_ci_jobNo;
	private TextView tv_ci_workDt;
	private TextView tv_ci_st;
	private TextView tv_ci_cbsCd1;
	private TextView tv_ci_cbsCd2;
	private TextView tv_ci_cbsCd3;
	private TextView tv_ci_faultCd;
	private TextView tv_ci_procCd;
	private TextView tv_ci_dutyCd;
	//private TextView tv_ci_check01;
//private TextView tv_ci_check02;
//private TextView tv_ci_check03;
//private TextView tv_ci_check04;
//private TextView tv_ci_check05;
//private TextView tv_ci_check06;
	private ListView lv_ci_deepDetail;


	private IR_CI03_R01P_ITEM01 item;
	private IR_CI03_R00_ITEM01 headerItem;
	private  ArrayList<IR_CI03_R01P_ITEM02> listItem;
	private IR_CI03_R01P_Adapter01 iR_CI03_R01P_Adapter01;
	protected IR_CI03_R01P(Context context, IR_CI03_R01P_ITEM01 item, IR_CI03_R00_ITEM01 headerItem, ArrayList<IR_CI03_R01P_ITEM02> item03_02) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item =item;
		this.headerItem = headerItem;
		this.context = context;
		this.listItem = item03_02;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci03_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		///title 위젯
		tv01_popTitle.setText("점검상세내역");
		////////////////////

		tv_ci_jobNo.setText(headerItem.getJobNo());
		tv_ci_workDt.setText(headerItem.getWorkDt());
		tv_ci_st.setText(headerItem.getSt());
		tv_ci_cbsCd1.setText(item.getCbsCd1());
		tv_ci_cbsCd2.setText(item.getCbsCd2());
		tv_ci_cbsCd3.setText(item.getCbsCd3());
		tv_ci_faultCd.setText(item.getFaultCd());
		tv_ci_procCd.setText(item.getProcCd());
		tv_ci_dutyCd.setText(item.getDutyCd());
//	tv_ci_check01.setText(item.getCheck01());
//	tv_ci_check02.setText(item.getCheck02());
//	tv_ci_check03.setText(item.getCheck03());
//	tv_ci_check04.setText(item.getCheck04());
//	tv_ci_check05.setText(item.getCheck05());
//	tv_ci_check06.setText(item.getCheck06());
	}
	protected void getInstances() {
		///title 위젯
		tv01_popTitle= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		////////////////////
		tv_ci_jobNo = (TextView) findViewById(R.id.tv_ci_jobNo);
		tv_ci_workDt = (TextView) findViewById(R.id.tv_ci_workDt);
		tv_ci_st = (TextView) findViewById(R.id.tv_ci_st);
		tv_ci_cbsCd1 = (TextView) findViewById(R.id.tv_ci_cbsCd1);
		tv_ci_cbsCd2= (TextView) findViewById(R.id.tv_ci_cbsCd2);
		tv_ci_cbsCd3= (TextView) findViewById(R.id.tv_ci_cbsCd3);
		tv_ci_faultCd= (TextView) findViewById(R.id.tv_ci_faultCd);
		tv_ci_procCd= (TextView) findViewById(R.id.tv_ci_procCd);
		tv_ci_dutyCd= (TextView) findViewById(R.id.tv_ci_dutyCd);
//	tv_ci_check01= (TextView) findViewById(R.id.tv_ci_check01);
//	tv_ci_check02= (TextView) findViewById(R.id.tv_ci_check02);
//	tv_ci_check03= (TextView) findViewById(R.id.tv_ci_check03);
//	tv_ci_check04= (TextView) findViewById(R.id.tv_ci_check04);
//	tv_ci_check05= (TextView) findViewById(R.id.tv_ci_check05);
//	tv_ci_check06= (TextView) findViewById(R.id.tv_ci_check06);
		lv_ci_deepDetail = (ListView)findViewById(R.id.lv_ci_deepDetail);
		iR_CI03_R01P_Adapter01 = new IR_CI03_R01P_Adapter01(context, listItem);
		lv_ci_deepDetail.setAdapter(iR_CI03_R01P_Adapter01);
		listViewHeightSet(iR_CI03_R01P_Adapter01, lv_ci_deepDetail);
		setEvents();
	}

	protected void setEvents() {
		///title 위젯
		this.btn_popClose.setOnClickListener(this);
		////////////////////
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
			default:
				break;
		}
	}
	private void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


}
