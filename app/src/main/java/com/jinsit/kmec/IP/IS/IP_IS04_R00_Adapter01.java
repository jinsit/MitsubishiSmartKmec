package com.jinsit.kmec.IP.IS;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

import java.util.List;

public class IP_IS04_R00_Adapter01 extends BaseAdapter {

	Context context;
	int layout;
	List<IP_IS04_R00_Item01> dataList = null;

	private OnClickListener onClickListener;


	public IP_IS04_R00_Adapter01(Context context, int layout,
								 List<IP_IS04_R00_Item01> dataList, OnClickListener onClickListener) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.onClickListener = onClickListener;
	}

	private class ViewHolder {
		private TextView WORK_DT;
		private TextView BLDG_NM;
		private TextView CAR_NO;
		private TextView ELEVATOR_NO;
		private TextView SEND_DATE;
		private TextView SEND_YN_NM;

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
		final IP_IS04_R00_Item01 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.WORK_DT = (TextView) convertView
					.findViewById(R.id.tv_is_adapter_work_dt);
			holder.BLDG_NM = (TextView) convertView
					.findViewById(R.id.tv_is_adapter_bldg_nm);
			holder.CAR_NO = (TextView) convertView
					.findViewById(R.id.tv_is_adapter_car_no);
			holder.ELEVATOR_NO = (TextView) convertView
					.findViewById(R.id.tv_is_adapter_elevator_no);
			holder.SEND_DATE = (TextView) convertView
					.findViewById(R.id.tv_is_adapter_send_date);
			holder.SEND_YN_NM = (TextView) convertView
					.findViewById(R.id.tv_is_adapter_send_yn_nm);




			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.WORK_DT.setText(item.getWORK_DT());
		holder.BLDG_NM.setText(item.getBLDG_NM());
		holder.CAR_NO.setText(item.getCAR_NO());
		holder.ELEVATOR_NO.setText(item.getELEVATOR_NO());
		holder.SEND_DATE.setText(item.getSEND_DATE());
		holder.SEND_YN_NM.setText(item.getSEND_YN_NM());


		return convertView;
	}

}