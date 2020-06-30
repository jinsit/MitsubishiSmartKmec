package com.jinsit.kmec.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jinsit.kmec.R;


public class WorkObjectTabWidget extends LinearLayout implements OnClickListener{
	Context context;
	LinearLayout navigationMenu;
	 Button btn_tabWT,btn_tabPL,btn_tabCL,btn_tabWC;
	 
	public WorkObjectTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		navigationMenu = (LinearLayout)inflater.inflate(R.layout.widget_workobject_tab,  null);
		addView(navigationMenu);
		btn_tabWT = (Button)findViewById(R.id.btn_tabWT);
		btn_tabPL = (Button)findViewById(R.id.btn_tabPL);
		btn_tabCL = (Button)findViewById(R.id.btn_tabCL);
		btn_tabWC = (Button)findViewById(R.id.btn_tabWC);
		btn_tabWT.setOnClickListener(this);
		btn_tabPL.setOnClickListener(this);
		btn_tabCL.setOnClickListener(this);
		btn_tabWC.setOnClickListener(this);
		
		 
	}
	public interface OnTabPositionListener{
		public void onTabPosition(int position);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.btn_tabWT:
			
			break;
		case R.id.btn_tabPL:
			
			break;
		case R.id.btn_tabCL:
		
			break;
		case R.id.btn_tabWC:
		break;
			
		}
	}
	
}