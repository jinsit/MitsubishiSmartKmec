package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

/**
 * WO_WT00_R01_Adapter00  BaseAdapter
 * 
 * @discription 각파트별 작업 진행현황 어댑터
 * @author 원성민
* @deprecated 이 소스는 정기정검 웹 기반이기 때문에 사용하지 않습니다.
 */
public class WO_WT00_R01_Adapter00_W extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_wt_nfcPlcNm, tv_wt_jobStNm;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<WO_WT00_R01_ITEM01> workStatusData;
	private ArrayList<WO_WT00_R01_ITEM01> orgWorkStatusData;

	public WO_WT00_R01_Adapter00_W(Context context, ArrayList<WO_WT00_R01_ITEM01> value) {
		super();
		this.mContext = context;

		setWorkStatusListItems(value);
	}

	public void setWorkStatusListItems(ArrayList<WO_WT00_R01_ITEM01> value) {
		this.orgWorkStatusData = value;
		this.workStatusData = new ArrayList<WO_WT00_R01_ITEM01>();

		for (WO_WT00_R01_ITEM01 workStatusData : this.orgWorkStatusData) {
			this.workStatusData.add(workStatusData);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return workStatusData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return workStatusData.get(position);
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
			convertView = inflater
					.inflate(R.layout.listitem_wt_jobstatus, null);
			holder.tv_wt_nfcPlcNm = (TextView) convertView
					.findViewById(R.id.tv_wt_nfcPlcNm);
			holder.tv_wt_jobStNm = (TextView) convertView
					.findViewById(R.id.tv_wt_jobStNm);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final WO_WT00_R01_ITEM01 mData = workStatusData.get(position);
		holder.tv_wt_nfcPlcNm.setText(mData.getNFC_PLC_NM());
		holder.tv_wt_jobStNm.setText(mData.getJOB_ST_NM());
		

		return convertView;
	}

}
