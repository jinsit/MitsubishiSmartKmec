package com.jinsit.kmec.SM.GM;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SearchDeptResData;

public class SM_GM00_R00Adapter extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_noticeSubject;
		public ImageView ibtn_deptDelete;
	}

	private ViewHolder holder;
	private Context mContext;

	public ArrayList<SearchDeptResData> searchDeptResData;;
	private ArrayList<SearchDeptResData> OrgsearchDeptResData;;

	public SM_GM00_R00Adapter(Context context,
			ArrayList<SearchDeptResData> receiptListItems) {
		super();
		this.mContext = context;
		setReceiptListItems(receiptListItems);
	}

	public void setReceiptListItems(ArrayList<SearchDeptResData> value) {
		this.OrgsearchDeptResData = value;
		this.searchDeptResData = new ArrayList<SearchDeptResData>();

		for (SearchDeptResData receiptListItem : this.OrgsearchDeptResData) {

			this.searchDeptResData.add(receiptListItem);

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchDeptResData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchDeptResData.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_groupmsg_dept,
					null);
			holder.tv_noticeSubject = (TextView) convertView
					.findViewById(R.id.tv_deptSubject);
			holder.ibtn_deptDelete = (ImageView) convertView
					.findViewById(R.id.ibtn_deptDelete);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SearchDeptResData mData = searchDeptResData.get(position);
		holder.tv_noticeSubject.setText(mData.getDEPT_NM());
		holder.ibtn_deptDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchDeptResData.remove(position);
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}


}
