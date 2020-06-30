package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class RoutineCheckExpandListAdapterMng extends BaseExpandableListAdapter {
	Context mContext;
	
	private ArrayList<PartCheckTableItemMng> checkTableItem,checkTableItem2;
	public TextView tv_wt_checkNm,tv_wt_rmk;
	public TextView et_wt_numeric;
	public ImageView iv_wt_status;


	public RoutineCheckExpandListAdapterMng(Context context,
			ArrayList<PartCheckTableItemMng> mappingItem, ArrayList<PartCheckTableItemMng> mappingItem2) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.checkTableItem = mappingItem2;
		//this.checkTableItem2= mappingItem;
		//this.childeGrouping(checkTableItem);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		//return partCheckListData.get(childPosition);
		
		return checkTableItem.get(groupPosition).child.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	int getABImage(int defVal){
		int resId = 0;
		if(defVal==0){
			//0이면 A
			resId = R.drawable.a_on;
		}else{
			//0보다 크면 B
			resId = R.drawable.b_on;
		}
		return resId;
	}
	
	// ChildView에 데이터 뿌리기
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = getChildGenericView();
		} else {
			view = convertView;
		}

		tv_wt_checkNm = (TextView) view.findViewById(R.id.tv_wt_checkNm);
		et_wt_numeric = (TextView) view.findViewById(R.id.et_wt_numeric);
		iv_wt_status = (ImageView)view.findViewById(R.id.iv_wt_status);
		tv_wt_rmk = (TextView)view.findViewById(R.id.tv_wt_rmk);
		
		PartCheckListDataMng mData= checkTableItem.get(groupPosition).child.get(childPosition);
	
		tv_wt_checkNm.setText(mData.getMNG_DESC() + mData.getCAR_NO());
		 iv_wt_status.setImageResource(getABImage(mData.getDefalutValue()));
		 if(mData.getINPUT_RMK().equals("")){
			 tv_wt_rmk.setVisibility(View.GONE);
		 }else{
			 tv_wt_rmk.setVisibility(View.VISIBLE);
			 tv_wt_rmk.setText("비고 : " + mData.getINPUT_RMK());	 
		 }
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return checkTableItem.get(groupPosition).child.size();
	}
	

	@Override
	public Object getGroup(int groupPosition) {
		return checkTableItem.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return checkTableItem.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// GroupView에 데이터 뿌리
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View view;
		if (convertView == null) {
			view = getParentGenericView();
		} else {
			view = convertView;
		}

		TextView text = (TextView) view.findViewById(R.id.tv_wo_routineGroup);
		text.setText(checkTableItem.get(groupPosition).group);
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return super.areAllItemsEnabled();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	// Child의 View의 XML을 생성
	public View getChildGenericView() {
		//holder = new ViewHolder();

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listitem_wt_checktable, null);
		
		//view.setTag(holder);
		return view;
	}
	
	// Parent(Group)의 View의 XML을 생성
	public View getParentGenericView() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.expandlistitem_routinechecktable,
				null);
		return view;
	}
}
