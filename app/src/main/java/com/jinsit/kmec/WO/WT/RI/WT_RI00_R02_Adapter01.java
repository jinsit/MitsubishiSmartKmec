package com.jinsit.kmec.WO.WT.RI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;

import java.util.ArrayList;

public class WT_RI00_R02_Adapter01 extends SimpleCursorAdapter {
	private final Context mContext;
	private final int mLayout;
	public Cursor mCursor;
	private final LayoutInflater mLayoutInflater;
	public ViewHolder holder;
	public ArrayList<PartCheckListWidgetData> widgetData;
	CommonSession commonSession;
	
	public String rJobNo, rNfcPlc;
	
	EditText et;
	private class ViewHolder {

		public TextView tv_wt_checkNm;
		public RadioGroup rdg_wt_abc, rdg_wt_ox;
		public RadioButton rd_wt_a, rd_wt_b, rd_wt_c, rd_wt_o, rd_wt_x;
		
	 
		public CheckBox cb_wt_holdOver;
		public TextView  et_wt_numeric;
		public LinearLayout ll_wt_checkList;
	}

	public WT_RI00_R02_Adapter01(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.commonSession = new CommonSession(mContext);
		this.mLayout = layout;
		this.mCursor = c;
		//--위젯데이터 초기화---//
		this.widgetData = new ArrayList<PartCheckListWidgetData>();
		PartCheckListWidgetData setPartCheckListWidgetData = new PartCheckListWidgetData();
		mCursor.moveToFirst();
		
		if(mCursor !=null&&mCursor.getCount()!=0){
			do{
				if(mCursor.getString(mCursor.getColumnIndex("MONTH_CHK_IF")).equals("1")){
					//초기화시 이월여부가 있는지 판단하여 1이면  overmonth를 셋팅해준다. 
					//최초에는 overmonth가 공백이기 때문에 체크박스상태 N을 공백일 경우 넣어주고 
					//공백이 아니면 db값을 넣어준다.
					//monthChkIf가 1이 아닌경우에는 overmonth = N으로 한다.
					if(mCursor.getString(mCursor.getColumnIndex("OVER_MONTH")).equals("")){
						setPartCheckListWidgetData.setOverMonth("N");	
					}else{
						setPartCheckListWidgetData.setOverMonth(mCursor.getString(mCursor.getColumnIndex("OVER_MONTH")));	
					}
				}else{
					setPartCheckListWidgetData.setOverMonth("N");	
				}
				setPartCheckListWidgetData.setSmartDesc(mCursor.getString(mCursor.getColumnIndex("SMART_DESC")));
				setPartCheckListWidgetData.setMonthChkIf(mCursor.getString(mCursor.getColumnIndex("MONTH_CHK_IF")));
				setPartCheckListWidgetData.setInputTp(mCursor.getString(mCursor.getColumnIndex("INPUT_TP")));
				setPartCheckListWidgetData.setInputTp1(mCursor.getString(mCursor.getColumnIndex("INPUT_TP1")));
				setPartCheckListWidgetData.setInputTp3(mCursor.getString(mCursor.getColumnIndex("INPUT_TP3")));
				setPartCheckListWidgetData.setInputTp7(mCursor.getString(mCursor.getColumnIndex("INPUT_TP7")));
				setPartCheckListWidgetData.setInputRmk(mCursor.getString(mCursor.getColumnIndex("INPUT_RMK")));
				setPartCheckListWidgetData.setOverMonth(mCursor.getString(mCursor.getColumnIndex("OVER_MONTH")));
				setPartCheckListWidgetData.setCsItemCd(mCursor.getString(mCursor.getColumnIndex("CS_ITEM_CD")));
				this.widgetData.add(setPartCheckListWidgetData);
				setPartCheckListWidgetData = new PartCheckListWidgetData();
		}while(mCursor.moveToNext());
	}
		//--위젯데이터 초기화---//
		this.mLayoutInflater = LayoutInflater.from(mContext);
	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mLayoutInflater.inflate(mLayout, null);
		holder = new ViewHolder();
		holder.ll_wt_checkList = (LinearLayout) view
				.findViewById(R.id.ll_wt_checkList);
		holder.tv_wt_checkNm = (TextView) view.findViewById(R.id.tv_wt_checkNm);
		holder.rdg_wt_abc = (RadioGroup) view.findViewById(R.id.rdg_wt_abc);
		holder.rd_wt_a = (RadioButton) view.findViewById(R.id.rd_wt_a);
		holder.rd_wt_b = (RadioButton) view.findViewById(R.id.rd_wt_b);
		holder.rd_wt_c = (RadioButton) view.findViewById(R.id.rd_wt_c);
		holder.rd_wt_o = (RadioButton) view.findViewById(R.id.rd_wt_o);
		holder.rd_wt_x = (RadioButton) view.findViewById(R.id.rd_wt_x);
		
		holder.rdg_wt_ox = (RadioGroup) view.findViewById(R.id.rdg_wt_ox);
		holder.et_wt_numeric = (TextView) view.findViewById(R.id.et_wt_numeric);
		holder.cb_wt_holdOver = (CheckBox) view
				.findViewById(R.id.cb_wt_holdOver);
		view.setTag(holder);

		return view;
	}
	
