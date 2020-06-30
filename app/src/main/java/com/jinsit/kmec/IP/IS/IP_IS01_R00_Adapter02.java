package com.jinsit.kmec.IP.IS;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IP_IS01_R00_Adapter02 extends BaseAdapter {

	Context context;
	int layout;
	List<IP_IS01_R00_Item02> dataList = null;

	public IP_IS01_R00_Adapter02(){};
	public IP_IS01_R00_Adapter02(Context context, int layout, List<IP_IS01_R00_Item02> dataList) {
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
		
		private TextView bldgNm;
		private TextView endVal;
		private TextView dCntAndtCnt;
		
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IP_IS01_R00_Item02 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.bldgNm  		= (TextView) cView.findViewById(R.id.tv01_is_adapter02_bldgNm);
			holder.endVal  		= (TextView) cView.findViewById(R.id.tv02_is_adapter02_endVal);
			holder.dCntAndtCnt  = (TextView) cView.findViewById(R.id.tv03_is_adapter02_figures);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.bldgNm.setText( item.getBldgNm() );
		holder.endVal.setText( item.getEndVal() + " " + item.getdCnt()+"/"+item.gettCnt());
		//holder.dCntAndtCnt.setText( item.getdCnt()+"/"+item.gettCnt() );
		return cView;
	}

};