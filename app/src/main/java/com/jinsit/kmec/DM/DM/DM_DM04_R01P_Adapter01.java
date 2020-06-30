package com.jinsit.kmec.DM.DM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

import java.util.List;

public class DM_DM04_R01P_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<DM_DM04_R01P_ITEM01> dataList = null;

	public DM_DM04_R01P_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DM_DM04_R01P_Adapter01(Context context, int layout,
                                  List<DM_DM04_R01P_ITEM01> dataList) {
		super();
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
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder {
		private TextView tv_dm04_r01p_adapter_week;
		private TextView tv_dm04_r01p_adapter_dayofweek;
		private TextView tv_dm04_r01p_adapter_date;
		private TextView tv_dm04_r01p_adapter_worktime;
	
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DM_DM04_R01P_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.tv_dm04_r01p_adapter_week = (TextView) convertView
					.findViewById(R.id.tv_dm04_r01p_adapter_week);
			holder.tv_dm04_r01p_adapter_dayofweek = (TextView) convertView
					.findViewById(R.id.tv_dm04_r01p_adapter_dayofweek);
			holder.tv_dm04_r01p_adapter_date = (TextView) convertView
					.findViewById(R.id.tv_dm04_r01p_adapter_date);
			holder.tv_dm04_r01p_adapter_worktime = (TextView) convertView
					.findViewById(R.id.tv_dm04_r01p_adapter_worktime);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_dm04_r01p_adapter_week.setText(item.getYEAR_WEEK_NM());
		holder.tv_dm04_r01p_adapter_dayofweek.setText(item.getWEEK_DAY_NM());
		holder.tv_dm04_r01p_adapter_date.setText(item.getWORK_DT());
		holder.tv_dm04_r01p_adapter_worktime.setText(item.getWORK_HH());



		return convertView;
	}

}