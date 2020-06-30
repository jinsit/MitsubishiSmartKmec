package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class CM_SearchWorkCategory_Adapter01 extends BaseAdapter {

	private Context context;
	private int selTp;
	private boolean[] isChecked;
	public ArrayList<CM_SearchWorkCategory_ITEM01> dataList;;
	private int layout;

	public CM_SearchWorkCategory_Adapter01(Context context, int layout,
			ArrayList<CM_SearchWorkCategory_ITEM01> dataList) {
		super();
		this.context = context;
		this.dataList = dataList;
		this.layout = layout;
	}

	private class ViewHolder {
		TextView workNm;
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
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CM_SearchWorkCategory_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.workNm = (TextView) convertView
					.findViewById(R.id.tv_wo_adapter_workNm);
		
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.workNm.setText(item.getWorkNm());
		return convertView;
	}
}
