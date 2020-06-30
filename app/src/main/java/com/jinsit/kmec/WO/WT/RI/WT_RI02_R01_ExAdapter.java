package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class WT_RI02_R01_ExAdapter extends BaseExpandableListAdapter {
	Context mContext;
	
	private ArrayList<WT_RI02_R00_ITEM_Table> checkTableItem;
	public TextView tv_wt_checkNm;
	public TextView et_wt_numeric;
	public ImageView iv_wt_status;
	public CheckBox cb_wt_holdOver;


	public WT_RI02_R01_ExAdapter(Context context,
			ArrayList<WT_RI02_R00_ITEM_Table> mappingItem) {
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

	
	private int getInputImage(WT_RI02_R00_ITEM00 mData){
		int resId = 0 ;
		 String inputTp, type;
		 inputTp = mData.getINPUT_TP();
		switch(Integer.valueOf(inputTp)){
		case 1:
			type = mData.getINPUT_TP1();
			if(type.equals("A")){
				resId = R.drawable.a_on;
			}else if(type.equals("B")){
				resId = R.drawable.b_on;
			}else if(type.equals("C")){
				resId = R.drawable.c_on;
			}else if(type.equals("E")){
				resId = R.drawable.none_on_abc;
			}
			break;
		case 3:
			type = mData.getINPUT_TP3();
			break;
		case 7:
			type = mData.getINPUT_TP7();
			if(type.equals("1")){
				resId = R.drawable.ok_on;
			}else if(type.equals("0")){
				resId = R.drawable.no_on;
			}else if(type.equals("E")){
				resId = R.drawable.none_on_ox;
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
		cb_wt_holdOver = (CheckBox)view.findViewById(R.id.cb_wt_holdOver);
		
		WT_RI02_R00_ITEM00 mData= checkTableItem.get(groupPosition).child.get(childPosition);
		 tv_wt_checkNm.setText(mData.getSMART_DESC() + " car " + mData.getCAR_NO());
		 
		// 이월여부 있는지 판단

			// 체크박스의 상태 변화를 체크한다.
		 String monthChkIf = mData.getMONTH_CHK_IF();
		 String overMonth = mData.getOVER_MONTH();
			if (monthChkIf.equals("1")) {
				cb_wt_holdOver.setVisibility(View.VISIBLE);
			} else {
				cb_wt_holdOver.setVisibility(View.INVISIBLE);
			}
			
		 if(mData.getINPUT_TP().equals("3")){
			 et_wt_numeric.setVisibility(View.VISIBLE); 
			 et_wt_numeric.setText(mData.getINPUT_TP3());
			 Log.e("etText", "getIntputTp3 = " +mData.getINPUT_TP3() );
		 }else{
			 et_wt_numeric.setVisibility(View.INVISIBLE);
		 }

			if(overMonth.equals("Y")){
				cb_wt_holdOver.setChecked(true);
				et_wt_numeric.setVisibility(View.INVISIBLE); 
			}else{
				cb_wt_holdOver.setChecked(false);
			}
			cb_wt_holdOver.setClickable(false);
		// getInputImage(mData);
		 
		 iv_wt_status.setImageResource( getInputImage(mData));
		
//		switch(groupPosition){
//		case 0:
//			 PartCheckListData mData= checkTableItem.get(groupPosition).child.get(childPosition);
//			 tv_wt_checkNm.setText(mData.getSMART_DESC());
//			// getInputImage(mData);
//			 if(mData.getINPUT_TP().equals("3")){
//				 et_wt_numeric.setVisibility(View.VISIBLE); et_wt_numeric.setText(mData.getINPUT_TP3());
//			 }else{
//				 et_wt_numeric.setVisibility(View.INVISIBLE);
//			 }
//			 iv_wt_status.setImageResource( getInputImage(mData));
//			break;
//			
//		case 1:
//			 PartCheckListData mData2=  checkTableItem.get(groupPosition).child.get(childPosition);
//			 tv_wt_checkNm.setText(mData2.getSMART_DESC());
//			 if(mData2.getINPUT_TP().equals("3")){
//				 et_wt_numeric.setVisibility(View.VISIBLE); et_wt_numeric.setText(mData2.getINPUT_TP3());
//			 }	else{
//				 et_wt_numeric.setVisibility(View.INVISIBLE);
//			 }
//			 iv_wt_status.setImageResource(getInputImage(mData2));
//			break;
//		case 2:
//			 PartCheckListData mData3=  checkTableItem.get(groupPosition).child.get(childPosition);
//			 tv_wt_checkNm.setText(mData3.getSMART_DESC());
//			 if(mData3.getINPUT_TP().equals("3")){
//				 et_wt_numeric.setVisibility(View.VISIBLE); et_wt_numeric.setText(mData3.getINPUT_TP3());
//			 }else{
//				 et_wt_numeric.setVisibility(View.INVISIBLE);
//			 }
//			 iv_wt_status.setImageResource(getInputImage(mData3));
//			break;
//		case 3:
//			 PartCheckListData mData4=  checkTableItem.get(groupPosition).child.get(childPosition);
//			 tv_wt_checkNm.setText(mData4.getSMART_DESC());
//			 if(mData4.getINPUT_TP().equals("3")){
//				 et_wt_numeric.setVisibility(View.VISIBLE); et_wt_numeric.setText(mData4.getINPUT_TP3());
//			 }else{
//				 et_wt_numeric.setVisibility(View.INVISIBLE);
//			 }
//			 iv_wt_status.setImageResource(getInputImage(mData4));
//			break;
//		}
//		final PartCheckListData mData = partCheckListData.get(childPosition);
//		PartCheckListWidgetData mWidgetData = widgetData.get(childPosition);
		
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
