package com.jinsit.kmec.WO.WT.MW;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.DateUtil;

public class WT_MW00_R02P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////

	TextView tv_mw_r02pCheckDay1, tv_mw_r02pInspecterInfo1,tv_mw_r02pSuccessInfo1, tv_mw_r02pFailInfo1,
			tv_mw_r02pRowid;
	private WT_MW00_R02_ITEM00 item;

	public WT_MW00_R02P(Context context, WT_MW00_R02_ITEM00 item) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item = item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_mw00_r02p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText(item.getROW_NUM()+"차검사 내용");
		tv_mw_r02pRowid.setText(item.getROW_NUM()+"차검사일");
		tv_mw_r02pCheckDay1.setText(item.getWORK_DT());
		tv_mw_r02pInspecterInfo1.setText(item.getINSP_ST_NM());
		tv_mw_r02pSuccessInfo1.setText(item.getJOB_ST_NM());
		tv_mw_r02pFailInfo1.setText(item.getDETAIL_RMK());
		

	}

	protected void getInstances() {
		// /title 위젯
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv_mw_r02pRowid=(TextView) findViewById(R.id.tv_mw_r02pRowid);
		tv_mw_r02pCheckDay1=(TextView) findViewById(R.id.tv_mw_r02pCheckDay1);
		tv_mw_r02pCheckDay1.setText(DateUtil.nowDate());
		tv_mw_r02pInspecterInfo1 = (TextView)findViewById(R.id.tv_mw_r02pInspecterInfo1);
		tv_mw_r02pSuccessInfo1=(TextView) findViewById(R.id.tv_mw_r02pSuccessInfo1);
		
		tv_mw_r02pFailInfo1=(TextView) findViewById(R.id.tv_mw_r02pFailInfo1);
	
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
