package com.jinsit.kmec.DM.DM;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class DM_DM03_R00_Adapter02 extends BaseAdapter {

	Context context;
	int layout;
	private List<DM_DM03_R00_ITEM02> dataList = null;

	public DM_DM03_R00_Adapter02() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DM_DM03_R00_Adapter02(Context context, int layout,
								 List<DM_DM03_R00_ITEM02> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView empNm;
		private TextView otTime;
		private TextView approvalYnNm;

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
		final DM_DM03_R00_ITEM02 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.empNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_empNm);
			holder.otTime = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_ot);
			holder.approvalYnNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_approvalYnNm);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.empNm.setText(item.getEmpNm());



		String otTime = "특별 " + String.valueOf(item.getSpecialOt()) +" 분 / 계 " +String.valueOf(item.getTotalOt()) +" 분";

		holder.otTime.setText(otTime);
		holder.approvalYnNm.setText(item.getApprovalYnNm());
		return convertView;
	}

}