package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class SelectPendCodeAdapter extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_deptName;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<SelectPendCodeData> pendCodeData;;
	private ArrayList<SelectPendCodeData> orgPendCodeData;;

	public SelectPendCodeAdapter(Context context,
			ArrayList<SelectPendCodeData> resData) {
		super();
		this.mContext = context;
		
		setPendCodeListItems(resData);
	}
	

	public void setPendCodeListItems(ArrayList<SelectPendCodeData> value) {
		this.orgPendCodeData = value;
		this.pendCodeData = new ArrayList<SelectPendCodeData>();

		for (SelectPendCodeData resData : this.orgPendCodeData) {
			this.pendCodeData.add(resData);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pendCodeData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pendCodeData.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_search_dept, null);
			holder.tv_deptName = (TextView)convertView.findViewById(R.id.tv_deptName);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SelectPendCodeData mData = pendCodeData.get(position);
		holder.tv_deptName.setText(mData.getYET_REASON_NM());
	return convertView;
	}

	

}
