package com.jinsit.kmec.IR.PI;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

public class IR_PI04_R00_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IR_PI04_R00_Item> dataList = null;

	private OnClickListener onClickListener;
	public IR_PI04_R00_Adapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI04_R00_Adapter(Context context, int layout,
			List<IR_PI04_R00_Item> dataList, OnClickListener onClickListener) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.onClickListener = onClickListener;
	}

	private class ViewHolder {
		private TextView itemNo;
		private TextView itemNm;
		private TextView size;
		private TextView unitPrc;
		private TextView image;

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
		final IR_PI04_R00_Item item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.itemNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_itemNo);
			holder.itemNm = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_itemNm);
			holder.size = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_size);
			holder.unitPrc = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_unitPrc);
			holder.image = (TextView) convertView
					.findViewById(R.id.btn_pi_adapter_image);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(onClickListener != null){
			holder.image.setTag(item.getItemNo());
			holder.image.setOnClickListener(onClickListener);
		}

		holder.itemNo.setText(item.getItemNo());
		holder.itemNm.setText(item.getItemNm());
		holder.size.setText(item.getSize());
		holder.unitPrc.setText(SysUtil.makeStringWithComma(item.getUnitPrc(),true));
		if(item.getImgChk().equals("0"))
		{
			holder.image.setVisibility(View.GONE);
		}
		else if(item.getImgChk().equals("1"))
		{
			holder.image.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.image.setVisibility(View.GONE);
		}

		return convertView;
	}

}