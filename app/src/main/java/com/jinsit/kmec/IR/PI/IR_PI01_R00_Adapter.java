package com.jinsit.kmec.IR.PI;

import java.util.List;

import com.jinsit.kmec.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IR_PI01_R00_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IR_PI01_R00_Item> dataList = null;

	public IR_PI01_R00_Adapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI01_R00_Adapter(Context context, int layout,
			List<IR_PI01_R00_Item> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView partsDt;
		private TextView partsNo;
		private TextView repSt;
		
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
		final IR_PI01_R00_Item item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.partsDt = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_partsDt);
			holder.partsNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_partsNo);
			holder.repSt = (TextView) convertView.findViewById(R.id.tv_pi_adapter_repSt);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.partsDt.setText(item.getPartsDt());
		holder.partsNo.setText(item.getPartsNo());
		holder.repSt.setText(item.getRepSt());

		return convertView;
	}

}
