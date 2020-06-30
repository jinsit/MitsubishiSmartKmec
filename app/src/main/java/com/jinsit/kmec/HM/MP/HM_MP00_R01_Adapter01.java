package com.jinsit.kmec.HM.MP;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class HM_MP00_R01_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<HM_MP00_R01_Item01> dataList = null;

	public HM_MP00_R01_Adapter01(){};
	public HM_MP00_R01_Adapter01(Context context, int layout, List<HM_MP00_R01_Item01> dataList) {
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
		
		private TextView toolNm;
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final HM_MP00_R01_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.toolNm  		= (TextView) cView.findViewById(R.id.tv_hm_mp00_r01p_toolNm);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.toolNm.setText( item.getToolNm() );
		return cView;
	}

};