	 int getResId(String value){
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
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	
		mCursor = cursor;
		holder = (ViewHolder) view.getTag();
		final int position = mCursor.getPosition();
		final String jobNo = cursor.getString(cursor.getColumnIndex("JOB_NO"));
		final String nfcPlc = cursor.getString(cursor.getColumnIndex("NFC_PLC"));
		rJobNo = jobNo;
		rNfcPlc = nfcPlc;
		final PartCheckListWidgetData mWidgetData = widgetData.get(cursor.getPosition());
		final String smartDesc = mWidgetData.getSmartDesc();
		final  String inputTp = mWidgetData.getInputTp();
		final String monthChkIf =mWidgetData.getMonthChkIf();
		final String inputTp1 = mWidgetData.getInputTp1();
		final String inputTp3 =mWidgetData.getInputTp3();
		final String inputTp7 = mWidgetData.getInputTp7();
		final String inputRmk =mWidgetData.getInputRmk();
		final String overMonth = mWidgetData.getOverMonth();
		final String csItemCd = mWidgetData.getCsItemCd();
		
		

		holder.tv_wt_checkNm.setText(smartDesc);
		unCheckedBg(widgetData.get(position).isUnChecked());
		
		widgetData.get(position).setInputTp(inputTp);	
		widgetData.get(position).setInputTp3(inputTp3);
		switch (Integer.valueOf(inputTp)) {
	
		case 1:
			// 상태 (abc)
			holder.rdg_wt_abc.setVisibility(View.VISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);
			
			//mWidgetData.setInputTp1(inputTp1);
			holder.rdg_wt_abc.check(getResId(inputTp1));
			
			holder.rd_wt_a.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mWidgetData.setInputTp1("A");
					//updateOneItem("INPUT_TP1","A",jobNo,nfcPlc, csItemCd);
					holder.rdg_wt_abc.check(getResId(inputTp1));
					Log.i("rd_wt_a", "rd_wt_a" + inputTp);
				}
			});
			
			holder.rd_wt_b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mWidgetData.setInputTp1("B");
					//updateOneItem("INPUT_TP1","B",jobNo,nfcPlc, csItemCd);
					
					Log.i("rd_wt_b", "rd_wt_b" + inputTp);
				}
			});
			holder.rd_wt_c.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mWidgetData.setInputTp1("C");
					//updateOneItem("INPUT_TP1","C",jobNo,nfcPlc, csItemCd);
					
					Log.i("rd_wt_c", "rd_wt_c" + inputTp);
				}
			});
			
			
