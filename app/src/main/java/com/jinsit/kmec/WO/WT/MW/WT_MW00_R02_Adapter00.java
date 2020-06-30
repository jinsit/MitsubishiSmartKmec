package com.jinsit.kmec.WO.WT.MW;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class WT_MW00_R02_Adapter00 extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_mw_r02RowNum,tv_mw_r02WorkDt,tv_mw_r02InspStNm,tv_mw_r02JobStNm,tv_mw_r02DetailRmk;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<WT_MW00_R02_ITEM00> wT_MW00_R02_ITEM00;;
	public WT_MW00_R02_Adapter00(Context context,
			ArrayList<WT_MW00_R02_ITEM00> items) {
		super();
		this.mContext = context;
		this.wT_MW00_R02_ITEM00 = items;
		//setListItem(items);
	}

	public void setListItem(ArrayList<WT_MW00_R02_ITEM00> value) {
	
		for (WT_MW00_R02_ITEM00 item : this.wT_MW00_R02_ITEM00) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.wT_MW00_R02_ITEM00.add(item);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wT_MW00_R02_ITEM00.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return wT_MW00_R02_ITEM00.get(position);
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
			convertView = inflater.inflate(R.layout.wt_mw00_r02_adapter00, null);
			holder.tv_mw_r02RowNum= (TextView)convertView.findViewById(R.id.tv_mw_r02RowNum);
			holder.tv_mw_r02WorkDt =(TextView)convertView.findViewById(R.id.tv_mw_r02WorkDt);
			holder.tv_mw_r02InspStNm = (TextView)convertView.findViewById(R.id.tv_mw_r02InspStNm);
			holder.tv_mw_r02JobStNm =(TextView)convertView.findViewById(R.id.tv_mw_r02JobStNm);
			holder.tv_mw_r02DetailRmk= (TextView)convertView.findViewById(R.id.tv_mw_r02DetailRmk);
		convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final WT_MW00_R02_ITEM00 mData = wT_MW00_R02_ITEM00.get(position);
		holder.tv_mw_r02RowNum.setText(mData.getROW_NUM()+"차검사일");
		holder.tv_mw_r02WorkDt.setText(mData.getWORK_DT());
		holder.tv_mw_r02InspStNm.setText(mData.getINSP_ST_NM());
		holder.tv_mw_r02JobStNm.setText(mData.getJOB_ST_NM());
		holder.tv_mw_r02DetailRmk.setText(mData.getDETAIL_RMK());
		return convertView;
	}

	

}
