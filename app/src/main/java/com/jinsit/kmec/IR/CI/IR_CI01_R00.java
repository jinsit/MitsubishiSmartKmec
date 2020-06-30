package com.jinsit.kmec.IR.CI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.CM.SearchAdminInfo;

public class IR_CI01_R00 extends Fragment implements OnClickListener {
	private Context context;
	private Activity activity;

	private TextView tv_ci01_pBldgNo;
	private TextView tv_ci01_bldgNm;
	private TextView tv_ci01_addr;
	private TextView tv_ci01_csCd;
	private TextView tv_ci01_runSt;
	private TextView tv_ci01_clientDept;
	private TextView tv_ci01_clientTel;
	private TextView tv_ci01_clientNm;
	private TextView tv_ci01_clientHp;
	private TextView tv_ci01_contrDt;
	private TextView tv_ci01_contrDtFrTo;
	private TextView tv_ci01_faultDtFrTo;
	private TextView tv_ci01_insDtFrTo;
	private TextView tv_ci01_empNm1;
	private TextView tv_ci01_empNm2;
	private TextView tv_ci01_emp1Hp;
	private TextView tv_ci01_emp2Hp;

	private TextView btn_ci01_adminInfo;
	private TextView btn_ci01_carNoInfo;

	private android.app.ActionBar aBar;

	private CM_SearchBldgInfo_ITEM01 item01;


	/**
	 *
	 */
	public IR_CI01_R00(CM_SearchBldgInfo_ITEM01 item) {
		super();
		// TODO Auto-generated constructor stub
		this.item01 = item;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ir_ci01_r00, null);
		activityInit(view);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	protected void activityInit(View view) {
		context = activity;
		getInstances(view);

		// 타이틀 바
		aBar = this.activity.getActionBar();
		aBar.setTitle("기본 정보");
		aBar.setDisplayShowHomeEnabled(false);

		tv_ci01_pBldgNo.setText(item01.getBldgNo());
		tv_ci01_bldgNm.setText(item01.getBldgNm());
		tv_ci01_addr.setText(item01.getAddr());
		tv_ci01_csCd.setText(item01.getCsCd());
		tv_ci01_runSt.setText(item01.getRunSt());
		tv_ci01_clientDept.setText(item01.getClientDept());
		tv_ci01_clientTel.setText(item01.getClientTel());
		tv_ci01_clientNm.setText(item01.getClientNm());
		tv_ci01_clientHp.setText(item01.getClientHp());
		tv_ci01_contrDt.setText(item01.getContrDt());
		tv_ci01_contrDtFrTo.setText(item01.getContrDtFrTo());
		tv_ci01_faultDtFrTo.setText(item01.getFaultDtFrTo());
		tv_ci01_insDtFrTo.setText(item01.getInsDtFrTo());
		tv_ci01_empNm1.setText(item01.getEmpNm1());
		tv_ci01_empNm2.setText(item01.getEmpNm2());
		tv_ci01_emp1Hp.setText(item01.getEmp1hP());
		tv_ci01_emp2Hp.setText(item01.getEmp2hP());
	}

	protected void getInstances(View view) {

		tv_ci01_pBldgNo = (TextView) view.findViewById(R.id.tv_ci01_pBldgNo);
		tv_ci01_bldgNm = (TextView) view.findViewById(R.id.tv_ci01_bldgNm);
		tv_ci01_addr = (TextView) view.findViewById(R.id.tv_ci01_addr);
		tv_ci01_csCd = (TextView) view.findViewById(R.id.tv_ci01_csCd);
		tv_ci01_runSt = (TextView) view.findViewById(R.id.tv_ci01_runSt);
		tv_ci01_clientDept = (TextView) view
				.findViewById(R.id.tv_ci01_clientDept);
		tv_ci01_clientTel = (TextView) view
				.findViewById(R.id.tv_ci01_clientTel);
		tv_ci01_clientNm = (TextView) view.findViewById(R.id.tv_ci01_clientNm);
		tv_ci01_clientHp = (TextView) view.findViewById(R.id.tv_ci01_clientHp);
		tv_ci01_contrDt = (TextView) view.findViewById(R.id.tv_ci01_contrDt);
		tv_ci01_contrDtFrTo = (TextView) view
				.findViewById(R.id.tv_ci01_contrDtFrTo);
		tv_ci01_faultDtFrTo = (TextView) view
				.findViewById(R.id.tv_ci01_faultDtFrTo);
		tv_ci01_insDtFrTo = (TextView) view
				.findViewById(R.id.tv_ci01_insDtFrTo);
		tv_ci01_empNm1 = (TextView) view.findViewById(R.id.tv_ci01_empNm1);
		tv_ci01_empNm2 = (TextView) view.findViewById(R.id.tv_ci01_empNm2);
		tv_ci01_emp1Hp = (TextView) view.findViewById(R.id.tv_ci01_emp1Hp);
		tv_ci01_emp2Hp = (TextView) view.findViewById(R.id.tv_ci01_emp2Hp);

		btn_ci01_adminInfo = (TextView) view
				.findViewById(R.id.btn_ci01_adminInfo);
		btn_ci01_carNoInfo = (TextView) view
				.findViewById(R.id.btn_ci01_carNoInfo);

		setEvents();
	}

	protected void setEvents() {
		btn_ci01_adminInfo.setOnClickListener(this);
		btn_ci01_carNoInfo.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			case R.id.btn_ci01_adminInfo:
				SearchAdminInfo sAdminInfo = new SearchAdminInfo(context,
						item01.getBldgNo());
				sAdminInfo.show();
				break;
			case R.id.btn_ci01_carNoInfo:
				CM_SearchElev elev = new CM_SearchElev(context, item01.getBldgNo(),
						2);
				elev.show();
				elev.elevSearch();
				break;

			default:
				break;
		}
	}

}
