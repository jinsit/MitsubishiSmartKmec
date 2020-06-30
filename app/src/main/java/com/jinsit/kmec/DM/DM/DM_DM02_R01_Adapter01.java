package com.jinsit.kmec.DM.DM;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class DM_DM02_R01_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<DM_DM02_R01_ITEM01> dataList = null;
	private String attendDate;
	public DM_DM02_R01_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DM_DM02_R01_Adapter01(Context context, int layout,
			List<DM_DM02_R01_ITEM01> dataList,String date) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.attendDate = date;
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
	private class ViewHolder {
		private TextView tv_dm_adapter02_attendDt;
		private TextView tv_dm_adapter02_attendCdNm;  
		private TextView tv_dm_adapter02_repStNm;   
	
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DM_DM02_R01_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.tv_dm_adapter02_attendDt = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter02_attendDt);
			holder.tv_dm_adapter02_attendCdNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter02_attendCdNm);
			holder.tv_dm_adapter02_repStNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter02_repStNm);
		
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_dm_adapter02_attendDt.setText(attendDate);
		holder.tv_dm_adapter02_attendCdNm.setText(item.getATTEND_CD_NM());
		holder.tv_dm_adapter02_repStNm.setText(item.getREP_ST_NM());
		
		return convertView;
	}

}