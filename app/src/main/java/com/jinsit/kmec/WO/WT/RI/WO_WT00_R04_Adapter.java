package com.jinsit.kmec.WO.WT.RI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.IR.PI.IR_PI06_R00_Item;
import com.jinsit.kmec.R;

import java.util.ArrayList;

public class WO_WT00_R04_Adapter extends BaseAdapter {

	private class ViewHolder {
		private TextView tvChkItemNm;
		private TextView tvPreCheckResult;
		private TextView tvSelCheckResult;
		private TextView tvRemark;
		private Button btnResultChange;
		private LinearLayout ll_remark;
		private LinearLayout llCheckParent;
	}

	private ViewHolder holder;
	private Context mContext;
	private ArrayList<WO_WT00_R04_Detail> wO_WT00_R04_DetailList;
	private String[] spinnerItems;
	private ArrayList<IR_PI06_R00_Item> spinnerItemCd;
	public WO_WT00_R04_Adapter(Context context,
							   ArrayList<WO_WT00_R04_Detail> list,
							   ArrayList<IR_PI06_R00_Item> spinnerItem) {
		super();
		this.mContext = context;
		wO_WT00_R04_DetailList = list;
		spinnerItemCd = spinnerItem;
		spinnerItems = new String[spinnerItem.size()];
		for(int i=0; i< spinnerItem.size(); i++){
			spinnerItems[i] = spinnerItem.get(i).getNM();
		}
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wO_WT00_R04_DetailList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return wO_WT00_R04_DetailList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.wo_wt00_r04_adapter, null);
			holder.tvChkItemNm = (TextView)convertView.findViewById(R.id.tvChkItemNm);
			holder.tvPreCheckResult = (TextView)convertView.findViewById(R.id.tvPreCheckResult);
			holder.tvSelCheckResult = (TextView)convertView.findViewById(R.id.tvSelCheckResult);
			holder.tvRemark = (TextView)convertView.findViewById(R.id.tvRemark);
			holder.btnResultChange = (Button) convertView.findViewById(R.id.btnResultChange);
			holder.ll_remark = (LinearLayout) convertView.findViewById(R.id.ll_remark);
			holder.llCheckParent = (LinearLayout)convertView.findViewById(R.id.llCheckParent);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final WO_WT00_R04_Detail mData = wO_WT00_R04_DetailList.get(position);
		holder.tvChkItemNm.setText(mData.getSEL_CHK_ITEM_NM());
		holder.tvPreCheckResult.setText(mData.getPRE_CHK_RESULT());
		holder.tvSelCheckResult.setText(mData.getSEL_CHK_RESULT_NM());
		holder.tvRemark.setText(mData.getREMARK());
		//체크리절트 Status 값이 A, D, E가 아니면 비고를 입력 할 수 있게 해야한다.
		if(isEnableRemark(mData.getSEL_CHK_RESULT())){
			holder.ll_remark.setVisibility(View.VISIBLE);
		}else{
			holder.ll_remark.setVisibility(View.GONE);
		}

		if("Y".equalsIgnoreCase(mData.getHEADER_IF())){
			holder.btnResultChange.setVisibility(View.GONE);
			holder.tvPreCheckResult.setVisibility(View.GONE);
			holder.tvSelCheckResult.setVisibility(View.GONE);
			holder.ll_remark.setVisibility(View.GONE);
			holder.llCheckParent.setBackgroundResource(R.color.comm_edit_bg);
			holder.tvChkItemNm.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


		}else{
			holder.btnResultChange.setVisibility(View.VISIBLE);
			holder.tvPreCheckResult.setVisibility(View.VISIBLE);
			holder.tvSelCheckResult.setVisibility(View.VISIBLE);
			holder.llCheckParent.setBackgroundColor(0);
			holder.tvChkItemNm.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));
		}

		/*holder.btnResultChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout dial = (LinearLayout) inflater.inflate(R.layout.wt_mw00_r02_dialog_edittext, null);
				TextView dTitle = (TextView) dial.findViewById(R.id.dialTitle);
				dTitle.setText("상태값을 입력하세요.\n 상태값은 A~E 까지 입력 가능합니다.");
				etRmk = (EditText) dial.findViewById(R.id.et_mw_r02FailureInput);
				//etRmk.setInputType(InputType.TYPE_CLASS_NUMBER);
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
										String status = etRmk.getText().toString();
										if(status == null || status.equals("")){
											AlertView.showAlert("문자를 입력해주세요.", mContext);
											return;
										}
										String firstWord = status.substring(0, 1);
										if(firstWord.equalsIgnoreCase("A") ||
												firstWord.equalsIgnoreCase("B") ||
												firstWord.equalsIgnoreCase("C") ||
												firstWord.equalsIgnoreCase("D") ||
												firstWord.equalsIgnoreCase("E")){
											wO_WT00_R04_DetailList.get(position).setSEL_CHK_RESULT(status.toUpperCase());
											notifyDataSetChanged();
										}
										else{
											AlertView.showAlert("A~E 까지 문자만 입력해주세요.", mContext);
											return;
										}

									}
								}).show();
			}
		});*/


		holder.btnResultChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
						.setTitle("상태값을 선택하세요.")
						.setItems(spinnerItems, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								wO_WT00_R04_DetailList.get(position).setSEL_CHK_RESULT_NM(spinnerItems[which].toString());
								wO_WT00_R04_DetailList.get(position).setSEL_CHK_RESULT(spinnerItemCd.get(which).getCD());
								notifyDataSetChanged();
							}
						}).show();
			}
		});
		holder.tvRemark.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout dial = (LinearLayout) inflater.inflate(R.layout.wt_mw00_r02_dialog_edittext, null);
				TextView dTitle = (TextView) dial.findViewById(R.id.dialTitle);
				dTitle.setText("특기사항을 입력하세요.");
				etRmk = (EditText) dial.findViewById(R.id.et_mw_r02FailureInput);
                etRmk.setText(mData.getREMARK());
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
										String remark = etRmk.getText().toString();
										wO_WT00_R04_DetailList.get(position).setREMARK(remark);
										holder.tvRemark.setText(remark);
										notifyDataSetChanged();
									}
								}).show();
			}
			//}
		});
		return convertView;
	}

	private boolean isEnableRemark(String status){
		if("A".equalsIgnoreCase(status) || "D".equalsIgnoreCase(status) || "E".equalsIgnoreCase(status)){
			return false;
		}else{
			return true;
		}
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

	public boolean isNoInputRemark(){
		boolean isNoInputRemark = false;

		for (WO_WT00_R04_Detail item :wO_WT00_R04_DetailList) {
			String checkResult = item.getSEL_CHK_RESULT();
			if("B".equalsIgnoreCase(checkResult) || "C".equalsIgnoreCase(checkResult)){
				if(item.getREMARK() == null || item.getREMARK().equals("") || item.getREMARK().isEmpty()){
					isNoInputRemark = true;
					break;
				}
			}
		}
		return isNoInputRemark;
	}


}
