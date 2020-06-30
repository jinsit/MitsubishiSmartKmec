package com.jinsit.kmec.WO.WT.TS;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jinsit.kmec.R;

/**
 * 고장수리
 */
public class WO_TS00_M10P extends Dialog implements OnClickListener, OnDismissListener{

	public WO_TS00_M10P(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private Context context;
	
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private EditText et_fr_code1;
	private EditText et_fr_code2;
	private EditText et_fr_code3;
	private EditText et_fr_code4;
	private EditText et_fr_code5;
	
	private TextView btn_fr_ok;
	private List<String> cpErrorCodeList;

	private ProgressDialog progress;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wo_ts00_m10p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("Cp Error 코드");
		cpErrorCodeList = new ArrayList<String>();
		
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		
		et_fr_code1 =  (EditText)findViewById(R.id.et_fr_code1);
		et_fr_code2 =  (EditText)findViewById(R.id.et_fr_code2);
		et_fr_code3 =  (EditText)findViewById(R.id.et_fr_code3);
		et_fr_code4 =  (EditText)findViewById(R.id.et_fr_code4);
		et_fr_code5 =  (EditText)findViewById(R.id.et_fr_code5);
		btn_fr_ok = (TextView)findViewById(R.id.btn_fr_ok);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		btn_fr_ok.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_popClose:
			cpErrorCodeList.clear();
			dismiss();
			break;
		case R.id.btn_fr_ok:
			if(!et_fr_code1.getText().toString().equals("")){
				cpErrorCodeList.add(et_fr_code1.getText().toString());
			}
			if(!et_fr_code2.getText().toString().equals("")){
				cpErrorCodeList.add(et_fr_code2.getText().toString());
			}

			if(!et_fr_code3.getText().toString().equals("")){
				cpErrorCodeList.add(et_fr_code3.getText().toString());
			}
			
			if(!et_fr_code4.getText().toString().equals("")){
				cpErrorCodeList.add(et_fr_code4.getText().toString());
			}
			
			if(!et_fr_code5.getText().toString().equals("")){
				cpErrorCodeList.add(et_fr_code5.getText().toString());
			}
			dismiss();
			break;
		}
	}

	
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	
	public List<String> getCpErrorCodeList(){
		return this.cpErrorCodeList;
	}
	
}