//			holder.rdg_wt_abc
//					.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(RadioGroup group,
//								int checkedId) {
//							// TODO Auto-generated method stub
//							//widgetData.get(position).setRadioABC(checkedId);
//							switch (checkedId) {
//							case R.id.rd_wt_a:
//								//widgetData.get(position).setInputTp1("A");
//								updateOneItem("INPUT_TP1","A",jobNo,nfcPlc, csItemCd);
//								
//								Log.i("rd_wt_a", "rd_wt_a" + inputTp);
//								break;
//							case R.id.rd_wt_b:
//								//widgetData.get(position).setInputTp1("B");
//								updateOneItem("INPUT_TP1","B",jobNo,nfcPlc, csItemCd);
//								Log.i("rd_wt_b", "rd_wt_b" + inputTp);
//								break;
//							case R.id.rd_wt_c:
//								Log.i("rd_wt_c", "rd_wt_c" + inputTp);
//								//widgetData.get(position).setInputTp1("C");
//								updateOneItem("INPUT_TP1","C",jobNo,nfcPlc, csItemCd);
//							
//								break;
//							}
//
//						}
//					});
		
			break;
		case 3:
			// 수치 edittext
			
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.VISIBLE);
			//mWidgetData.setInputTp3(inputTp3);
			holder.et_wt_numeric.setText(inputTp3);
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

					 et = (EditText) dial
							.findViewById(R.id.et_numeric);
					 hd_etNumeric.sendEmptyMessageDelayed(0,100);
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
											Log.i("CheckCheck", "CheckCheck" + inputTp);
											//widgetData.get(position).setNumbericText(
															//et.getText().toString());
											mWidgetData.setInputTp3(et.getText().toString());
											//widgetData.get(position).setInputTp3(et.getText().toString());
											//updateOneItem("INPUT_TP3",et.getText().toString(),jobNo,nfcPlc, csItemCd);
											holder.et_wt_numeric.setText(et.getText().toString());
											notifyDataSetChanged();// 리프레시 해주도록
														}
									}).show();
				}
			});
		
		
			break;
		case 7:
			// 유무 (ox)
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.VISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.check(getResId(inputTp7));
			//mWidgetData.setInputTp7(inputTp7);
			
			
			holder.rd_wt_o.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mWidgetData.setInputTp7("1");
					//updateOneItem("INPUT_TP7","1",jobNo,nfcPlc, csItemCd);
					
					Log.e("rd_wt_o", "rd_wt_o" + inputTp);
				}
			});
			
			holder.rd_wt_x.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mWidgetData.setInputTp7("0");
					//updateOneItem("INPUT_TP7","0",jobNo,nfcPlc, csItemCd);
					
					Log.e("rd_wt_x", "rd_wt_x" + inputTp);
				}
			});
			
