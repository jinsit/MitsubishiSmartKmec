package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class JinSimpleArrayAdapter extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_listSubject;
	}

	private ViewHolder holder;
	private Context mContext;
	private int layout;
	public ArrayList<String> jinList;;
	public JinSimpleArrayAdapter(Context context,int layout,
			ArrayList<String> items) {
		super();
		this.mContext = context;
		this.layout = layout;
		this.jinList = items;
		//setListItem(items);
	}

	public void setListItem(ArrayList<String> value) {
	
		for (String item : this.jinList) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.jinList.add(item);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jinList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jinList.get(position);
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
			convertView = inflater.inflate(layout, null);
			holder.tv_listSubject= (TextView)convertView.findViewById(R.id.tv_listSubject);
		
		convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String mData = jinList.get(position);
		holder.tv_listSubject.setText(mData);
		
		return convertView;
	}

	

}
