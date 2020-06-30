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
 * 다발고장 조회 RepeatFailure RF 2번 ITEM
 * @author Won
 *
 */
public class IR_CD02_R00_Adapter01 extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_cbs02BldgNm, tv_cbs02CarNo,tv_cbs02Repeat;
	}



	private ViewHolder holder;
	private Context mContext;
	public ArrayList<IR_CD02_R00_ITEM01> NotiListItems;;
	private ArrayList<IR_CD02_R00_ITEM01> OrgNotiListItems;;

	public IR_CD02_R00_Adapter01(Context context,
								 ArrayList<IR_CD02_R00_ITEM01> receiptListItems) {
		super();
		this.mContext = context;

		setReceiptListItems(receiptListItems);
	}

	public void setReceiptListItems(ArrayList<IR_CD02_R00_ITEM01> value) {
		this.OrgNotiListItems = value;
		this.NotiListItems = new ArrayList<IR_CD02_R00_ITEM01>();

		for (IR_CD02_R00_ITEM01 receiptListItem : this.OrgNotiListItems) {
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
			convertView = inflater.inflate(R.layout.ir_cd02_r01_adapter, null);

			holder.tv_cbs02BldgNm = (TextView)convertView.findViewById(R.id.tv_cbs02BldgNm);
			holder.tv_cbs02CarNo = (TextView)convertView.findViewById(R.id.tv_cbs02CarNo);
			holder.tv_cbs02Repeat = (TextView)convertView.findViewById(R.id.tv_cbs02Repeat);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IR_CD02_R00_ITEM01 mData = NotiListItems.get(position);
		holder.tv_cbs02BldgNm.setText(mData.getBLDG_NM());
		holder.tv_cbs02CarNo.setText(mData.getCAR_NO() + mData.getDONG_CAR_NO());
		holder.tv_cbs02Repeat.setText("고장횟수  : "+ mData.getRST_CNT());

		return convertView;
	}



}