//			holder.rdg_wt_ox
//					.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(RadioGroup group,
//								int checkedId) {
//							// TODO Auto-generated method stub
//							widgetData.get(position).setRadioOX(checkedId);
//							switch (checkedId) {
//							case R.id.rd_wt_o:
//								//widgetData.get(position).setInputTp7("1");
//								updateOneItem("INPUT_TP7","1",jobNo,nfcPlc, csItemCd);
//								Log.w("rd_wt_o", "rd_wt_o" + inputTp);
//								
//								break;
//							case R.id.rd_wt_x:
//								//widgetData.get(position).setInputTp7("0");
//								updateOneItem("INPUT_TP7","0",jobNo,nfcPlc, csItemCd);
//								Log.w("rd_wt_x", "rd_wt_x" + inputTp);
//								
//								break;
//
//							}
//
//						}
//					});
		

			break;
		}

		// 이월여부 있는지 판단

		// 체크박스의 상태 변화를 체크한다.
	
		holder.cb_wt_holdOver
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 체크를 할 때
						//widgetData.get(position).setCheckBox(isChecked);
						if (isChecked) {
							//widgetData.get(position).setOverMonth("Y");
							//updateOneItem("OVER_MONTH","Y",jobNo,nfcPlc, csItemCd);
							//overMonthSet(jobNo,nfcPlc, csItemCd);
							mWidgetData.setOverMonth("Y");
							Log.v("setOverMonth(Y);", "setOverMonth(y);" + inputTp);
						} else {
							//widgetData.get(position).setOverMonth("N");
							//updateOneItem("OVER_MONTH","N",jobNo,nfcPlc, csItemCd);
							mWidgetData.setOverMonth("N");
							Log.v("setOverMonth(N);", "setOverMonth(N);" + inputTp);
						}
					}
				});
	
		//holder.cb_wt_holdOver.setChecked(mWidgetData.isCheckBox());
	
		if (monthChkIf.equals("1")) {
			holder.cb_wt_holdOver.setVisibility(View.VISIBLE);
		} else {
			holder.cb_wt_holdOver.setVisibility(View.INVISIBLE);
		}
		
		if (mWidgetData.getOverMonth().equals("Y")) {
			holder.cb_wt_holdOver.setChecked(true);
//			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
//			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
//			holder.et_wt_numeric.setVisibility(View.INVISIBLE);
			
			

			if (inputTp.equals("1")) {
//				holder.rdg_wt_abc.setVisibility(View.VISIBLE);
//				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
//				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
//				
				holder.rd_wt_a.setClickable(false);
				holder.rd_wt_b.setClickable(false);
				holder.rd_wt_c.setClickable(false);
				//holder.rdg_wt_abc.setClickable(true);
			} else if (inputTp.equals("3")) {
//				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
//				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
//				holder.et_wt_numeric.setVisibility(View.VISIBLE);
				holder.et_wt_numeric.setClickable(false);
			} else if (inputTp.equals("7")) {
//				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
//				holder.rdg_wt_ox.setVisibility(View.VISIBLE);
//				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
//				holder.rdg_wt_ox.setClickable(true);
				holder.rd_wt_o.setClickable(false);
				holder.rd_wt_x.setClickable(false);
			}
			
	

		} else {
			holder.cb_wt_holdOver.setChecked(false);

			if (inputTp.equals("1")) {
//				holder.rdg_wt_abc.setVisibility(View.VISIBLE);
//				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
//				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
//				
				holder.rd_wt_a.setClickable(true);
				holder.rd_wt_b.setClickable(true);
				holder.rd_wt_c.setClickable(true);
				//holder.rdg_wt_abc.setClickable(true);
			} else if (inputTp.equals("3")) {
//				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
//				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
//				holder.et_wt_numeric.setVisibility(View.VISIBLE);
				holder.et_wt_numeric.setClickable(true);
			} else if (inputTp.equals("7")) {
//				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
//				holder.rdg_wt_ox.setVisibility(View.VISIBLE);
//				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
//				holder.rdg_wt_ox.setClickable(true);
				
				holder.rd_wt_o.setClickable(true);
				holder.rd_wt_x.setClickable(true);


			}
		}
		
	}
	void setOverMonth(){
		
	}
	
	@SuppressLint("ResourceAsColor")
	private void unCheckedBg(boolean isUnChecked){
		
		if(isUnChecked){
			holder.ll_wt_checkList.setBackgroundColor(R.color.comm_boldFont_color);
		}else{
			holder.ll_wt_checkList.setBackgroundColor(0);
		}
	
	}
	private void overMonthSet( String jobNo, String nfcPlc, String itemCd){
		String value = "";
		String where = "  CS_EMP_ID	= '" + commonSession.getEmpId() + "'"		
				+"  AND		 WORK_DT	= '" + commonSession.getWorkDt() + "'"		
				+"  AND	    	JOB_NO		= '" + jobNo + "'"		
				+"  AND		 NFC_PLC			= '" + nfcPlc + "'"		
				+"  AND		  CS_ITEM_CD		= '" + itemCd + "'"	;
		
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(mContext,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("INPUT_TP7", value);
		values.put("INPUT_TP3", value);
		values.put("INPUT_TP1", value);
		db.update("TCSQ213", values, where, null);
		db.close();
		dbHelper.close();
		//this.changeCursor(getCursor());
		cursorRequery();
	}
	private void updateOneItem(String key, String value, String jobNo, String nfcPlc, String itemCd){
		
		String where = "  CS_EMP_ID	= '" + commonSession.getEmpId() + "'"		
				+"  AND		 WORK_DT	= '" + commonSession.getWorkDt() + "'"		
				+"  AND	    	JOB_NO		= '" + jobNo + "'"		
				+"  AND		 NFC_PLC			= '" + nfcPlc + "'"		
				+"  AND		  CS_ITEM_CD		= '" + itemCd + "'"	;
		
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(mContext,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(key, value);
		db.update("TCSQ213", values, where, null);
	
		db.close();
		dbHelper.close();
		//this.changeCursor(getCursor());
		cursorRequery();
	}
	
	
	private void cursorRequery(){
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(mContext,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = new DatabaseRawQuery().selectCheckDetail(commonSession.getEmpId(),commonSession.getWorkDt(), rJobNo,rNfcPlc);
				Cursor requery = db.rawQuery(query,null); // 쿼리 날리고 
				
				this.changeCursor(requery);
				//mCursor = requery;
				
	}
	
	private void updateCheckList(String jobNo, String nfcPlc, 
			String type1,String type3,String type7,String rmk, String ovMonth,String itemCd) {
		
		String query = new DatabaseRawQuery().updateCheckList(commonSession.getEmpId(), commonSession.getWorkDt(), jobNo,
				nfcPlc,type1,type3,type7,rmk,ovMonth,itemCd);

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(mContext,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);

		db.close();
		dbHelper.close();
		
		//this.changeCursor(getCursor());
		//changeCursor(mCursor);
		//this.notifyDataSetChanged();
	}
	
	
	private Handler hd_etNumeric = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm;
			imm = (InputMethodManager) mContext.getSystemService("input_method");
			imm.showSoftInput(et, 0);
		}
	};
}
