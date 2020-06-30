package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class CM_SearchElev_Adapter01 extends BaseAdapter {

	private Context context;
	private int selTp;
	private boolean[] isChecked;
	public ArrayList<CM_SearchElev_ITEM01> dataList;;

	public CM_SearchElev_Adapter01(Context context, int selTp,
			ArrayList<CM_SearchElev_ITEM01> dataList) {
		super();
		this.context = context;
		this.dataList = dataList;
		this.isChecked = new boolean[dataList.size()];
		this.selTp = selTp;
	}

	private class ViewHolder {
		TextView carNo;
		TextView modelNm;
		CheckBox elev;
		TextView dongCarNo;
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
	
	public void setChecked(int position) {
		isChecked[position] = !isChecked[position];
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
	
	public ArrayList<CM_SearchElev_ITEM01> getSelectedData() {
		ArrayList<CM_SearchElev_ITEM01> mArrayList = new ArrayList<CM_SearchElev_ITEM01>();
		for (int i = 0; i < isChecked.length; i++) {
			if (isChecked[i]) {
				mArrayList.add(dataList.get(i));
			}
		}
		return mArrayList;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CM_SearchElev_ITEM01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if (selTp == 1 || selTp == 2) {
				convertView = View.inflate(context,
						R.layout.cm_searchelev_adapter01, null);
				holder.carNo = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_carNo);
				holder.modelNm = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_modelNm);
				holder.dongCarNo = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_dongCarNo);
			} else {
				convertView = View.inflate(context,
						R.layout.cm_searchelev_adapter02, null);
				holder.carNo = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_carNo);
				holder.modelNm = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_modelNm);
				holder.elev = (CheckBox) convertView
						.findViewById(R.id.cb_cm_adapter_elev);
				holder.dongCarNo = (TextView) convertView
						.findViewById(R.id.tv_cm_adapter_dongCarNo);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (selTp == 1 || selTp == 2) {
			holder.carNo.setText(item.getCarNo());
			holder.modelNm.setText(item.getModelNm());
			holder.dongCarNo.setText(item.getDongCarNo());
		} else {
			holder.carNo.setText(item.getCarNo());
			holder.modelNm.setText(item.getModelNm());
			holder.elev.setClickable(false);
			holder.elev.setFocusable(false);
			holder.elev.setChecked(isChecked[position]);
			holder.dongCarNo.setText(item.getDongCarNo());
		}
		return convertView;
	}
}
