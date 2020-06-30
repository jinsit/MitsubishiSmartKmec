package com.jinsit.kmec.IP.IS;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IP_IS01_R01P_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<IP_IS01_R01P_Item01> dataList = null;

	public IP_IS01_R01P_Adapter01(){};
	public IP_IS01_R01P_Adapter01(Context context, int layout, List<IP_IS01_R01P_Item01> dataList) {
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
		private TextView jobStNm;
		private TextView planWorkDt;
		private TextView workDt;
		private TextView empNm;
		
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IP_IS01_R01P_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.carNo  		= (TextView) cView.findViewById(R.id.tv01_is_p01_carNo_value);
			holder.jobStNm  	= (TextView) cView.findViewById(R.id.tv02_is_p01_jobStNm_value);
			holder.planWorkDt  	= (TextView) cView.findViewById(R.id.tv03_is_p01_planWorkDt_value);
			holder.workDt  		= (TextView) cView.findViewById(R.id.tv04_is_p01_workDt_value);
			holder.empNm  		= (TextView) cView.findViewById(R.id.tv05_is_p01_empNm_value);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.carNo.setText( item.getCarNo() );
		holder.jobStNm.setText( item.getJobStNm() );
		holder.planWorkDt.setText( item.getPlanWorkDt() );
		holder.workDt.setText( item.getWorkDt() );
		holder.empNm.setText( item.getEmpNm() );
		return cView;
	}

};