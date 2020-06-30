package com.jinsit.kmec.DM.DM;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class DM_DM01_R00_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<DM_DM01_R00_ITEM02> dataList = null;

	public DM_DM01_R00_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DM_DM01_R00_Adapter01(Context context, int layout,
								 List<DM_DM01_R00_ITEM02> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView bldgNm;
		private TextView carNo;  //호기
		private TextView csTmFrTo;   //작업시간대
		private TextView csTm;  //OT

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
		final DM_DM01_R00_ITEM02 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.bldgNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_bldgNm);
			holder.carNo = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_carNo);
			holder.csTmFrTo = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_csTmFrTo);
			holder.csTm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_csTm);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.bldgNm.setText(item.getBldgNm());
		holder.carNo.setText(item.getCarNo());
		holder.csTmFrTo.setText(item.getCsTmFr());
		holder.csTm.setText(item.getCsTm());
		return convertView;
	}

}