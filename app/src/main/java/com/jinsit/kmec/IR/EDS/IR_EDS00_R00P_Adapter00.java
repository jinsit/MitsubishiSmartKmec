package com.jinsit.kmec.IR.EDS;

import java.util.List;

import com.jinsit.kmec.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IR_EDS00_R00P_Adapter00 extends BaseAdapter {

	private Context context;
	private List<IR_EDS00_R00P_ITEM00> dataList = null;

	public IR_EDS00_R00P_Adapter00(){};
	public IR_EDS00_R00P_Adapter00(Context context, List<IR_EDS00_R00P_ITEM00> dataList) {
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
		
		TextView tv_edsAdapter00_licenseNo;
		TextView tv_edsAdapter00_licenseNm;
		TextView tv_edsAdapter00_eduToDt;
	}
	
	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IR_EDS00_R00P_ITEM00 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, R.layout.ir_eds00_r00p_adapter00, null);
			
			holder = new ViewHolder();
			holder.tv_edsAdapter00_licenseNo = (TextView) cView.findViewById(R.id.tv_edsAdapter00_licenseNo);
			holder.tv_edsAdapter00_licenseNm  = (TextView) cView.findViewById(R.id.tv_edsAdapter00_licenseNm);
			holder.tv_edsAdapter00_eduToDt = (TextView) cView.findViewById(R.id.tv_edsAdapter00_eduToDt);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.tv_edsAdapter00_licenseNo.setText( item.getLICENSE_NO() );
		holder.tv_edsAdapter00_licenseNm.setText( item.getLICENSE_NM() );
		holder.tv_edsAdapter00_eduToDt.setText( item.getEDU_TO_DT() );
		
		return cView;
	}

};
