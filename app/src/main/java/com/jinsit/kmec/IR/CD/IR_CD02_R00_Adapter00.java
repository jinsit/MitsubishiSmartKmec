package com.jinsit.kmec.IR.CD;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;


/**
 * 다발고장 조회 RepeatFailure RF 1번 Adapter
 * @author Won
 *
 */


public class IR_CD02_R00_Adapter00 extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_cbs02r2DateEtc, tv_cbs02r2Status,tv_cbs02r2Status2;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<IR_CD02_R00_ITEM00> NotiListItems;;
	private ArrayList<IR_CD02_R00_ITEM00> OrgNotiListItems;;

	public IR_CD02_R00_Adapter00(Context context,
								 ArrayList<IR_CD02_R00_ITEM00> receiptListItems) {
		super();
		this.mContext = context;

		setReceiptListItems(receiptListItems);
	}

	public void setReceiptListItems(ArrayList<IR_CD02_R00_ITEM00> value) {
		this.OrgNotiListItems = value;
		this.NotiListItems = new ArrayList<IR_CD02_R00_ITEM00>();

		for (IR_CD02_R00_ITEM00 receiptListItem : this.OrgNotiListItems) {
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
			convertView = inflater.inflate(R.layout.ir_cd02_r02_adapter, null);
			holder.tv_cbs02r2DateEtc = (TextView)convertView.findViewById(R.id.tv_cbs02r2DateEtc);
			holder.tv_cbs02r2Status = (TextView)convertView.findViewById(R.id.tv_cbs02r2Status);
			holder.tv_cbs02r2Status2 = (TextView)convertView.findViewById(R.id.tv_cbs02r2Status2 );


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IR_CD02_R00_ITEM00 mData = NotiListItems.get(position);
		holder.tv_cbs02r2DateEtc.setText(mData.getRECEV_TM() + " " +  mData.getSTATUS_CD());
		holder.tv_cbs02r2Status.setText(mData.getCBS_CD_1() + "-" + mData.getCBS_CD_2() + "-" + mData.getCBS_CD_3());
		holder.tv_cbs02r2Status2.setText(mData.getFAULT_CD() + "-" + mData.getPROC_CD() + "-" + mData.getDUTY_CD());


		return convertView;
	}



}
