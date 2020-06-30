package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class CM_SearchBldg_Adapter extends BaseAdapter {

	private Context context;
	private int layout;
	public ArrayList<CM_SearchBldgInfo_ITEM01> dataList;

	public CM_SearchBldg_Adapter(Context context, int layout,
			ArrayList<CM_SearchBldgInfo_ITEM01> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		TextView csDeptNm;
		TextView bldg_nm;
		TextView addr;
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
		final CM_SearchBldgInfo_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,layout, null);
				holder.csDeptNm = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_csDeptNm);
				holder.bldg_nm = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_bldg_nm);
				holder.addr = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_addr);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.csDeptNm.setText(item.getCsDetpNm());
		holder.bldg_nm.setText(item.getBldgNm());
		holder.addr.setText(item.getAddr());
		return convertView;
	}
}
