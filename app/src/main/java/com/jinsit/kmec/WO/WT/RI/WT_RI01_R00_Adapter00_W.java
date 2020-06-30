package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class WT_RI01_R00_Adapter00_W extends BaseAdapter {

	private class ViewHolder {
		
		public TextView tv_wo_routineBldgNm, tv_wo_routineICnt,tv_wo_routineEText;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<RoutineCheckListData> routineCheckListItems;;
	private ArrayList<RoutineCheckListData> OrgroutineCheckListItems;;

	public WT_RI01_R00_Adapter00_W(Context context,
			ArrayList<RoutineCheckListData> routineListItem) {
		super();
		this.mContext = context;
		
		setRoutineListItems(routineListItem);
	}

	public void setRoutineListItems(ArrayList<RoutineCheckListData> value) {
		this.OrgroutineCheckListItems = value;
		this.routineCheckListItems = new ArrayList<RoutineCheckListData>();

		for (RoutineCheckListData routineListItem : this.OrgroutineCheckListItems) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.routineCheckListItems.add(routineListItem);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return routineCheckListItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return routineCheckListItems.get(position);
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
			convertView = inflater.inflate(R.layout.listitem_routine_checklist, null);
			holder.tv_wo_routineBldgNm = (TextView)convertView.findViewById(R.id.tv_wo_routineBldgNm);
			holder.tv_wo_routineICnt = (TextView)convertView.findViewById(R.id.tv_wo_routineICnt);
			holder.tv_wo_routineEText = (TextView)convertView.findViewById(R.id.tv_wo_routineEText);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RoutineCheckListData mData = routineCheckListItems.get(position);
		holder.tv_wo_routineBldgNm.setText(mData.getBLDG_NM());
		holder.tv_wo_routineICnt.setText(mData.getI_CNT() + "/" + mData.getT_CNT());
		holder.tv_wo_routineEText.setText(mData.getE_TEXT());
		
		return convertView;
	}

	

}
