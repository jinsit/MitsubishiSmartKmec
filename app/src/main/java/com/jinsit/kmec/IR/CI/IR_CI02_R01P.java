package com.jinsit.kmec.IR.CI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinsit.kmec.IR.ES.IR_ES00_R00;
import com.jinsit.kmec.R;

import java.util.Properties;

public class IR_CI02_R01P  extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
///////////

	private TextView tv_ci_carNo;
	private TextView tv_ci_modelNm;
	private TextView tv_ci_recevDt;
	private TextView tv_ci_arriveDt;
	private TextView tv_ci_completeDt;
	private TextView tv_ci_rescueDt;
	private TextView tv_ci_contactCd;
	private TextView tv_ci_recevDesc;
	private TextView tv_ci_statusCd;
	private TextView tv_ci_cbsCd1;
	private TextView tv_ci_cbsCd2;
	private TextView tv_ci_cbsCd3;
	private TextView tv_ci_dutyCd;
	private TextView tv_ci_procCd;
	private TextView tv_ci_faultCd;
	private TextView tv_ci_orderDesc;

	private TextView tv_ci_keeper;
	private TextView tv_ci_phone;

	private TextView tv_ci_notify;
	private TextView tv_ci_notify_phone;

	private IR_CI02_R01P_ITEM01 item;
	protected IR_CI02_R01P(Context context, IR_CI02_R01P_ITEM01 item, String recevNo) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item =item;
		this.recevNo =recevNo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci02_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		///title 위젯
		tv01_popTitle.setText("고장상세내역");
		////////////////////

		tv_ci_carNo.setText(item.getCarNo());
		tv_ci_modelNm.setText(item.getModelNm());
		tv_ci_recevDt.setText(item.getRecevDt());
		tv_ci_arriveDt.setText(item.getArriveDt());
		tv_ci_completeDt.setText(item.getCompleteDt());
		tv_ci_rescueDt.setText(item.getRescueDt());
		tv_ci_contactCd.setText(item.getContactCd());
		tv_ci_recevDesc.setText(item.getRecevDesc());
	/*tv_ci_statusCd.setText(item.getStatusCd()); statusCd가 데이터가 없이 나오고
	공주임이 말한대로라면 exOrdercd가 고장상황과 일치하는 데이터이다.
	ex 접수취소, 진동소음 등등
	statusCd가 st에 따라 분류된다. 전체/고장/서비스 ...
	ex_orderCd가 없으면 status는 항상있다는 가정하에 코딩한다.
	*/
		if(item.getExOrder().equals(""))tv_ci_statusCd.setText(item.getStatusCd());
		if(item.getStatusCd().equals(""))tv_ci_statusCd.setText(item.getExOrder());
		tv_ci_cbsCd1.setText(item.getCbsCd1());
		tv_ci_cbsCd2.setText(item.getCbsCd2());
		tv_ci_cbsCd3.setText(item.getCbsCd3());
		tv_ci_dutyCd.setText(item.getDutyCd());
		tv_ci_procCd.setText(item.getProcCd());
		tv_ci_faultCd.setText(item.getFaultCd());
		tv_ci_orderDesc.setText(item.getOrderDesc());
		tv_ci_keeper.setText(item.getCsEmpNm());
		tv_ci_phone.setText(item.getCsEmpMobile());
		tv_ci_notify.setText(item.getNotify());
		tv_ci_notify_phone.setText(item.getNotifyPhone());

	}
	protected void getInstances() {
		///title 위젯
		tv01_popTitle= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		////////////////////
		tv_ci_carNo = (TextView) findViewById(R.id.tv_ci_carNo);
		tv_ci_modelNm = (TextView) findViewById(R.id.tv_ci_modelNm);
		tv_ci_recevDt = (TextView) findViewById(R.id.tv_ci_recevDt);
		tv_ci_arriveDt = (TextView) findViewById(R.id.tv_ci_arriveDt);
		tv_ci_completeDt= (TextView) findViewById(R.id.tv_ci_completeDt);
		tv_ci_rescueDt= (TextView) findViewById(R.id.tv_ci_rescueDt);
		tv_ci_contactCd= (TextView) findViewById(R.id.tv_ci_contactCd);
		tv_ci_recevDesc= (TextView) findViewById(R.id.tv_ci_recevDesc);
		tv_ci_statusCd= (TextView) findViewById(R.id.tv_ci_statusCd);
		tv_ci_cbsCd1= (TextView) findViewById(R.id.tv_ci_cbsCd1);
		tv_ci_cbsCd2= (TextView) findViewById(R.id.tv_ci_cbsCd2);
		tv_ci_cbsCd3= (TextView) findViewById(R.id.tv_ci_cbsCd3);
		tv_ci_dutyCd= (TextView)findViewById(R.id.tv_ci_dutyCd);
		tv_ci_procCd = (TextView)findViewById(R.id.tv_ci_procCd);
		tv_ci_faultCd= (TextView) findViewById(R.id.tv_ci_faultCd);
		tv_ci_orderDesc = (TextView)findViewById(R.id.tv_ci_orderDesc);
		tv_ci_keeper = (TextView)findViewById(R.id.tv_ci_keeper);
		tv_ci_phone = (TextView)findViewById(R.id.tv_ci_phone);
		tv_ci_notify = (TextView)findViewById(R.id.tv_ci_notify);
		tv_ci_notify_phone = (TextView)findViewById(R.id.tv_ci_notify_phone);

		setEvents();
	}

	protected void setEvents() {
		///title 위젯
		this.btn_popClose.setOnClickListener(this);
		this.tv_ci_keeper.setOnClickListener(this);
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
			case R.id.tv_ci_keeper:
				moveActivity();
				break;

			default:
				break;
		}
	}

	private void moveActivity(){
		if(tv_ci_keeper.getText().toString() != null){
			Intent intent = new Intent(context, IR_ES00_R00.class);
			intent.putExtra("csEmpNm", tv_ci_keeper.getText().toString());
			context.startActivity(intent);
		}

	}

	private String recevNo;


}
