package com.jinsit.kmec.DM.DM;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class DM_DM03_R01_Adapter02 extends BaseAdapter {

	Context context;
	int layout;
	private List<DM_DM03_R01_ITEM02> dataList = null;
	
	public DM_DM03_R01_Adapter02() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DM_DM03_R01_Adapter02(Context context, int layout,
			List<DM_DM03_R01_ITEM02> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
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
		private TextView tv_dm_adapter03_empNm;
		private TextView tv_dm_adapter03_totalOtTm;
		private TextView tv_dm_adapter03_bldgNm;
		private TextView tv_dm_adapter03_repStNm;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DM_DM03_R01_ITEM02 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.tv_dm_adapter03_empNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter03_empNm);
			holder.tv_dm_adapter03_totalOtTm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter03_totalOtTm);
			holder.tv_dm_adapter03_bldgNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter03_bldgNm);
			
			holder.tv_dm_adapter03_repStNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter03_repStNm);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_dm_adapter03_empNm.setText(item.getEMP_NM());
		holder.tv_dm_adapter03_totalOtTm.setText(item.getOT_TM() + " / " + item.getTOTAL_OT_TM());
		holder.tv_dm_adapter03_bldgNm.setText(item.getBLDG_NM());
		holder.tv_dm_adapter03_repStNm.setText(item.getREP_ST_NM());
		
		return convertView;
	}

}