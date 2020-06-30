package com.jinsit.kmec.IP.JS;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IP_JS00_R01P_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IP_JS00_R01P_Item> dataList = null;

	public IP_JS00_R01P_Adapter(){};
	public IP_JS00_R01P_Adapter(Context context, int layout, List<IP_JS00_R01P_Item> dataList) {
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
		
		private TextView workNm;
		private TextView fromToTotalTime;
		private TextView deptNm;
		private TextView BldgNm;
		private TextView carNo;
		
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IP_JS00_R01P_Item item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.workNm  			= (TextView) cView.findViewById(R.id.tv01_js_adapter_workNm);
			holder.fromToTotalTime  = (TextView) cView.findViewById(R.id.tv02_js_adapter_fromToTotalTime);
			holder.deptNm  			= (TextView) cView.findViewById(R.id.tv03_js_adapter_deptNm);
			holder.BldgNm  			= (TextView) cView.findViewById(R.id.tv05_js_adapter_BldgNm);
			holder.carNo   			= (TextView) cView.findViewById(R.id.tv06_js_adapter_carNo);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.workNm.setText( item.getWorkNm() );
		holder.fromToTotalTime.setText( item.getFromToTotalTime() );
		holder.deptNm.setText( item.getDeptNm() );
		holder.BldgNm.setText( item.getBldgNm() );
		holder.carNo.setText( item.getCarNo() );
		
		return cView;
	}

};