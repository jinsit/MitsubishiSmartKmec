package com.jinsit.kmec.IR.CI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.CM_SelectPermissionCheck_StartActivity;
import com.jinsit.kmec.IR.TI.IR_TI00_R00;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.widget.JActionbar;

public class IR_CI00_R00_F extends FragmentActivity implements OnClickListener {
	private Context context;

	private IR_CI00_R00_F_Widget fgWidget;

	private TextView btn_ci_basicInfo;
	private TextView btn_ci_troubleHistory;
	private TextView btn_ci_inspectionHistory;
	private TextView btn_ci_equipmentHistory;
	private TextView btn_ci_repaireSales;

	int currentFragmentIndex;

	public final static int FRAGMENT_BASIC_INFO = 0;
	public final static int FRAGMENT_TROUBLE_HISTORY = 1;
	public final static int FRAGMENT_INSPECTION_HISTORY = 2;
	public final static int FRAGMENT_EQUIPMENT_HISTORY = 3;
	public final static int FRAGMENT_REPAIRE_SALES = 4;

	public final static int FRAGMENT_INSPECTION_SCHEDULE = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci00_r00_f);
		activityInit();
	}

	protected void activityInit() {
		context = this;
		getInstances();

		// 타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("고객 정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		Object selectedItem = (Object) this.getIntent().getExtras()
				.getSerializable("selectedItem");
		if (selectedItem.getClass().getName()
				.equals("com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01")) {
			currentSelectedItem01 = (CM_SearchBldgInfo_ITEM01) selectedItem;
		} else {
			currentSelectedItem01 = new CM_SearchBldgInfo_ITEM01();
		}
		fragmentReplace(FRAGMENT_BASIC_INFO);
		btnSelect(FRAGMENT_BASIC_INFO);

	}

	protected void getInstances() {
		fgWidget = new IR_CI00_R00_F_Widget(context, null);
		fgWidget = (IR_CI00_R00_F_Widget) findViewById(R.id.widg_ci_menu);

		btn_ci_basicInfo = (TextView) fgWidget
				.findViewById(R.id.btn_ci_basicInfo);
		btn_ci_troubleHistory = (TextView) fgWidget
				.findViewById(R.id.btn_ci_troubleHistory);
		btn_ci_inspectionHistory = (TextView) fgWidget
				.findViewById(R.id.btn_ci_inspectionHistory);
		btn_ci_equipmentHistory = (TextView) fgWidget
				.findViewById(R.id.btn_ci_equipmentHistory);
		btn_ci_repaireSales = (TextView) fgWidget
				.findViewById(R.id.btn_ci_repaireSales);
		setEvents();
	}

	protected void setEvents() {
		btn_ci_basicInfo.setOnClickListener(this);
		btn_ci_troubleHistory.setOnClickListener(this);
		btn_ci_inspectionHistory.setOnClickListener(this);
		btn_ci_equipmentHistory.setOnClickListener(this);
		btn_ci_repaireSales.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_ci_basicInfo:
				fragmentReplace(FRAGMENT_BASIC_INFO);
				btnSelect(FRAGMENT_BASIC_INFO);
				break;

			case R.id.btn_ci_troubleHistory:
				fragmentReplace(FRAGMENT_TROUBLE_HISTORY);
				btnSelect(FRAGMENT_TROUBLE_HISTORY);
				break;
			case R.id.btn_ci_inspectionHistory:
				fragmentReplace(FRAGMENT_INSPECTION_HISTORY);
				btnSelect(FRAGMENT_INSPECTION_HISTORY);
				break;
			case R.id.btn_ci_equipmentHistory:
				fragmentReplace(FRAGMENT_EQUIPMENT_HISTORY);
				btnSelect(FRAGMENT_EQUIPMENT_HISTORY);
				break;
			case R.id.btn_ci_repaireSales:
				///이건 한번 체크해야 한다. 권한체크를 말이다.
				CM_SelectPermissionCheck_StartActivity pCheck= new CM_SelectPermissionCheck_StartActivity(context);
				String ret = pCheck.permissionCheck("IR_CI05_R00");
				if(ret.equals("1")){
					//권한있음
					fragmentReplace(FRAGMENT_REPAIRE_SALES);
					btnSelect(FRAGMENT_REPAIRE_SALES);
				}else if(ret.equals("0")){
					//권한없음
					SimpleDialog sm01 = new SimpleDialog(context, "알림","접근권한이 없습니다.");
					sm01.show();
				}else{
					//네트워크에러
					SimpleDialog sm01 = new SimpleDialog(context, "알림","네트워크 연결 에러입니다. WIFI나 LTE를 확인하시고 다시 시도하십시오.");
					sm01.show();
				}

//			fragmentReplace(FRAGMENT_REPAIRE_SALES);
//			btnSelect(FRAGMENT_REPAIRE_SALES);
				break;

			default:
				break;

		}
	}

	public void fragmentReplace(int fragmentIndex) {
		Fragment newFragment = null;
		switch (fragmentIndex) {
			case FRAGMENT_BASIC_INFO:
				newFragment = new IR_CI01_R00(this.currentSelectedItem01);
				break;
			case FRAGMENT_TROUBLE_HISTORY:
				newFragment = new IR_CI02_R00(this.currentSelectedItem01);
				break;

			case FRAGMENT_INSPECTION_HISTORY:
				newFragment = new IR_CI03_R00(this.currentSelectedItem01);
				break;
				//점검계획 추가 20190917 yowonsm
			case FRAGMENT_INSPECTION_SCHEDULE:
				newFragment = new IR_CI03_R01(this.currentSelectedItem01);
				break;
			case FRAGMENT_EQUIPMENT_HISTORY:
				newFragment = new IR_CI04_R00(this.currentSelectedItem01);
				break;
			case FRAGMENT_REPAIRE_SALES:
				newFragment = new IR_CI05_R00(this.currentSelectedItem01);
				break;
			default:
				break;
		}

		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.lin_ci_parent, newFragment);
		transaction.commit();

	}

	private void btnSelect(int idx) {

		btn_ci_basicInfo.setBackgroundResource(R.drawable.tab_ci01_off);
		btn_ci_troubleHistory.setBackgroundResource(R.drawable.tab_ci02_off);
		btn_ci_inspectionHistory.setBackgroundResource(R.drawable.tab_ci03_off);
		btn_ci_equipmentHistory.setBackgroundResource(R.drawable.tab_ci04_off);
		btn_ci_repaireSales.setBackgroundResource(R.drawable.tab_ci05_off);

		btn_ci_basicInfo.setEnabled(true);
		btn_ci_troubleHistory.setEnabled(true);
		btn_ci_inspectionHistory.setEnabled(true);
		btn_ci_equipmentHistory.setEnabled(true);
		btn_ci_repaireSales.setEnabled(true);

		switch (idx) {
			case FRAGMENT_BASIC_INFO:
				btn_ci_basicInfo.setBackgroundResource(R.drawable.tab_ci01_on);
				btn_ci_basicInfo.setEnabled(false);
				break;
			case FRAGMENT_TROUBLE_HISTORY:
				btn_ci_troubleHistory.setBackgroundResource(R.drawable.tab_ci02_on);
				btn_ci_troubleHistory.setEnabled(false);
				break;
			case FRAGMENT_INSPECTION_HISTORY:
			case FRAGMENT_INSPECTION_SCHEDULE:

				btn_ci_inspectionHistory
						.setBackgroundResource(R.drawable.tab_ci03_on);
				btn_ci_inspectionHistory.setEnabled(false);
				break;

			case FRAGMENT_EQUIPMENT_HISTORY:
				btn_ci_equipmentHistory.setBackgroundResource(R.drawable.tab_ci04_on);
				btn_ci_equipmentHistory.setEnabled(false);
				break;
			case FRAGMENT_REPAIRE_SALES:
				btn_ci_repaireSales.setBackgroundResource(R.drawable.tab_ci05_on);
				btn_ci_repaireSales.setEnabled(false);
				break;
		}
	}



	public CM_SearchBldgInfo_ITEM01 currentSelectedItem01;



}
