package com.jinsit.kmec.IR.CI;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_CI03_R00_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<IR_CI03_R00_ITEM01> dataList = null;
	
	public IR_CI03_R00_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_CI03_R00_Adapter01(Context context, int layout,
			List<IR_CI03_R00_ITEM01> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView workDt;
		private TextView st;
		private TextView jobST;
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
		final IR_CI03_R00_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.workDt = (TextView) convertView
					.findViewById(R.id.tv_ci_adapter_workDt);
			holder.st = (TextView) convertView
					.findViewById(R.id.tv_ci_adapter_st);
			holder.jobST = (TextView) convertView.findViewById(R.id.tv_ci_adapter_jobST);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.workDt.setText(item.getWorkDt());
		holder.st.setText(item.getSt());
		holder.jobST.setText(item.getJobST());
		return convertView;
	}

}