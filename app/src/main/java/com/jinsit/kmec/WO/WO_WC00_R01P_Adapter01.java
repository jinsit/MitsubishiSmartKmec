package com.jinsit.kmec.WO;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class WO_WC00_R01P_Adapter01 extends BaseAdapter {

	private Context context;
	public ArrayList<WO_WC00_R01P_ITEM01> dataList;;
	private int layout;

	public WO_WC00_R01P_Adapter01(Context context, int layout,
			ArrayList<WO_WC00_R01P_ITEM01> dataList) {
		super();
		this.context = context;
		this.dataList = dataList;
		this.layout = layout;
	}

	private class ViewHolder {
		TextView deptNm;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final WO_WC00_R01P_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.deptNm= (TextView) convertView
					.findViewById(R.id.tv_wt_adapter_deptNm);
		
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.deptNm.setText(item.getDeptNm());
		return convertView;
	}
}
