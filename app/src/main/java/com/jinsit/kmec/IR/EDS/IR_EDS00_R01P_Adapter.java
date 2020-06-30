package com.jinsit.kmec.IR.EDS;

import java.util.List;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.ES.IR_ES00_R01P_Item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IR_EDS00_R01P_Adapter extends BaseAdapter {

	private Context context;
	private int layout;
	private List<IR_ES00_R01P_Item> dataList = null;

	public IR_EDS00_R01P_Adapter(){};
	public IR_EDS00_R01P_Adapter(Context context, int layout, List<IR_ES00_R01P_Item> dataList) {
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
		
		TextView deptNm;
		TextView empNm;
		//TextView workNm;
		//TextView nowBldg;
	}
	
	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final IR_ES00_R01P_Item item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.deptNm = (TextView) cView.findViewById(R.id.tv01_deptNm);
			holder.empNm  = (TextView) cView.findViewById(R.id.tv02_empNm);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.deptNm.setText( item.getDeptNm() );
		holder.empNm.setText( item.getEmpNm() );
		//holder.workNm.setText( item.getWorkNm() );
		//holder.nowBldg.setText( item.getNowBldg() );
		
		
		return cView;
	}

};
