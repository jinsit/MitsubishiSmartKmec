package com.jinsit.kmec.WO.WT.TS;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class WO_TS00_R09P_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<WO_TS00_R09P_ITEM01> dataList = null;

	public WO_TS00_R09P_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WO_TS00_R09P_Adapter01(Context context, int layout,
			List<WO_TS00_R09P_ITEM01> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView dutyNm;
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
		final WO_TS00_R09P_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.dutyNm = (TextView) convertView
					.findViewById(R.id.tv_fr_adapter_dutyNm);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.dutyNm.setText(item.getDutyNm());
		return convertView;
	}

}