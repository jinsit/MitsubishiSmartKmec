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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class RoutineCheckExpandListAdapter extends BaseExpandableListAdapter {
	Context mContext;
	
	private ArrayList<PartCheckTableItem> checkTableItem;
	public TextView tv_wt_checkNm,tv_wt_rmk;
	public TextView et_wt_numeric;
	public ImageView iv_wt_status;
	public LinearLayout ll_wt_checkList;

	public RoutineCheckExpandListAdapter(Context context,
			ArrayList<PartCheckTableItem> mappingItem) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.checkTableItem = mappingItem;
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

	
	private int getInputImage(PartCheckListData mData){
		int resId = 0 ;
		 String inputTp, type;
		 inputTp = mData.getINPUT_TP();
		 if(inputTp == null || inputTp.equals("")){
		 	return  0;
		 }
		switch(Integer.valueOf(inputTp)){
		case 1:
			type = mData.getINPUT_TP1() == null ? "" : mData.getINPUT_TP1();
			if(type.equals("A")){
				resId = R.drawable.a_on;
			}else if(type.equals("B")){
				resId = R.drawable.b_on;
			}else if(type.equals("C")){
				resId = R.drawable.c_on;
			}
			break;
		case 3:
			type = mData.getINPUT_TP3();
			break;
		case 7:
			type = mData.getINPUT_TP7() == null ? "" : mData.getINPUT_TP7();
			if(type.equals("1")){
				resId = R.drawable.ok_on;
			}else if(type.equals("0")){
				resId = R.drawable.no_on;
			}
			break;
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
		ll_wt_checkList = (LinearLayout)view.findViewById(R.id.ll_wt_checkList);
		
		
		PartCheckListData mData= checkTableItem.get(groupPosition).child.get(childPosition);
		 tv_wt_checkNm.setText(mData.getSMART_DESC());
		String headerIf = mData.getHEADER_IF();
		if (headerIf.equals("1")) {
			ll_wt_checkList.setBackgroundResource(R.color.comm_edit_bg);
			tv_wt_checkNm.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}
		else {
			ll_wt_checkList.setBackgroundColor(0);
			tv_wt_checkNm.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));
		}

		 if(mData.getINPUT_TP().equals("3")){
			 et_wt_numeric.setVisibility(View.VISIBLE); 
			 et_wt_numeric.setText(mData.getINPUT_TP3());
			 Log.e("etText", "getIntputTp3 = " +mData.getINPUT_TP3() );
		 }else{
			 et_wt_numeric.setVisibility(View.INVISIBLE);
		 }
		 iv_wt_status.setImageResource( getInputImage(mData));
		 if(mData.getINPUT_RMK().equals("")){
			 tv_wt_rmk.setVisibility(View.GONE);
		 }else{
			 tv_wt_rmk.setVisibility(View.VISIBLE);
			 tv_wt_rmk.setText("비고 : " +mData.getINPUT_RMK());	 
		 }

		
			return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
//		int retData = 0;
//		switch(groupPosition){
//		case 0:
//			retData = part1Data.size();
//			break;
//		case 1:
//			retData = part2Data.size();
//			break;
//		case 2:
//			retData = part3Data.size();
//			break;
//		case 3:
//			retData = part4Data.size();
//			break;
//		}
//		return retData;
		
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
