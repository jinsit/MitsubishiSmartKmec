package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;



public class SearchBldgAdapter extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_noticeSubject, tv_noticeTime;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<SearchBldgResData> bldgResData;;
	private ArrayList<SearchBldgResData> orgBldgResData;;

	public SearchBldgAdapter(Context context,
			ArrayList<SearchBldgResData> resData) {
		super();
		this.mContext = context;
		
		setReceiptListItems(resData);
	}
	

	public void setReceiptListItems(ArrayList<SearchBldgResData> value) {
		this.orgBldgResData = value;
		this.bldgResData = new ArrayList<SearchBldgResData>();

		for (SearchBldgResData resData : this.orgBldgResData) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.bldgResData.add(resData);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bldgResData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bldgResData.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_notice, null);
			holder.tv_noticeSubject = (TextView)convertView.findViewById(R.id.tv_noticeSubject);
			holder.tv_noticeTime = (TextView)convertView.findViewById(R.id.tv_noticeTime);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SearchBldgResData mData = bldgResData.get(position);
		holder.tv_noticeSubject.setText(mData.getBLDG_NM());
		holder.tv_noticeTime.setText(mData.getADDR());

		return convertView;
	}

	

}
