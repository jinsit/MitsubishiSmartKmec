package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ActionViewEvent;
import com.jinsit.kmec.comm.jinLib.UnderLineString;

public class CM_SelectInspector_Adapter extends BaseAdapter implements OnClickListener {

	private Context context;
	public ArrayList<CM_SelectInspector_ITEM> dataList;
	
	public CM_SelectInspector_Adapter(Context context,
			ArrayList<CM_SelectInspector_ITEM> dataList) {
		super();
		this.context = context;
		this.dataList = dataList;
	}

	
	private class ViewHolder {
	 TextView tv_insp_deptNm;
	 TextView tv_insp_empNm;  
	 TextView tv_insp_phone1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CM_SelectInspector_ITEM item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			
			holder = new ViewHolder();
		
				convertView = View.inflate(context, R.layout.cm_selectinspector_adapter, null);
				holder.tv_insp_deptNm = (TextView) convertView
						.findViewById(R.id.tv_insp_deptNm);
				holder.tv_insp_empNm = (TextView) convertView
						.findViewById(R.id.tv_insp_empNm);
				holder.tv_insp_phone1 = (TextView) convertView
						.findViewById(R.id.tv_insp_phone1);
				holder.tv_insp_phone1.setOnClickListener(this);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
			holder.tv_insp_deptNm.setText(item.getDeptNm());
			holder.tv_insp_empNm.setText(item.getEmpNm());
			holder.tv_insp_phone1.setText(UnderLineString.getUnderLineString(item.getPhone1()));
		return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_insp_phone1:
			TextView phone = (TextView)v;
			ActionViewEvent.callAction(context,phone.getText().toString());
			break;
			
		}
	}
}
