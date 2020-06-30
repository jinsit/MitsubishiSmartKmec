package com.jinsit.kmec.IR.ES;

import java.util.List;

import com.jinsit.kmec.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InspectorByTeam_Adapter extends BaseAdapter {

	private Context context;
	private int layout;
	private List<InspectorByTeam> dataList = null;

	public InspectorByTeam_Adapter(){};
	public InspectorByTeam_Adapter(Context context, int layout, List<InspectorByTeam> dataList) {
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
		TextView workNm;
		TextView nowBldg;
	}
	
	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final InspectorByTeam item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.deptNm = (TextView) cView.findViewById(R.id.tv01_deptNm);
			holder.empNm  = (TextView) cView.findViewById(R.id.tv02_empNm);
			holder.workNm = (TextView) cView.findViewById(R.id.tv03_workNm);
			holder.nowBldg = (TextView) cView.findViewById(R.id.tv04_nowBldg);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.deptNm.setText(item.getDEPT_NM());
		holder.empNm.setText(item.getEMP_NM());
		holder.workNm.setText(item.getMNG_USR_ID());
		holder.nowBldg.setText(item.getEMP_NO());
		
		
		return cView;
	}

};
