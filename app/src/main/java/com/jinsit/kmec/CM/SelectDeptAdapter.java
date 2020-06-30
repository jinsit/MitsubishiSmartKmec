package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class SelectDeptAdapter extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_noticeSubject, tv_noticeTime;
		CheckBox cb_checkbox;
	}

	private ViewHolder holder;
	private Context mContext;
	private boolean[] isChecked;
	public ArrayList<SearchDeptResData> NotiListItems;;
	private ArrayList<SearchDeptResData> OrgNotiListItems;;

	public SelectDeptAdapter(Context context,
			ArrayList<SearchDeptResData> receiptListItems) {
		super();
		this.mContext = context;
		this.isChecked = new boolean[receiptListItems.size()];
		setReceiptListItems(receiptListItems);
	}

	public void setReceiptListItems(ArrayList<SearchDeptResData> value) {
		this.OrgNotiListItems = value;
		this.NotiListItems = new ArrayList<SearchDeptResData>();

		for (SearchDeptResData receiptListItem : this.OrgNotiListItems) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.NotiListItems.add(receiptListItem);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return NotiListItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return NotiListItems.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_select_dept, null);
			holder.tv_noticeSubject = (TextView) convertView
					.findViewById(R.id.tv_deptSubject);
			holder.cb_checkbox = (CheckBox) convertView
					.findViewById(R.id.cb_deptCheck);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SearchDeptResData mData = NotiListItems.get(position);
		holder.tv_noticeSubject.setText(mData.getDEPT_NM());
		
		holder.cb_checkbox.setClickable(false);
		holder.cb_checkbox.setFocusable(false);
		holder.cb_checkbox.setChecked(isChecked[position]);
		return convertView;
	}

	public void setChecked(int position) {
		isChecked[position] = !isChecked[position];
	}

	public ArrayList<Integer> getChecked() {
		int tempSize = isChecked.length;
		ArrayList<Integer> mArrayList = new ArrayList<Integer>();
		for (int i = 0; i < tempSize; i++) {
			if (isChecked[i]) {
				mArrayList.add(i);
			}
		}
		return mArrayList;
	}
	
	public ArrayList<SearchDeptResData> getSelectedData() {
		int tempSize = isChecked.length;
		ArrayList<SearchDeptResData> mArrayList = new ArrayList<SearchDeptResData>();
		for (int i = 0; i < tempSize; i++) {
			if (isChecked[i]) {
				mArrayList.add(NotiListItems.get(i));
			}
		}
		return mArrayList;
	}

}
