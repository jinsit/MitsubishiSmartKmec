package com.jinsit.kmec.IR.PI;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_PI02_R00_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IR_PI02_R00_Item> dataList = null;

	public IR_PI02_R00_Adapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI02_R00_Adapter(Context context, int layout,
			List<IR_PI02_R00_Item> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView outDt;
		private TextView carNo;
		private TextView partsNo;
		private TextView itemNm;
		private TextView outQty;

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
		final IR_PI02_R00_Item item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.outDt = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_outDt);
			holder.carNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_carNo);
			holder.partsNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_partsNo);
			holder.itemNm = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_itemNm);
			holder.outQty = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_outQty);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.outDt.setText(item.getOutDt());
		holder.carNo.setText(item.getCarNo());
		holder.partsNo.setText(item.getPartsNo());
		holder.itemNm.setText(item.getItemNm());
		holder.outQty.setText(item.getOutQty() + " / "  + item.getPlQty());

		return convertView;
	}

}