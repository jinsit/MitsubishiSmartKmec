package com.jinsit.kmec.IR.CI;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

public class IR_CI05_R01P_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	private List<IR_CI05_R01P_ITEM02> dataList = null;
	
	public IR_CI05_R01P_Adapter01() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_CI05_R01P_Adapter01(Context context, int layout,
			List<IR_CI05_R01P_ITEM02> dataList) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
	}

	private class ViewHolder {
		private TextView carNo;
		private TextView modelNm;
		private TextView pCsPrc;
		private TextView rmk;

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
		final IR_CI05_R01P_ITEM02 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.carNo = (TextView) convertView
					.findViewById(R.id.tv_ci_adapter_carNo);
			holder.modelNm = (TextView) convertView
					.findViewById(R.id.tv_ci_adapter_modelNm);
			holder.pCsPrc = (TextView) convertView
					.findViewById(R.id.tv_ci_adapter_pCsPrc);
			holder.rmk = (TextView) convertView
					.findViewById(R.id.tv_ci_adapter_rmk);
			
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.carNo.setText(item.getCarNo());
		holder.modelNm.setText(item.getModelNm());
		holder.pCsPrc.setText(SysUtil.makeStringWithComma(item.getpCsPrc(),true));
		holder.rmk.setText(item.getRmk());
		return convertView;
	}

}