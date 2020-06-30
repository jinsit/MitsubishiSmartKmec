package com.jinsit.kmec.IR.PI;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_PI03_R01P_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IR_PI03_R01P_Item02> dataList = null;

	public IR_PI03_R01P_Adapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI03_R01P_Adapter(Context context, int layout,
								List<IR_PI03_R01P_Item02> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView stockNm;  //창고명
		private TextView qty;   //재고수량
		private TextView irrQty;  //선출고 수량\
		private TextView location; //재고위치

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
		final IR_PI03_R01P_Item02 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.stockNm = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_stockNm);
			holder.qty = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_qty);
			holder.irrQty = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_irrQty);
			holder.location = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_location);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.stockNm.setText(item.getStockNm());
		holder.qty.setText(item.getQty());
		holder.irrQty.setText(item.getIrrQty());
		holder.location.setText(item.getLocation());
		return convertView;
	}

}