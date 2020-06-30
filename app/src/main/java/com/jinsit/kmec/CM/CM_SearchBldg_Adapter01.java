package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class CM_SearchBldg_Adapter01 extends BaseExpandableListAdapter {

	private Context context;
	public ArrayList<CM_SearchBldgInfo_ITEM01> dataList;
	private boolean[] isChecked;
	
	public CM_SearchBldg_Adapter01(Context context,
			ArrayList<CM_SearchBldgInfo_ITEM01> dataList) {
		super();
		this.context = context;
		this.dataList = dataList;
		this.isChecked = new boolean[dataList.size()];
		for(CM_SearchBldgInfo_ITEM01 item : dataList){
			item.getChild().add(item);
		}
	}

	private class ViewHolder {
		TextView clientDept;
		TextView clientTel;
		TextView clientNm;
		TextView clientHp;
		TextView empNm1;
		TextView emp1Hp;
		TextView empNm2;
		TextView emp2Hp;
		TextView bldg_nm;
		TextView csDeptNm;
		TextView addr;
		CheckBox bldg;
		
		}



	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.dataList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.dataList.get(groupPosition).getChild().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.dataList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return this.dataList.get(groupPosition).getChild().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setChecked(int position) {
		isChecked[position] = !isChecked[position];
		if (isChecked[position]) {
			int tempSize = isChecked.length;
			for (int i = 0; i < tempSize; i++) {
				if (i != position) {
					 isChecked[i] = false;
				}
			}
		}
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
	
	public CM_SearchBldgInfo_ITEM01 getSelectedData() {
		ArrayList<CM_SearchBldgInfo_ITEM01> mArrayList = new ArrayList<CM_SearchBldgInfo_ITEM01>();
		for (int i = 0; i < isChecked.length; i++) {
			if (isChecked[i]) {
				return dataList.get(i);
			}
		}
		return null;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		CM_SearchBldgInfo_ITEM01 item = dataList.get(groupPosition);
		ViewHolder holder = null;
	
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.cm_searchbldg_adapter01, null);
			
				holder.csDeptNm = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_csDeptNm);
				holder.bldg_nm = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_bldg_nm);
				holder.addr = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_addr);
				holder.bldg = (CheckBox) convertView
						.findViewById(R.id.cb_cm_adapter_bldg);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.csDeptNm.setText(item.getCsDetpNm());
		holder.bldg_nm.setText(item.getBldgNm());
		holder.addr.setText(item.getAddr());
		holder.bldg.setClickable(false);
		holder.bldg.setFocusable(false);
		holder.bldg.setChecked(isChecked[groupPosition]);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		CM_SearchBldgInfo_ITEM01 item = dataList.get(groupPosition).getChild().get(childPosition);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.cm_searchbldg_adapter02, null);
		
		holder.clientDept = (TextView)convertView.findViewById(R.id.tv_cm_adapter_clientDept);
		holder.clientTel = (TextView)convertView.findViewById(R.id.tv_cm_adapter_clientTel);
		holder.clientNm = (TextView)convertView.findViewById(R.id.tv_cm_adapter_clientNm);
		holder.clientHp = (TextView)convertView.findViewById(R.id.tv_cm_adapter_clientHp);
		holder.empNm1 = (TextView)convertView.findViewById(R.id.tv_cm_adapter_empNm1);
		holder.emp1Hp = (TextView)convertView.findViewById(R.id.tv_cm_adapter_emp1Hp);
		holder.empNm2 = (TextView)convertView.findViewById(R.id.tv_cm_adapter_empNm2);
		holder.emp2Hp = (TextView)convertView.findViewById(R.id.tv_cm_adapter_emp2Hp);
		convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.clientDept.setText(item.getClientDept());
		holder.clientTel.setText(item.getClientTel());
		holder.clientNm.setText(item.getClientNm());
		holder.clientHp.setText(item.getClientHp());
		holder.empNm1.setText(item.getEmpNm1());
		holder.emp1Hp.setText(item.getEmp1hP());
		holder.empNm2.setText(item.getEmpNm2());
		holder.emp2Hp.setText(item.getEmp2hP());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}





