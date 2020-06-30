package com.jinsit.kmec.WO.WT.TS;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_ReadPicture;
/**
 * 고장수리
 */
public class WO_TS00_R11P extends Dialog implements OnClickListener, OnDismissListener{

	public WO_TS00_R11P(Context context,WO_TS00_R00_ITEM03 workTargetData,
			WO_TS00_R03P_ITEM01 selectedItem03, List<WO_TS00_R04P_ITEM01> selectedItemList04,
			WO_TS00_R07P_ITEM01 selectedItem07,WO_TS00_R08P_ITEM01 selectedItem08,
			WO_TS00_R09P_ITEM01 selectedItem09, WO_TS00_R00_ITEM04 item04) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.workTargetData = workTargetData;
		this.selectedItem03 = selectedItem03;
		this.selectedItemList04 = selectedItemList04;
		this.selectedItem07 = selectedItem07;
		this.selectedItem08 = selectedItem08;
		this.selectedItem09 = selectedItem09;
		this.item04 = item04;
	}

	private Context context;
	
	private TextView tv01_popTitle;
	private TextView btn_popClose;


	private TextView btn_fr_picture;
	private TextView btn_fr_register;
	
	private TextView tv_fr_jobNo;
	private TextView tv_fr_carInfo;
	private TextView tv_fr_recevDesc;
	private TextView tv_fr_date;
	private TextView tv_fr_reserveDate;
	private TextView tv_fr_moveTm;
	private TextView tv_fr_arrivalTm;
	private TextView tv_fr_completeTm;
	private TextView tv_fr_contactYn;
	private TextView tv_fr_rescueYn;
	private TextView tv_fr_rescueTm;
	private TextView tv_fr_aSCd;
	private TextView tv_fr_cbsCd1;
	private TextView tv_fr_cbsCd2;
	private TextView tv_fr_cbsCd3;
	private TextView tv_fr_faultCd;
	private TextView tv_fr_procCd;
	private TextView tv_fr_dutyCd;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wo_ts00_r11p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("A/S 처리 보고서");	
		
		tv_fr_jobNo.setText(this.workTargetData.getJobNO());
		tv_fr_carInfo.setText(this.workTargetData.getCarNo() + " "+ this.workTargetData.getModelNm());
		tv_fr_recevDesc.setText(this.workTargetData.getRecevDesc());
		tv_fr_date.setText(this.workTargetData.getRecevTm());
		tv_fr_reserveDate.setText(this.workTargetData.getReservTm());
		tv_fr_moveTm.setText(this.workTargetData.getMoveTm());
		tv_fr_arrivalTm.setText(this.workTargetData.getArriveTm());
		tv_fr_completeTm.setText(this.workTargetData.getCompleteTm());
		if(this.workTargetData.isContact())
		{
			tv_fr_contactYn.setText("Y");
		}
		else
		{
			tv_fr_contactYn.setText("N");
		}
		if(this.workTargetData.isStatus())
		{
			tv_fr_rescueYn.setText("Y");
			tv_fr_rescueTm.setText(this.workTargetData.getRescueTm());
		}
		else
		{
			tv_fr_rescueYn.setText("N");
			tv_fr_rescueTm.setText("");
		}
		if(this.selectedItem03 != null){
			tv_fr_aSCd.setText(this.selectedItem03.getCodeNm());
		}

		if(this.selectedItemList04.size() > 0){
			tv_fr_cbsCd1.setText(this.selectedItemList04.get(0).getAssyNm());
			tv_fr_cbsCd2.setText(this.selectedItemList04.get(1).getAssyNm());
			tv_fr_cbsCd3.setText(this.selectedItemList04.get(2).getAssyNm());
		}
		if(this.selectedItem07 != null){
			tv_fr_faultCd.setText(this.selectedItem07.getFaultNm());
		}
		if(this.selectedItem08 != null){
			tv_fr_procCd.setText(this.selectedItem08.getProcNm());
		}
		if(this.selectedItem09 != null){
			tv_fr_dutyCd.setText(this.selectedItem09.getDutyNm());
		}
		if( item04.getCustSign() != null && !item04.getCustSign().equals("")){
			this.btn_fr_register.setText("고객 승인 보기");
		}else{
			this.btn_fr_register.setText("작업 완료 등록");
		}
		
		this.resultOk = false;
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		
		btn_fr_picture = (TextView)findViewById(R.id.btn_fr_picture);
		btn_fr_register = (TextView)findViewById(R.id.btn_fr_register);
		
		tv_fr_jobNo = (TextView) findViewById(R.id.tv_fr_jobNo);
		tv_fr_carInfo = (TextView) findViewById(R.id.tv_fr_carInfo);
		tv_fr_recevDesc = (TextView) findViewById(R.id.tv_fr_recevDesc);
		tv_fr_date = (TextView) findViewById(R.id.tv_fr_date);
		tv_fr_reserveDate = (TextView) findViewById(R.id.tv_fr_reserveDate);
		tv_fr_moveTm = (TextView) findViewById(R.id.tv_fr_moveTm);
		tv_fr_arrivalTm = (TextView) findViewById(R.id.tv_fr_arrivalTm);
		tv_fr_completeTm = (TextView) findViewById(R.id.tv_fr_completeTm);
		tv_fr_contactYn = (TextView) findViewById(R.id.tv_fr_contactYn);
		tv_fr_rescueYn = (TextView) findViewById(R.id.tv_fr_rescueYn);
		tv_fr_rescueTm = (TextView) findViewById(R.id.tv_fr_rescueTm);
		tv_fr_aSCd = (TextView) findViewById(R.id.tv_fr_aSCd);
		tv_fr_cbsCd1 = (TextView) findViewById(R.id.tv_fr_cbsCd1);
		tv_fr_cbsCd2  = (TextView) findViewById(R.id.tv_fr_cbsCd2);
		tv_fr_cbsCd3 = (TextView) findViewById(R.id.tv_fr_cbsCd3);
		tv_fr_faultCd = (TextView) findViewById(R.id.tv_fr_faultCd);
		tv_fr_procCd = (TextView) findViewById(R.id.tv_fr_procCd);
		tv_fr_dutyCd = (TextView) findViewById(R.id.tv_fr_dutyCd);
		
		
		
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		btn_fr_picture.setOnClickListener(this);
		btn_fr_register.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_popClose:
			dismiss();
			break;
		case R.id.btn_fr_picture:
			
			CM_ReadPicture wo14 = new CM_ReadPicture(context, workTargetData.getJobNO() ,workTargetData.getWorkDt(),"1");
			wo14.show();
			wo14.inqueryImages();
			break;
		case R.id.btn_fr_register:
			final WO_TS00_M12P wo12 = new WO_TS00_M12P(context,workTargetData,item04);
			wo12.show();
			wo12.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					resultOk = wo12.getResult();
					if(wo12.getResult()){
						dismiss();
					}
					else{
						
					}
				}

			});
			break;
		}
	}

	
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	private WO_TS00_R00_ITEM03 workTargetData;
	private WO_TS00_R00_ITEM04 item04;
	
	private WO_TS00_R03P_ITEM01 selectedItem03;


	private List<WO_TS00_R04P_ITEM01> selectedItemList04;
	
	
	private WO_TS00_R07P_ITEM01 selectedItem07;
	
	private WO_TS00_R08P_ITEM01 selectedItem08;
	
	
	private WO_TS00_R09P_ITEM01 selectedItem09;
	
	private boolean resultOk;
	public boolean getResult(){
		return resultOk;
	}
	
	public WO_TS00_R00_ITEM03 getWorkTargetData(){
		return workTargetData;
	}
	
}
