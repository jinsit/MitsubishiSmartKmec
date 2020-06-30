package com.jinsit.kmec.WO;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
/**
 * WO_WT00_R01F_Adatper00
 * @discription 작업대상목록 baseAdapter 
 * @author 원성민
 *
 */
public class WO_WT00_R00_Adapter00 extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_wo_jobTime, tv_wo_bldgNm, tv_wo_workNm, tv_wo_st,
				tv_wo_checkSt, tv_wo_carNo;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<WO_WT00_R00_ITEM00> workListData;
	private ArrayList<WO_WT00_R00_ITEM00> orgWorkListData;

	public WO_WT00_R00_Adapter00(Context context,
			ArrayList<WO_WT00_R00_ITEM00> value) {
		super();
		this.mContext = context;

		setReceiptListItems(value);
	}

	public void setReceiptListItems(ArrayList<WO_WT00_R00_ITEM00> value) {
		this.orgWorkListData = value;
		this.workListData = new ArrayList<WO_WT00_R00_ITEM00>();

		for (WO_WT00_R00_ITEM00 workListData : this.orgWorkListData) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.workListData.add(workListData);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return workListData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return workListData.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_job, null);

			holder.tv_wo_jobTime = (TextView) convertView
					.findViewById(R.id.tv_wo_jobTime);
			holder.tv_wo_workNm = (TextView) convertView
					.findViewById(R.id.tv_wo_workNm);
			holder.tv_wo_carNo = (TextView) convertView
					.findViewById(R.id.tv_wo_carNo);
			holder.tv_wo_bldgNm = (TextView) convertView
					.findViewById(R.id.tv_wo_bldgNm);
			holder.tv_wo_st = (TextView) convertView
					.findViewById(R.id.tv_wo_st);
			holder.tv_wo_checkSt = (TextView) convertView
					.findViewById(R.id.tv_wo_checkSt);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final WO_WT00_R00_ITEM00 mData = workListData.get(position);
		holder.tv_wo_jobTime.setText(mData.getWORK_DT());
		holder.tv_wo_workNm.setText(mData.getWORK_NM());
		holder.tv_wo_carNo.setText(mData.getCAR_NO());
		holder.tv_wo_bldgNm.setText(mData.getBLDG_NM());
		holder.tv_wo_st.setText(mData.getST() + " " + mData.getCS_FR());
		if(mData.getWORK_NM().equals("정기점검")){
			holder.tv_wo_checkSt.setVisibility(View.VISIBLE);
		holder.tv_wo_checkSt.setText("점검상황 " + mData.getY_CNT() + "/"
				+ mData.getT_CNT());
		}else{
			holder.tv_wo_checkSt.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

}
