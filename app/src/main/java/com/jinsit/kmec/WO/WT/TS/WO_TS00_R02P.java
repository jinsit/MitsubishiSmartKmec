package com.jinsit.kmec.WO.WT.TS;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.StringUtil;

/**
 * 고장수리
 */
public class WO_TS00_R02P extends AlertDialog implements OnClickListener, OnDismissListener{

	public WO_TS00_R02P(Context context, WO_TS00_R03P_ITEM01 selectedItem03, List<WO_TS00_R04P_ITEM01> selectedItemList04,WO_TS00_R07P_ITEM01 selectedItem07,WO_TS00_R08P_ITEM01 selectedItem08,WO_TS00_R09P_ITEM01 selectedItem09) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.selectedItem03 = selectedItem03;
		this.selectedItemList04 = selectedItemList04;
		this.selectedItem07 = selectedItem07;
		this.selectedItem08 = selectedItem08;
		this.selectedItem09 = selectedItem09;
	}

	private Context context;
	
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private TextView btn_cm_ok;
	private TextView btn_fr_trouble;
    private TextView btn_fr_service;

	private TextView btn_fr_code;
	private TextView btn_fr_cbsCode1;
	private TextView tv_fr_cbsCode2;
	private TextView tv_fr_cbsCode3;
	
	private TextView btn_fr_faultCode;
	private TextView btn_fr_procCode;
	
	private TextView btn_fr_dutyCode;
	
	private String selCode;
	private static String TAB_TROUBLE ="01";
	private static String TAB_SERVICE ="00";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_ts00_r02p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("A/S 처리코드");
		selCode = "01";
		btnSelect("01");
		if(this.selectedItem03 != null){
			this.setselectedItem03(selectedItem03);
		}
		if(this.selectedItemList04.size() > 0){
			this.setselectedItemList04(selectedItemList04);
		}
		if(this.selectedItem07 != null){
			this.setselectedItem07(selectedItem07);
		}
		if(this.selectedItem08 != null){
			this.setselectedItem08(selectedItem08);
		}
		if(this.selectedItem09 != null){
			this.setselectedItem09(selectedItem09);
		}
	
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		btn_cm_ok = (TextView) findViewById(R.id.btn_cm_ok);
		btn_fr_trouble  = (TextView) findViewById(R.id.btn_fr_trouble);
		btn_fr_service = (TextView)findViewById(R.id.btn_fr_service); 
		btn_fr_code = (TextView)findViewById(R.id.btn_fr_code); 
		btn_fr_cbsCode1 = (TextView)findViewById(R.id.btn_fr_cbsCode1);
		tv_fr_cbsCode2 = (TextView)findViewById(R.id.tv_fr_cbsCode2);
		tv_fr_cbsCode3 = (TextView)findViewById(R.id.tv_fr_cbsCode3);
		btn_fr_faultCode = (TextView)findViewById(R.id.btn_fr_faultCode);
		btn_fr_procCode = (TextView)findViewById(R.id.btn_fr_procCode);
		btn_fr_dutyCode = (TextView)findViewById(R.id.btn_fr_dutyCode);
		setEvents();
	}

	protected void setEvents() {
		btn_cm_ok.setOnClickListener(this);
		btn_popClose.setOnClickListener(this);
		btn_fr_trouble.setOnClickListener(this);
		btn_fr_service.setOnClickListener(this);
		btn_fr_code.setOnClickListener(this);
		btn_fr_cbsCode1.setOnClickListener(this);
		btn_fr_faultCode.setOnClickListener(this);
		btn_fr_procCode.setOnClickListener(this);
		btn_fr_dutyCode.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cm_ok:
			SimpleDialog sm01;
			if(this.selectedItem03 == null){
				if(selCode.equals("01")){
					sm01 = new SimpleDialog(context, "알림","상태코드를  넣어주세요");
				}
				else
				{
					sm01 = new SimpleDialog(context, "알림","고장 제외 코드를  넣어주세요");
				}
				sm01.show();
				return;
			}else if(this.selectedItemList04.size() != 3){
				sm01 = new SimpleDialog(context, "알림","부위코드를  넣어주세요");
				sm01.show();
			}else if(this.selectedItem07 == null ){
				sm01 = new SimpleDialog(context, "알림","불량코드를  넣어주세요");
				sm01.show();
			}else if(this.selectedItem08 == null ){
				sm01 = new SimpleDialog(context, "알림","처리코드를  넣어주세요");
				sm01.show();
			}else if(this.selectedItem09 == null ){
				sm01 = new SimpleDialog(context, "알림","책임코드를  넣어주세요");
				sm01.show();
			}
			dismiss();
			break;
		case R.id.btn_popClose:
			selectedItem03 = null;
			selectedItemList04 = null;
			this.selectedItem07 = null;
			this.selectedItem08 = null;
			this.selectedItem09 = null;
			dismiss();
			break;
		case R.id.btn_fr_trouble:
			btnSelect(TAB_TROUBLE);
			btn_fr_code.setText("상태코드");
			break;
		case R.id.btn_fr_service:
			btn_fr_code.setText("일반 서비스");
			btnSelect(TAB_SERVICE);
			break;
		case R.id.btn_fr_code:
			final WO_TS00_R03P wo01 = new WO_TS00_R03P(context, selCode);
			wo01.show();
			wo01.inqueryTroubleServiceCode();
			wo01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_TS00_R03P_ITEM01 item = wo01.getCurrentselectedItem();
					if (item == null) {
					} else {
						setselectedItem03(item);
					}
				}

			});
			break;
		case R.id.btn_fr_cbsCode1:
			final WO_TS00_R04P wo04 = new WO_TS00_R04P(context);
			wo04.show();
			wo04.inqueryCBSCode1();
			wo04.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					List<WO_TS00_R04P_ITEM01> itemList = wo04.getCurrentselectedItemList();
					if (itemList == null || itemList.size() == 0) {
					} else {
						setselectedItemList04(itemList);
					}
				}

			});
			break;
		case R.id.btn_fr_faultCode:
			final WO_TS00_R07P wo07 = new WO_TS00_R07P(context);
			wo07.show();
			wo07.inqueryFaultCode();
			wo07.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_TS00_R07P_ITEM01 item = wo07.getCurrentselectedItem();
					if (item == null || item.equals("")) {
					} else {
						setselectedItem07(item);
					}
				}

			});
			break;
		case R.id.btn_fr_procCode:
			final WO_TS00_R08P wo08 = new WO_TS00_R08P(context);
			wo08.show();
			wo08.inqueryProcCode();
			wo08.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_TS00_R08P_ITEM01 item = wo08.getCurrentselectedItem();
					if (item == null || item.equals("")) {
					} else {
						setselectedItem08(item);
					}
				}

			});
			break;
		case R.id.btn_fr_dutyCode:
			final WO_TS00_R09P wo09 = new WO_TS00_R09P(context);
			wo09.show();
			wo09.inqueryDutyCode();
			wo09.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_TS00_R09P_ITEM01 item = wo09.getCurrentselectedItem();
					if (item == null || item.equals("")) {
					} else {
						setselectedItem09(item);
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
	
	
	private void btnSelect(String idx) {
		selCode = idx;
		btn_fr_trouble.setBackgroundResource(R.drawable.tab_ts_breakdown_off);
		btn_fr_service.setBackgroundResource(R.drawable.tab_ts_service_off);

		btn_fr_trouble.setEnabled(true);
		btn_fr_service.setEnabled(true);
		if(idx.equals(TAB_TROUBLE)){
			btn_fr_trouble.setBackgroundResource(R.drawable.tab_ts_breakdown_on);
			btn_fr_trouble.setEnabled(false);
		}
		else if(idx.equals(TAB_SERVICE)){
			btn_fr_service.setBackgroundResource(R.drawable.tab_ts_service_on);
			btn_fr_service.setEnabled(false);
		}
		
	}
	
	public String getSelCode(){
		return selCode;
	}

	private WO_TS00_R03P_ITEM01 selectedItem03;
	public WO_TS00_R03P_ITEM01 getSelectedItem03(){
		return selectedItem03;
	}
	public void setselectedItem03(WO_TS00_R03P_ITEM01 item) {
		selectedItem03 = item;
		String btnText = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_code), item.getCodeNm());
		this.btn_fr_code.setText(btnText);
	}

	private List<WO_TS00_R04P_ITEM01> selectedItemList04;
	public List<WO_TS00_R04P_ITEM01> getSelectedItemList04(){
		return selectedItemList04;
	}
	public void setselectedItemList04(List<WO_TS00_R04P_ITEM01> itemList) {
		selectedItemList04 = itemList;
		String btnText1 = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_cbsCode1), selectedItemList04.get(0).getAssyNm());
		this.btn_fr_cbsCode1.setText(btnText1);
		String btnText2 = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_cbsCode2), selectedItemList04.get(1).getAssyNm());
		this.tv_fr_cbsCode2.setText(btnText2);
		String btnTex3 = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_cbsCode3), selectedItemList04.get(2).getAssyNm());
		this.tv_fr_cbsCode3.setText(btnTex3);
	}
	
	private WO_TS00_R07P_ITEM01 selectedItem07;
	public WO_TS00_R07P_ITEM01 getSelectedItem07(){
		return selectedItem07;
	}
	public void setselectedItem07(WO_TS00_R07P_ITEM01 item) {
		this.selectedItem07 = item;
		String btnText = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_faultCode), item.getFaultNm());
		this.btn_fr_faultCode.setText(btnText);
	}
	
	private WO_TS00_R08P_ITEM01 selectedItem08;
	public WO_TS00_R08P_ITEM01 getSelectedItem08(){
		return selectedItem08;
	}
	public void setselectedItem08(WO_TS00_R08P_ITEM01 item) {
		this.selectedItem08 = item;
		String btnText = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_procCode), item.getProcNm());
		this.btn_fr_procCode.setText(btnText);
	}
	
	private WO_TS00_R09P_ITEM01 selectedItem09;
	public WO_TS00_R09P_ITEM01 getSelectedItem09(){
		return selectedItem09;
	}
	public void setselectedItem09(WO_TS00_R09P_ITEM01 item) {
		this.selectedItem09 = item;
		String btnText = StringUtil.padLeft(
				context.getString(R.string.btnstr_fr_dutyCode), item.getDutyNm());
		this.btn_fr_dutyCode.setText(btnText);
	}
}
