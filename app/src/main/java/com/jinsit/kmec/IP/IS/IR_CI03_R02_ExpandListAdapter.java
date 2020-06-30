package com.jinsit.kmec.IP.IS;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.PartCheckListData;
import com.jinsit.kmec.WO.WT.RI.PartCheckTableItem;

import java.util.ArrayList;

public class IR_CI03_R02_ExpandListAdapter extends BaseExpandableListAdapter {
	Context mContext;

	private ArrayList<PartCheckTableItem> checkTableItem;
	public TextView tv_wt_checkNm;
	public TextView tv_inputTypeName;


	public IR_CI03_R02_ExpandListAdapter(Context context,
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
		tv_inputTypeName = (TextView) view.findViewById(R.id.tv_inputTypeName);
		
		
		PartCheckListData mData= checkTableItem.get(groupPosition).child.get(childPosition);
		 tv_wt_checkNm.setText(mData.getSMART_DESC());
		// getInputImage(mData);
		 if("1".equals(mData.getINPUT_TP())){
			 tv_inputTypeName.setText("상태");
		 }else if("3".equals(mData.getINPUT_TP())){
			 tv_inputTypeName.setText("수치");
		 }else if("7".equals(mData.getINPUT_TP())){
			 tv_inputTypeName.setText("유무");
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
		View view = inflater.inflate(R.layout.ir_ci03_r02_expand_listitem, null);
		
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
