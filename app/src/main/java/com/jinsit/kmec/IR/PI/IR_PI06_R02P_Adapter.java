package com.jinsit.kmec.IR.PI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SysUtil;

import java.util.ArrayList;

public class IR_PI06_R02P_Adapter extends BaseAdapter{

	private class ViewHolder {

		public TextView tv_pi06_itemNo;
		public TextView tv_pi06_itemNm;
		public TextView tv_pi06_itemNm2;
		public TextView tv_pi06_drawNo;
		public TextView tv_pi06_glNo;
		public TextView tv_pi06_size;
		public TextView tv_pi06_unitPrice;
		public LinearLayout ll_pi06_partsImage;
		public ImageView iv_pi06_partsImage;
		public CheckBox chk_pi06_check;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<IR_PI06_R01P_Item> ir_pi06_r01_items;
	private boolean[] isChekcedConfirm;
	View.OnClickListener imageClickListener;


	public IR_PI06_R02P_Adapter(Context context,
                                ArrayList<IR_PI06_R01P_Item> resData,
                                View.OnClickListener imageClickListener) {
		super();
		this.mContext = context;
		this.imageClickListener = imageClickListener;

		setReceiptListItems(resData);
		this.isChekcedConfirm = new boolean[ir_pi06_r01_items.size()];
	}
	
	public void setChecked(int position) {
		isChekcedConfirm[position] = !isChekcedConfirm[position];
	}

//	public ArrayList<Integer> getChecked() {
//		ArrayList<Integer> mArrayList = new ArrayList<Integer>();
//		for(int i =0; i<isChekcedConfirm.length; i++){
//			if(isChekcedConfirm[i]){
//				mArrayList.add(i);
//			}
//		}
//		return mArrayList;
//	}
	public boolean getChecked(int position) {
		return isChekcedConfirm[position];
	}
	public void setReceiptListItems(ArrayList<IR_PI06_R01P_Item> value) {
		this.ir_pi06_r01_items = value;

		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ir_pi06_r01_items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ir_pi06_r01_items.get(position);
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
			convertView = inflater.inflate(R.layout.ir_pi06_r02p_adapter, null);
			holder.tv_pi06_itemNo = (TextView)convertView.findViewById(R.id.tv_pi06_itemNo);
			holder.tv_pi06_itemNm = (TextView)convertView.findViewById(R.id.tv_pi06_itemNm);
			holder.tv_pi06_itemNm2 = (TextView)convertView.findViewById(R.id.tv_pi06_itemNm2);
			holder.tv_pi06_drawNo = (TextView)convertView.findViewById(R.id.tv_pi06_drawNo);
			holder.tv_pi06_glNo = (TextView)convertView.findViewById(R.id.tv_pi06_glNo);
			holder.tv_pi06_size = (TextView)convertView.findViewById(R.id.tv_pi06_size);
			holder.tv_pi06_unitPrice = (TextView)convertView.findViewById(R.id.tv_pi06_unitPrice);

			holder.ll_pi06_partsImage = (LinearLayout) convertView.findViewById(R.id.ll_pi06_partsImage);
			holder.iv_pi06_partsImage = (ImageView)convertView.findViewById(R.id.iv_pi06_partsImage);

			holder.chk_pi06_check = (CheckBox)convertView.findViewById(R.id.chk_pi06_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IR_PI06_R01P_Item mData = ir_pi06_r01_items.get(position);
		holder.tv_pi06_itemNo.setText(mData.getItemId());
		holder.tv_pi06_itemNm.setText(mData.getItemNm());
		holder.tv_pi06_itemNm2.setText(mData.getItemNm2());
		holder.tv_pi06_drawNo.setText(mData.getDrawNo());
		holder.tv_pi06_glNo.setText(mData.getGlNo() + "| ");
		holder.tv_pi06_size.setText(mData.getSize() + "| ");
		holder.tv_pi06_unitPrice.setText(SysUtil.makeStringWithComma(mData.getUnitPrice(), true));
		if("Y".equals(mData.getImageYN())){
			holder.iv_pi06_partsImage.setImageResource(R.drawable.btn_showpicture);
			//holder.ll_pi06_partsImage.setVisibility(View.VISIBLE);
		}else{
			holder.iv_pi06_partsImage.setImageResource(R.drawable.btn_addpicture);
			//holder.ll_pi06_partsImage.setVisibility(View.GONE);
		}
		if(imageClickListener != null){
			holder.iv_pi06_partsImage.setTag(R.id.tag_ir_pi06_image_yn, mData.getImageYN());	//이미지 유무
        holder.iv_pi06_partsImage.setTag(R.id.tag_ir_pi06_image_no, mData.getItemId());	//품번

        holder.iv_pi06_partsImage.setOnClickListener(imageClickListener);
        }
		holder.chk_pi06_check.setClickable(false);
		holder.chk_pi06_check.setFocusable(false);
		holder.chk_pi06_check.setChecked(isChekcedConfirm[position]);

	return convertView;
	}

}
