package com.jinsit.kmec.IR.PI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

import java.util.ArrayList;

public class IR_PI06_R00_Adapter extends BaseAdapter {

	private class ViewHolder {

		public TextView tv_pi06_seq;
		public TextView tv_pi06_carNo;
		public TextView tv_pi06_itemNm;
		public TextView tv_pi06_itemNo;
		public TextView tv_pi06_qty;
		public TextView tv_pi06_unitPrice;
		public TextView tv_pi06_amount;
		public TextView tv_pi06_size;
		public TextView tv_pi06_drawNo;

		public Button btn_changeQty;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<PartsItem> partsItems;;

	public IR_PI06_R00_Adapter(Context context,
								ArrayList<PartsItem> item) {
		super();
		this.mContext = context;
		this.partsItems = item;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partsItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return partsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.ir_pi06_r00_adapter, null);
			holder.tv_pi06_seq = (TextView)convertView.findViewById(R.id.tv_pi06_seq);
			holder.tv_pi06_carNo = (TextView)convertView.findViewById(R.id.tv_pi06_carNo);
			holder.tv_pi06_itemNm = (TextView)convertView.findViewById(R.id.tv_pi06_itemNm);
			holder.tv_pi06_itemNo = (TextView)convertView.findViewById(R.id.tv_pi06_itemNo);
			holder.tv_pi06_qty = (TextView)convertView.findViewById(R.id.tv_pi06_qty);
			holder.tv_pi06_unitPrice = (TextView)convertView.findViewById(R.id.tv_pi06_unitPrice);
			holder.tv_pi06_amount = (TextView)convertView.findViewById(R.id.tv_pi06_amount);
			holder.tv_pi06_size = (TextView)convertView.findViewById(R.id.tv_pi06_size);
			holder.tv_pi06_drawNo = (TextView)convertView.findViewById(R.id.tv_pi06_drawNo);
			holder.btn_changeQty = (Button)convertView.findViewById(R.id.btn_changeQty);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final PartsItem data = partsItems.get(position);
		int seq = position + 1;
		int qty = Integer.parseInt(data.getQuantity());
		String unitPrice = data.getUnitPrice();
		if(unitPrice == null || unitPrice.equals("")){
			unitPrice = "0";
		}
		int price = Integer.parseInt(unitPrice);
		int amt = (qty * price);
		holder.tv_pi06_seq.setText(seq + "");
		holder.tv_pi06_carNo.setText(data.getCarNo());
		holder.tv_pi06_itemNm.setText(data.getItemNm());
		holder.tv_pi06_itemNo.setText(data.getItemId());
		holder.tv_pi06_drawNo.setText(data.getDrawNo());
		holder.tv_pi06_size.setText(data.getSize());
		holder.tv_pi06_qty.setText("수량:"+data.getQuantity());
		holder.tv_pi06_unitPrice.setText("단가:"+ SysUtil.makeStringWithComma(unitPrice,true));
		holder.tv_pi06_amount.setText("금액:"+ SysUtil.makeStringWithComma(data.getAmt(data.getQuantity(),unitPrice),true));
		holder.btn_changeQty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout dial = (LinearLayout) inflater.inflate(R.layout.wt_mw00_r02_dialog_edittext, null);
				TextView dTitle = (TextView) dial.findViewById(R.id.dialTitle);
				dTitle.setText("수량을 입력하세요.");
				etRmk = (EditText) dial.findViewById(R.id.et_mw_r02FailureInput);
				etRmk.setInputType(InputType.TYPE_CLASS_NUMBER);
				hdRmk_etNumeric.sendEmptyMessageDelayed(0, 100);
				new AlertDialog.Builder(mContext)
						.setView(dial)
						.setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										return;
									}
								})

						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String qty = etRmk.getText().toString();
										partsItems.get(position).setQuantity(qty);
										notifyDataSetChanged();
									}
								}).show();
			}
		});
		return convertView;
	}



	/**
	 * 특기사항 입력 푸터가 빠지면서 일단은 사용하지 않는다
	 *
	 */
	EditText etRmk;

	private Handler hdRmk_etNumeric = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm;
			imm = (InputMethodManager) mContext.getSystemService("input_method");
			imm.showSoftInput(etRmk, 0);
		}
	};

}
