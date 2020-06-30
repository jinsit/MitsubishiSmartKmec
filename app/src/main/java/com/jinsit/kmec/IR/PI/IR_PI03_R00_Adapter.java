package com.jinsit.kmec.IR.PI;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_PI03_R00_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IR_PI03_R00_Item> dataList = null;
	private boolean isLogis = false;
	public IR_PI03_R00_Adapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI03_R00_Adapter(Context context, int layout,
			List<IR_PI03_R00_Item> dataList, boolean isLogis) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.isLogis = isLogis;
	}

	private class ViewHolder {
		private TextView itemNo;
		private TextView itemNm;
		private TextView drawNo;
		private TextView qty;
		private TextView stockCd;
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
		final IR_PI03_R00_Item item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.itemNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_itemNo);
			holder.itemNm = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_itemNm);
			holder.drawNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_drawNo);
			holder.qty = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_qty);

			holder.stockCd = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_stockCd);

			if(isLogis){
				holder.stockCd.setVisibility(View.GONE);
				holder.qty.setVisibility(View.VISIBLE);
			}else{
				holder.stockCd.setVisibility(View.GONE);
				holder.qty.setVisibility(View.GONE);
			}
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.itemNo.setText(item.getItemNo());
		holder.itemNm.setText(item.getItemNm());
		holder.drawNo.setText(item.getDrawNo());
		//창고별조회일 경우에만
		if(isLogis){
			holder.qty.setText("수량: " + item.getQty());
			//holder.stockCd.setText(item.getStockCd());
		}
		return convertView;
	}

}