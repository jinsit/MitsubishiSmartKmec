package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class SearchElevAdapter extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_elevCAR_NO, tv_elevModel;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<SearchElevResData> elevResData;;
	private ArrayList<SearchElevResData> orgElevResData;;

	public SearchElevAdapter(Context context,
							 ArrayList<SearchElevResData> resData) {
		super();
		this.mContext = context;

		setReceiptListItems(resData);
	}


	public void setReceiptListItems(ArrayList<SearchElevResData> value) {
		this.orgElevResData = value;
		this.elevResData = new ArrayList<SearchElevResData>();

		for (SearchElevResData resData : this.orgElevResData) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.elevResData.add(resData);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return elevResData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return elevResData.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_search_elev, null);
			holder.tv_elevCAR_NO = (TextView)convertView.findViewById(R.id.tv_elevCAR_NO);
			holder.tv_elevModel = (TextView)convertView.findViewById(R.id.tv_elevModel);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SearchElevResData mData = elevResData.get(position);
		holder.tv_elevCAR_NO.setText(mData.getCAR_NO() +"호기");
		holder.tv_elevModel.setText(mData.getMODEL_NM());

		return convertView;
	}



}
