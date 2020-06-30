
package com.jinsit.kmec.CM;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class CM_SearchEmp_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<CM_SearchEmp_ITEM01> dataList = null;

	public CM_SearchEmp_Adapter01(){};
	public CM_SearchEmp_Adapter01(Context context, int layout, List<CM_SearchEmp_ITEM01> dataList) {
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder{
		
		private TextView deptNm;
		private TextView csEmpId;
		private TextView empNm;
		private TextView phone;
		private TextView isrtDt;
		private TextView lastJobTm;
		private TextView loginDay;
		private TextView deviceSt;
		private TextView workNm;
		private TextView engSt;
		private TextView nowBldg;
		private TextView addr;
		
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final CM_SearchEmp_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.deptNm  = (TextView) cView.findViewById(R.id.tv01_cm_deptNm);
			holder.empNm   = (TextView) cView.findViewById(R.id.tv02_cm_empNm);
			holder.workNm  = (TextView) cView.findViewById(R.id.tv03_cm_workNm);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.deptNm.setText( item.getDeptNm() );
		holder.empNm.setText( item.getEmpNm() );
		holder.workNm.setText( item.getWorkNm() );
		return cView;
	}

}
