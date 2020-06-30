package com.jinsit.kmec.IP.IS;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IP_IS02_R01P_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<IP_IS02_R01P_Item01> dataList = null;

	public IP_IS02_R01P_Adapter01(){};
	public IP_IS02_R01P_Adapter01(Context context, int layout, List<IP_IS02_R01P_Item01> dataList) {
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
		
		private TextView jobTime;
		private TextView workNm;
		private TextView carNo;
		private TextView bldgNm;
		private TextView jobStNm_csTmFr;
		private TextView yCnt_tCnt;
		
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IP_IS02_R01P_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.jobTime  			= (TextView) cView.findViewById(R.id.tv_wo_jobTime);
			holder.workNm 				= (TextView) cView.findViewById(R.id.tv_wo_workNm);
			holder.carNo  				= (TextView) cView.findViewById(R.id.tv_wo_carNo);
			holder.bldgNm  				= (TextView) cView.findViewById(R.id.tv_wo_bldgNm);
			holder.jobStNm_csTmFr  		= (TextView) cView.findViewById(R.id.tv_wo_st);
			holder.yCnt_tCnt  			= (TextView) cView.findViewById(R.id.tv_wo_checkSt);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.jobTime.setText( item.getWorkDt() );
		holder.workNm.setText( item.getWorkNm() );
		holder.carNo.setText( item.getCarNo() );
		holder.bldgNm.setText( item.getBldgNm() );
		holder.jobStNm_csTmFr.setText( item.getJobStNm() +" "+ item.getCsTmFr() );
		holder.yCnt_tCnt.setText( item.getyCnt() +"/"+ item.getTCnt() );
		return cView;
	}

};
