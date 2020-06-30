package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class SearchDeptAdapter extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_deptName;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<SearchDeptResData> deptResData;;
	private ArrayList<SearchDeptResData> orgDeptResData;;

	public SearchDeptAdapter(Context context,
			ArrayList<SearchDeptResData> resData) {
		super();
		this.mContext = context;
		
		setReceiptListItems(resData);
	}
	

	public void setReceiptListItems(ArrayList<SearchDeptResData> value) {
		this.orgDeptResData = value;
		this.deptResData = new ArrayList<SearchDeptResData>();

		for (SearchDeptResData resData : this.orgDeptResData) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.deptResData.add(resData);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deptResData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return deptResData.get(position);
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
		final SearchDeptResData mData = deptResData.get(position);
		holder.tv_deptName.setText(mData.getDEPT_NM());
	return convertView;
	}

	

}
