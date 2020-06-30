package com.jinsit.kmec.WO.WT.RJ;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

public class WT_RJ01_R01P_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<WT_RJ01_R01P_Item01> dataList = null;

	public WT_RJ01_R01P_Adapter01(){};
	public WT_RJ01_R01P_Adapter01(Context context, int layout, List<WT_RJ01_R01P_Item01> dataList) {
		this.context = context;
		this.layout = layout;
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
		
		private TextView carNo;
		private TextView itemNo;
		private TextView itemNm;
		private TextView upUnitAmt;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final WT_RJ01_R01P_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.carNo  		= (TextView) cView.findViewById(R.id.tv01_rj01_r01p_carNo);
			holder.itemNo  		= (TextView) cView.findViewById(R.id.tv02_rj01_r01p_itemNo);
			holder.itemNm  		= (TextView) cView.findViewById(R.id.tv03_rj01_r01p_itemNm);
			holder.upUnitAmt  	= (TextView) cView.findViewById(R.id.tv04_rj01_r01p_upUnitAmt);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.carNo.setText( item.getCarNo() );
		holder.itemNo.setText( item.getItemNo() );
		holder.itemNm.setText( item.getItemNm() );
		holder.upUnitAmt.setText(SysUtil.makeStringWithComma(item.getUnitPrc(), true) +" * " 
								 + item.getQty()
								 + item.getUnit() + " = "
								 + SysUtil.makeStringWithComma(item.getAmt() ,true)
								);
		return cView;
	}

};
