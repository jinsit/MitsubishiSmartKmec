package com.jinsit.kmec.IR.CD;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_CD03_R00_Adapter extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_cbs03DateService, tv_cbs03BldgNm, tv_cbs03CarNo;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<IR_CD03_R00_ITEM> NotiListItems;;
	private ArrayList<IR_CD03_R00_ITEM> OrgNotiListItems;;

	public IR_CD03_R00_Adapter(Context context,
			ArrayList<IR_CD03_R00_ITEM> receiptListItems) {
		super();
		this.mContext = context;
		
		setReceiptListItems(receiptListItems);
	}

	public void setReceiptListItems(ArrayList<IR_CD03_R00_ITEM> value) {
		this.OrgNotiListItems = value;
		this.NotiListItems = new ArrayList<IR_CD03_R00_ITEM>();

		for (IR_CD03_R00_ITEM receiptListItem : this.OrgNotiListItems) {
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
			convertView = inflater.inflate(R.layout.ir_cd03_r01_adapter, null);
			
			holder.tv_cbs03DateService = (TextView)convertView.findViewById(R.id.tv_cbs03DateService);
			holder.tv_cbs03BldgNm = (TextView)convertView.findViewById(R.id.tv_cbs03BldgNm);
			holder.tv_cbs03CarNo = (TextView)convertView.findViewById(R.id.tv_cbs03CarNo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IR_CD03_R00_ITEM mData = NotiListItems.get(position);
		holder.tv_cbs03DateService.setText(mData.getRECEV_TM() + " " + mData.getORDER_NM());
		holder.tv_cbs03BldgNm.setText(mData.getBLDG_NM());
		holder.tv_cbs03CarNo.setText(mData.getCAR_NO());
		return convertView;
	}

	

}
