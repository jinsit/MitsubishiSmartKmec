package com.jinsit.kmec.IR.CI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;


public class IR_CI00_R00_F_Widget extends LinearLayout implements OnClickListener{
	private LinearLayout lin_ci_navitionMenu;
	 
	private Context context;
	private TextView btn_ci_basicInfo;
	private TextView btn_ci_troubleHistory;
	private TextView btn_ci_inspectionHistory;
	private TextView btn_ci_equipmentHistory;
	private TextView btn_ci_repaireSales;
	 
	public IR_CI00_R00_F_Widget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lin_ci_navitionMenu = (LinearLayout)inflater.inflate(R.layout.ir_ci00_r00_f_widget,  null);
		addView(lin_ci_navitionMenu);
		
		btn_ci_basicInfo = (TextView)findViewById(R.id.btn_ci_basicInfo);
		btn_ci_troubleHistory = (TextView)findViewById(R.id.btn_ci_troubleHistory);
		btn_ci_inspectionHistory = (TextView)findViewById(R.id.btn_ci_inspectionHistory);
		btn_ci_equipmentHistory= (TextView)findViewById(R.id.btn_ci_equipmentHistory);
		btn_ci_repaireSales = (TextView)findViewById(R.id.btn_ci_repaireSales);
		
		btn_ci_basicInfo.setOnClickListener(this);
		btn_ci_troubleHistory.setOnClickListener(this);
		btn_ci_inspectionHistory.setOnClickListener(this);
		btn_ci_equipmentHistory.setOnClickListener(this);
		btn_ci_repaireSales.setOnClickListener(this);
		
		 
	}
	public interface OnTabPositionListener{
		public void onTabPosition(int position);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.btn_ci_basicInfo:
			
			break;
		case R.id.btn_ci_troubleHistory:
			
			break;
		case R.id.btn_ci_inspectionHistory:
		
			break;
		case R.id.btn_ci_equipmentHistory:
		break;
			
		}
	}
	
}