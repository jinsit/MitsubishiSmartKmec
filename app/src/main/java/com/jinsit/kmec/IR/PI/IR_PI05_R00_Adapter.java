package com.jinsit.kmec.IR.PI;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

import java.util.List;

public class IR_PI05_R00_Adapter extends BaseAdapter {

	Context context;
	int layout;
	List<IR_PI05_R00_Item> dataList = null;

	private OnClickListener onClickListener;
	public IR_PI05_R00_Adapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI05_R00_Adapter(Context context, int layout,
                               List<IR_PI05_R00_Item> dataList, OnClickListener onClickListener) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.onClickListener = onClickListener;
	}

	private class ViewHolder {
		private TextView PItemNo;
		private TextView DS_DRAW_NO;
		private TextView GL_NO;
		private TextView DS_DRAW_NM;
		private TextView QTY;
		private TextView ITEM_NO;
		private TextView REMARK;

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
		final IR_PI05_R00_Item item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.PItemNo = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_pitem_no);
			holder.DS_DRAW_NO = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_ds_draw_no);
			holder.GL_NO = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_gl_no);
			holder.DS_DRAW_NM = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_ds_draw_nm);
			holder.QTY = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_qty);
			holder.ITEM_NO = (TextView) convertView
					.findViewById(R.id.tv_pi_adapter_itemno);
			holder.REMARK = (TextView) convertView.findViewById(R.id.tv_pi_adapter_remark);



			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.PItemNo.setText(item.getPITEM_NO());
		holder.DS_DRAW_NO.setText(item.getDS_DRAW_NO());
		holder.GL_NO.setText(item.getGL_NO());
		holder.DS_DRAW_NM.setText(item.getDS_DRAW_NM());
		holder.QTY.setText(item.getQTY());
		holder.ITEM_NO.setText(item.getITEM_NO());
		holder.REMARK.setText(item.getREMARK());

		holder.GL_NO.setSingleLine(true);
		holder.GL_NO.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		holder.GL_NO.setSelected(true);
		holder.DS_DRAW_NM.setSingleLine(true);
		holder.DS_DRAW_NM.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		holder.DS_DRAW_NM.setSelected(true);
		holder.REMARK.setSingleLine(true);
		holder.REMARK.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		holder.REMARK.setSelected(true);

		return convertView;
	}

}