package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.jinsit.kmec.R;

public class PartCheckListAdapter extends BaseAdapter {

	public class ViewHolder {

		public TextView tv_wt_checkNm;
		public RadioGroup rdg_wt_abc, rdg_wt_ox;
		public CheckBox cb_wt_holdOver;
		public TextView et_wt_numeric;
		public LinearLayout ll_wt_checkList;
	}

	public ArrayList<PartCheckListWidgetData> widgetData;

	public ViewHolder holder;
	private Context mContext;
	public ArrayList<PartCheckListData> partCheckListData;
	private ArrayList<PartCheckListData> orgPartCheckListData;
	private boolean[] isCheckedArray;

	public PartCheckListAdapter(Context context,
			ArrayList<PartCheckListData> value) {
		super();
		this.mContext = context;
		this.isCheckedArray = new boolean[value.size()];
		this.widgetData = new ArrayList<PartCheckListWidgetData>();
		setWorkStatusListItems(value);
	}

	public void setWorkStatusListItems(ArrayList<PartCheckListData> value) {
		this.orgPartCheckListData = value;
		this.partCheckListData = new ArrayList<PartCheckListData>();

		for (PartCheckListData PartCheckListData : this.orgPartCheckListData) {
			this.partCheckListData.add(PartCheckListData);
			this.widgetData.add(new PartCheckListWidgetData());
		}

		this.widgetDataInit(partCheckListData, widgetData);

		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partCheckListData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return partCheckListData.get(position);
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
			convertView = inflater
					.inflate(R.layout.listitem_wt_checklist, null);
			holder.ll_wt_checkList = (LinearLayout) convertView
					.findViewById(R.id.ll_wt_checkList);
			holder.tv_wt_checkNm = (TextView) convertView
					.findViewById(R.id.tv_wt_checkNm);
			holder.rdg_wt_abc = (RadioGroup) convertView
					.findViewById(R.id.rdg_wt_abc);
			holder.rdg_wt_ox = (RadioGroup) convertView
					.findViewById(R.id.rdg_wt_ox);
			holder.et_wt_numeric = (TextView) convertView
					.findViewById(R.id.et_wt_numeric);
			holder.cb_wt_holdOver = (CheckBox) convertView
					.findViewById(R.id.cb_wt_holdOver);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PartCheckListData mData = partCheckListData.get(position);
		PartCheckListWidgetData mWidgetData = widgetData.get(position);
		holder.tv_wt_checkNm.setText(mData.getSMART_DESC());
		unCheckedBg(mWidgetData.isUnChecked());
		switch (Integer.valueOf(mData.getINPUT_TP())) {
		case 1:
			// 상태 (abc)
			holder.rdg_wt_abc.setVisibility(View.VISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);
			holder.rdg_wt_abc
					.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							widgetData.get(position).setRadioABC(checkedId);
							switch (checkedId) {
							case R.id.rd_wt_a:
								widgetData.get(position).setInputTp1("A");
								break;
							case R.id.rd_wt_b:
								widgetData.get(position).setInputTp1("B");
								break;
							case R.id.rd_wt_c:
								widgetData.get(position).setInputTp1("C");
								break;
							}

						}
					});
			holder.rdg_wt_abc.check(mWidgetData.getRadioABC());

			break;
		case 3:
			// 수치 edittext
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.VISIBLE);
			holder.et_wt_numeric.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					widgetData.get(position).setUnChecked(false);
					unCheckedBg(widgetData.get(position).isUnChecked());
					LayoutInflater inflater = (LayoutInflater) mContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					final LinearLayout dial = (LinearLayout) inflater.inflate(
							R.layout.dialog_edittext, null);

					final EditText et = (EditText) dial
							.findViewById(R.id.et_numeric);

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
											widgetData
													.get(position)
													.setNumbericText(
															et.getText()
																	.toString());
											holder.et_wt_numeric.setText(et
													.getText().toString());
											widgetData
													.get(position)
													.setInputTp3(
															et.getText()
																	.toString());
										}
									}).show();
				}
			});
			holder.et_wt_numeric.setText(mWidgetData.getNumbericText());
			notifyDataSetChanged();// 리프레시 해주도록
			break;
		case 7:
			// 유무 (ox)
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.VISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);

			holder.rdg_wt_ox
					.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							widgetData.get(position).setRadioOX(checkedId);
							switch (checkedId) {
							case R.id.rd_wt_o:
								widgetData.get(position).setInputTp7("1");
								break;
							case R.id.rd_wt_x:
								widgetData.get(position).setInputTp7("0");
								break;

							}

						}
					});
			holder.rdg_wt_ox.check(mWidgetData.getRadioOX());

			break;
		}

		// 이월여부 있는지 판단

		// 체크박스의 상태 변화를 체크한다.
		holder.cb_wt_holdOver
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 체크를 할 때
						widgetData.get(position).setCheckBox(isChecked);
						if (isChecked) {
							widgetData.get(position).setOverMonth("Y");
						} else {
							widgetData.get(position).setOverMonth("N");
						}
					}
				});
	
		holder.cb_wt_holdOver.setChecked(mWidgetData.isCheckBox());

		if (mData.getMONTH_CHK_IF().equals("1")) {
			holder.cb_wt_holdOver.setVisibility(View.VISIBLE);
		} else {
			holder.cb_wt_holdOver.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	@SuppressLint("ResourceAsColor")
	private void unCheckedBg(boolean isUnChecked){
		
		if(isUnChecked){
			holder.ll_wt_checkList.setBackgroundColor(R.color.comm_boldFont_color);
		}else{
			holder.ll_wt_checkList.setBackgroundColor(0);
		}
	
	}
	/**
	 * 위젯 데이터 초기화
	 * 점검항목의 디폴트 값을 셋팅한다.
	 * @param mData
	 * @param wData
	 */
	private void widgetDataInit(ArrayList<PartCheckListData> mData,
			ArrayList<PartCheckListWidgetData> wData) {

		for (int i = 0; i < mData.size(); i++) {
			wData.get(i).setInputTp(mData.get(i).getINPUT_TP());
			wData.get(i).setCsItemCd(mData.get(i).getCS_ITEM_CD());
			switch (Integer.valueOf(mData.get(i).getINPUT_TP())) {
			case 1:
				// abc
				wData.get(i).setInputTp1(mData.get(i).getINPUT_TP1());
				wData.get(i).setRadioABC(getResId(wData.get(i).getInputTp1()));
				break;
			case 3:
				// numeric
				wData.get(i).setInputTp3(mData.get(i).getINPUT_TP3());
				break;
			case 7:
				// ox
				wData.get(i).setInputTp7(mData.get(i).getINPUT_TP7());
				wData.get(i).setRadioOX(getResId(wData.get(i).getInputTp7()));
				break;
			}
			Log.i("logi", "log I = " + i);
		}
	}
	
	private int getResId(String value){
		int resId = 0 ;
		 if(value.equals("A")){
			 resId =R.id.rd_wt_a;
		 }else if(value.equals("B")){
			 resId =R.id.rd_wt_b;
		 }else if(value.equals("C")){
			 resId =R.id.rd_wt_c;
		 }else if(value.equals("1")){
			 resId =R.id.rd_wt_o;
		 }else if(value.equals("0")){
			 resId =R.id.rd_wt_x;
		 }
		return resId;
		
	}
	public void setChecked(int position) {
		isCheckedArray[position] = !isCheckedArray[position];
	}

	public ArrayList<Integer> getChecked() {
		int tempSize = isCheckedArray.length;
		ArrayList<Integer> mArrayList = new ArrayList<Integer>();
		for (int i = 0; i < tempSize; i++) {
			if (isCheckedArray[i]) {
				mArrayList.add(i);
			}
		}
		return mArrayList;
	}

}
