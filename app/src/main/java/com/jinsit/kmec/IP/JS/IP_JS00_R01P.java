package com.jinsit.kmec.IP.JS;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;

public class IP_JS00_R01P extends AlertDialog {

	private EasyJsonList ejl;
	private ListAdapter adapter01;
	private ListAdapter adapter02;
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private TextView tv01_js_workDt_v;
	private TextView tv02_js_isAttended_v;
	private TextView tv03_js_totalWorkDt_v;
	private ListView lv01_js_workList;
	private TextView tv01_js_tab1;
	private TextView tv02_js_tab2;
	private static int TAB_BASICWORKTIME = 1;
	private static int TAB_WORKTIME = 2;

	protected IP_JS00_R01P(Context context, EasyJsonList ejl, ListAdapter adapter01, ListAdapter adapter02) {
		super(context);
		this.ejl = ejl;
		this.adapter01 = adapter01;
		this.adapter02 = adapter02;
	}

	public interface ClickListener{
		void onClick();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_js00_r01p);
		getInstances();
		tv01_popTitle.setText("작업 내역");

		try {

			ejl.getValue(0,"WORK_DT");
			ejl.getValue(0,"ATTEND_NM");
			ejl.getValue(0,"ATTEND_TM");
			ejl.getValue(0,"LEAVE_TM");
			ejl.getValue(0,"WORK_TM");

			tv01_js_workDt_v.setText( ejl.getValue(0,"WORK_DT") );
			tv02_js_isAttended_v.setText( ejl.getValue(0,"ATTEND_NM") );
			tv03_js_totalWorkDt_v.setText( ejl.getValue(0,"ATTEND_TM") +"~"+ejl.getValue(0,"LEAVE_TM")+"("+ejl.getValue(0,"WORK_TM")+"분)" );

		} catch (JSONException e) {
			e.printStackTrace();
		}

		lv01_js_workList.setAdapter(adapter01);
		btnSelect(TAB_BASICWORKTIME);

	}

	protected void getInstances(){
		tv01_js_workDt_v		= (TextView) findViewById(R.id.tv01_js_workDt_v);
		tv02_js_isAttended_v	= (TextView) findViewById(R.id.tv02_js_isAttended_v);
		tv03_js_totalWorkDt_v	= (TextView) findViewById(R.id.tv03_js_totalWorkDt_v);
		lv01_js_workList		= (ListView) findViewById(R.id.lv01_js_workList);
		tv01_js_tab1			= (TextView) findViewById(R.id.tv01_js_tab1);
		tv02_js_tab2			= (TextView) findViewById(R.id.tv02_js_tab2);
		tv01_popTitle           = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose            = (TextView) findViewById(R.id.btn_popClose);
		setEvents();
	}

	protected void setEvents(){
		tv01_js_tab1.setOnClickListener(listener);
		tv02_js_tab2.setOnClickListener(listener);
		btn_popClose.setOnClickListener(listener);
	}


	private void btnSelect(int idx) {

		tv01_js_tab1.setBackgroundResource(R.drawable.tab_js_worktime_off);
		tv02_js_tab2.setBackgroundResource(R.drawable.tab_js_homecall_off);

		tv01_js_tab1.setEnabled(true);
		tv02_js_tab2.setEnabled(true);
		switch (idx) {
			case 1:
				tv01_js_tab1.setBackgroundResource(R.drawable.tab_js_worktime_on);
				tv01_js_tab1.setEnabled(false);
				break;
			case 2:
				tv02_js_tab2.setBackgroundResource(R.drawable.tab_js_homecall_on);
				tv02_js_tab2.setEnabled(false);
				break;
		}
	}

	android.view.View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				case R.id.tv01_js_tab1:
					btnSelect(TAB_BASICWORKTIME);
					lv01_js_workList.setAdapter(null);
					lv01_js_workList.setAdapter(adapter01);
					break;

				case R.id.tv02_js_tab2:
					btnSelect(TAB_WORKTIME);
					lv01_js_workList.setAdapter(null);
					lv01_js_workList.setAdapter(adapter02);
					break;
				case R.id.btn_popClose:
					cancel();
					break;
			}
		}
	};

};
