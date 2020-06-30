package com.jinsit.kmec.HM.MP;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WO_WT00_R00_ITEM00;

public class HomeWorkListAdapter extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_home_bldgNm, tv_home_workNm,tv_home_carNo;
		public ImageView iv_home_jobDivision;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<WO_WT00_R00_ITEM00> workListData;
	private ArrayList<WO_WT00_R00_ITEM00> orgWorkListData;

	public HomeWorkListAdapter(Context context,
							   ArrayList<WO_WT00_R00_ITEM00> value) {
		super();
		this.mContext = context;

		setReceiptListItems(value);
	}

	public void setReceiptListItems(ArrayList<WO_WT00_R00_ITEM00> value) {
		this.orgWorkListData = value;
		this.workListData = new ArrayList<WO_WT00_R00_ITEM00>();

		for (WO_WT00_R00_ITEM00 workListData : this.orgWorkListData) {
			this.workListData.add(workListData);

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
			convertView = inflater.inflate(R.layout.listitem_home_joblist, null);
			holder.iv_home_jobDivision = (ImageView)convertView.findViewById(R.id.iv_home_jobDivision);
			holder.tv_home_bldgNm = (TextView)convertView.findViewById(R.id.tv_home_bldgNm);
			//holder.tv_home_carNo = (TextView)convertView.findViewById(R.id.tv_home_carNo);
			holder.tv_home_workNm = (TextView)convertView.findViewById(R.id.tv_home_workNm);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WO_WT00_R00_ITEM00 mData = workListData.get(position);
		if(mData.getBLDG_NM().equals("")){
			holder.tv_home_bldgNm.setText("빌딩정보가 없습니다.");
		}else{
			holder.tv_home_bldgNm.setText(mData.getBLDG_NM() + " " + mData.getCAR_NO());
		}

		//holder.tv_home_carNo.setText(mData.getCAR_NO());
		holder.tv_home_workNm.setText(mData.getWORK_NM());
		if(mData.getWORK_NM().equals("정기점검")){
			holder.iv_home_jobDivision.setImageResource(R.drawable.division_check);
		}else if(mData.getWORK_NM().equals("고장수리")){
			holder.iv_home_jobDivision.setImageResource(R.drawable.division_breakdown);
		}else{
			holder.iv_home_jobDivision.setImageResource(R.drawable.division_normal);
		}
		return convertView;
	}



}
