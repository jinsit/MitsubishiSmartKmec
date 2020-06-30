package com.jinsit.kmec.SM.CR;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class SM_CR02_R00_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<SM_CR02_R00_Item01> dataList = null;

	public SM_CR02_R00_Adapter01(){};
	public SM_CR02_R00_Adapter01(Context context, int layout, List<SM_CR02_R00_Item01> dataList) {
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
		
		private TextView ngNo;
		private TextView empNm;
		private TextView prjNm_prjNo;
		
	}

	@Override
	public View getView(int position, View cView, ViewGroup parent) {
		
		final SM_CR02_R00_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		
		if(cView == null){
			cView = View.inflate(context, layout, null);
			
			holder = new ViewHolder();
			holder.ngNo  		= (TextView) cView.findViewById(R.id.tv01_cr02_ngNo);
			holder.empNm  		= (TextView) cView.findViewById(R.id.tv02_cr02_empNm);
			holder.prjNm_prjNo  = (TextView) cView.findViewById(R.id.tv03_cr02_prjNm_prjNo);
			cView.setTag(holder);
			
		}else{
			holder = (ViewHolder) cView.getTag();
		}
		
		holder.ngNo.setText( item.getNgNo() );
		holder.empNm.setText( item.getEmpNm() );
		holder.prjNm_prjNo.setText( item.getProjectNm() +"("+item.getProjectNo() +")" );
		return cView;
	}

};