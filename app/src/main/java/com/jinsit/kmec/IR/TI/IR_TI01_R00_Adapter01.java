package com.jinsit.kmec.IR.TI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class IR_TI01_R00_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<IR_TI01_R00_ITEM01> dataList = null;
	private List<IR_TI01_R00_ITEM01> allList = null;
	public IR_TI01_R00_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_TI01_R00_Adapter01(Context context, int layout,
			List<IR_TI01_R00_ITEM01> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = new ArrayList<IR_TI01_R00_ITEM01>();
		this.allList = dataList;
		filter(""); 
	}

	private class ViewHolder {
		private TextView docuMNm;

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


	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		dataList.clear();
		if (charText.length() == 0) {
			dataList.addAll(allList);
		} 
		else 
		{
			for (IR_TI01_R00_ITEM01 item : allList) 
			{
				if (item.getDocuMNm().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					dataList.add(item);
				}
			}
		}
		notifyDataSetChanged();
	}
 

	 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final IR_TI01_R00_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.docuMNm = (TextView) convertView
					.findViewById(R.id.tv_ti_adapter_docuMNm);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.docuMNm.setText(item.getDocuMNm());
		return convertView;
	}

}