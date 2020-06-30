package com.jinsit.kmec.IR.CI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_CI03_R01P_Adapter01 extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_ci_deppDetail;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<IR_CI03_R01P_ITEM02> NotiListItems;;
	private ArrayList<IR_CI03_R01P_ITEM02> OrgNotiListItems;;

	public IR_CI03_R01P_Adapter01(Context context,
			ArrayList<IR_CI03_R01P_ITEM02> receiptListItems) {
		super();
		this.mContext = context;
		
		setReceiptListItems(receiptListItems);
	}

	public void setReceiptListItems(ArrayList<IR_CI03_R01P_ITEM02> value) {
		this.OrgNotiListItems = value;
		this.NotiListItems = new ArrayList<IR_CI03_R01P_ITEM02>();

		for (IR_CI03_R01P_ITEM02 receiptListItem : this.OrgNotiListItems) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.NotiListItems.add(receiptListItem);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return NotiListItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return NotiListItems.get(position);
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
			convertView = inflater.inflate(R.layout.ir_ci03_r01p_adapter, null);
			holder.tv_ci_deppDetail = (TextView)convertView.findViewById(R.id.tv_ci_deppDetail);
		
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IR_CI03_R01P_ITEM02 mData = NotiListItems.get(position);
		holder.tv_ci_deppDetail.setText(mData.getMONTH_DESC());
		return convertView;
	}

	

}
