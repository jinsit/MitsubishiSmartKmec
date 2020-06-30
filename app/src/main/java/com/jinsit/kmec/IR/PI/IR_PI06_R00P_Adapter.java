package com.jinsit.kmec.IR.PI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.CM.SearchDeptResData;
import com.jinsit.kmec.R;

import java.util.ArrayList;

public class IR_PI06_R00P_Adapter extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_pi06_nm;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<IR_PI06_R00_Item> ir_pi06_r00_items;;

	public IR_PI06_R00P_Adapter(Context context,
							   ArrayList<IR_PI06_R00_Item> resData) {
		super();
		this.mContext = context;
		setReceiptListItems(resData);
	}


	public void setReceiptListItems(ArrayList<IR_PI06_R00_Item> value) {
		this.ir_pi06_r00_items = value;

		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ir_pi06_r00_items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ir_pi06_r00_items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.ir_pi06_r00p_adapter, null);
			holder.tv_pi06_nm = (TextView)convertView.findViewById(R.id.tv_pi06_nm);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IR_PI06_R00_Item mData = ir_pi06_r00_items.get(position);
		holder.tv_pi06_nm.setText(mData.getNM());
		return convertView;
	}



}
