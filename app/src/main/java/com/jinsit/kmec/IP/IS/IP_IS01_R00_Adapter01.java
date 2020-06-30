package com.jinsit.kmec.IP.IS;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;

public class IP_IS01_R00_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<IP_IS01_R00_Item01> dataList = null;
	View.OnClickListener updateClicklistener;
	public IP_IS01_R00_Adapter01(){};
	public IP_IS01_R00_Adapter01(Context context, int layout, List<IP_IS01_R00_Item01> dataList, View.OnClickListener updateClicklistener) {
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.updateClicklistener = updateClicklistener;
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
		
		private TextView ymd;
		private TextView eleNo;
		private TextView bldgNm;
		private TextView updateDt;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IP_IS01_R00_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.ymd  	= (TextView) cView.findViewById(R.id.tv01_is_adapter_ymd);
			holder.eleNo  	= (TextView) cView.findViewById(R.id.tv02_is_adapter_eleNo);
			holder.bldgNm  	= (TextView) cView.findViewById(R.id.tv03_is_adapter_bldgNm);
			holder.updateDt = (TextView) cView.findViewById(R.id.tv_is_adapter_updateDt);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.ymd.setText( item.getYmd() );
		holder.eleNo.setText( item.getEleNo() );
		holder.bldgNm.setText( item.getBldgNm() );

		holder.updateDt.setOnClickListener(updateClicklistener);

		holder.updateDt.setTag(R.id.tag_ip_is00_job_no, item.getJobNo());	//작업 번호
		holder.updateDt.setTag(R.id.tag_ip_is00_work_dt, item.getYmd());	//기존 미처리 작업일자
		holder.updateDt.setTag(R.id.tag_ip_is00_bldg_nm, item.getBldgNm());	//현장명
		holder.updateDt.setTag(R.id.tag_ip_is00_user_id, item.getEmpId());	//기존 미처리 작업자


		return cView;
	}

};