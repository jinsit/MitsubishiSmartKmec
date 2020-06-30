package com.jinsit.kmec.IR.EDS;

import java.util.List;

import com.jinsit.kmec.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IR_EDS00_R00P_Adapter01 extends BaseAdapter {

	private Context context;
	private List<IR_EDS00_R00P_ITEM01> dataList = null;

	public IR_EDS00_R00P_Adapter01(){};
	public IR_EDS00_R00P_Adapter01(Context context, List<IR_EDS00_R00P_ITEM01> dataList) {
		this.context = context;
		this.dataList = dataList;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder{
		
		TextView tv_edsAdapter01_eduNm;
		TextView tv_edsAdapter01_applyCarCd;
		TextView tv_edsAdapter01_eduToDt;
		TextView tv_edsAdapter01_score;
	}
	
	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IR_EDS00_R00P_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, R.layout.ir_eds00_r00p_adapter01, null);
			
			holder = new ViewHolder();
			holder.tv_edsAdapter01_eduNm = (TextView) cView.findViewById(R.id.tv_edsAdapter01_eduNm);
			holder.tv_edsAdapter01_applyCarCd  = (TextView) cView.findViewById(R.id.tv_edsAdapter01_applyCarCd);
			holder.tv_edsAdapter01_eduToDt = (TextView) cView.findViewById(R.id.tv_edsAdapter01_eduToDt);
			holder.tv_edsAdapter01_score = (TextView) cView.findViewById(R.id.tv_edsAdapter01_score);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.tv_edsAdapter01_eduNm.setText( item.getEDU_NM() );
		holder.tv_edsAdapter01_applyCarCd.setText( item.getAPPLY_CAR_CD() );
		holder.tv_edsAdapter01_eduToDt.setText( item.getEDU_TO_DT() );
		holder.tv_edsAdapter01_score.setText( item.getSCORE() );
		
		
		return cView;
	}

};
