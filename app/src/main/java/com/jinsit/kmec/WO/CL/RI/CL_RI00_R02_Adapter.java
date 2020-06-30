package com.jinsit.kmec.WO.CL.RI;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.WO.WT.RI.PartCheckListWidgetData;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;

public class CL_RI00_R02_Adapter extends SimpleCursorAdapter {
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

		public TextView tv_wt_checkNm, tv_wt_rmk;
		public RadioGroup rdg_wt_abc, rdg_wt_ox;
		public RadioButton rd_wt_a, rd_wt_b, rd_wt_c, rd_wt_o, rd_wt_x,rd_wt_none, rd_wt_none_ox;//rd_wt_none 추가 (없음)


		//public CheckBox cb_wt_holdOver;
		public TextView  et_wt_numeric;
		public LinearLayout ll_wt_checkList, ll_wt_checkList_rmk, ll_wt_checkList_layout, ll_wt_header;

		public ImageView iv_wt_preInputTp;
		public TextView tv_wt_preInputTp;

		private TextView checkTypeTextView, checkCycleTextView, prevCheckMonthTextView;
		//헤더부분
		private TextView tv_wt_headerNm;
	}

	public CL_RI00_R02_Adapter(Context context, int layout, Cursor c,
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
				setPartCheckListWidgetData.setInputTp(mCursor.getString(mCursor.getColumnIndex("INPUT_TP")));
				setPartCheckListWidgetData.setInputTp1(mCursor.getString(mCursor.getColumnIndex("INPUT_TP1")));
				setPartCheckListWidgetData.setInputTp3(mCursor.getString(mCursor.getColumnIndex("INPUT_TP3")));
				setPartCheckListWidgetData.setInputTp7(mCursor.getString(mCursor.getColumnIndex("INPUT_TP7")));

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
		holder.ll_wt_checkList_rmk = (LinearLayout) view.findViewById(R.id.ll_wt_checkList_rmk);
		holder.ll_wt_checkList_layout = (LinearLayout) view.findViewById(R.id.ll_wt_checkList_layout);

		holder.tv_wt_checkNm = (TextView) view.findViewById(R.id.tv_wt_checkNm);
		holder.rdg_wt_abc = (RadioGroup) view.findViewById(R.id.rdg_wt_abc);
		holder.rd_wt_a = (RadioButton) view.findViewById(R.id.rd_wt_a);
		holder.rd_wt_b = (RadioButton) view.findViewById(R.id.rd_wt_b);
		holder.rd_wt_c = (RadioButton) view.findViewById(R.id.rd_wt_c);
		holder.rd_wt_none = (RadioButton) view.findViewById(R.id.rd_wt_none);
		holder.rd_wt_o = (RadioButton) view.findViewById(R.id.rd_wt_o);
		holder.rd_wt_x = (RadioButton) view.findViewById(R.id.rd_wt_x);
		holder.rd_wt_none_ox = (RadioButton) view.findViewById(R.id.rd_wt_none_ox);

		holder.rd_wt_a.setClickable(false);
		holder.rd_wt_b.setClickable(false);
		holder.rd_wt_c.setClickable(false);
		holder.rd_wt_none.setClickable(false);
		holder.rd_wt_o.setClickable(false);
		holder.rd_wt_x.setClickable(false);
		holder.rd_wt_none_ox.setClickable(false);

		holder.rdg_wt_ox = (RadioGroup) view.findViewById(R.id.rdg_wt_ox);
		holder.et_wt_numeric = (TextView) view.findViewById(R.id.et_wt_numeric);
		//holder.cb_wt_holdOver = (CheckBox) view.findViewById(R.id.cb_wt_holdOver);

		holder.tv_wt_rmk = (TextView) view.findViewById(R.id.tv_wt_rmk);
		holder.iv_wt_preInputTp = (ImageView) view.findViewById(R.id.iv_wt_preInputTp);
		holder.tv_wt_preInputTp = (TextView) view.findViewById(R.id.tv_wt_preInputTp);

		holder.ll_wt_header = (LinearLayout)view.findViewById(R.id.ll_wt_header);
		holder.tv_wt_headerNm = (TextView)view.findViewById(R.id.tv_wt_headerNm);
		holder.checkTypeTextView = (TextView)view.findViewById(R.id.checkTypeTextView);
		holder.checkCycleTextView = (TextView)view.findViewById(R.id.checkCycleTextView);
		holder.prevCheckMonthTextView = (TextView)view.findViewById(R.id.prevCheckMonthTextView);

		view.setTag(holder);

		return view;
	}

	int getResId(String value, String type){
		int resId = 0 ;
		if(type.equals("1"))
		{
			if(value.equals("A")){
				resId =R.id.rd_wt_a;
			}else if(value.equals("B")){
				resId =R.id.rd_wt_b;
			}else if(value.equals("C")){
				resId =R.id.rd_wt_c;
			}
			else if(value.equals("E")){
				resId =R.id.rd_wt_none;
			}
		}
		else if(type.equals("7"))
		{
			if(value.equals("1")){
				resId =R.id.rd_wt_o;
			}else if(value.equals("0")){
				resId =R.id.rd_wt_x;
			}
			else if(value.equals("E")){
				resId =R.id.rd_wt_none_ox;
			}
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
		final String smartDesc = cursor.getString(cursor.getColumnIndex("SMART_DESC"));
		final  String inputTp = cursor.getString(cursor.getColumnIndex("INPUT_TP"));
		final String monthChkIf = cursor.getString(cursor.getColumnIndex("MONTH_CHK_IF"));
		final String inputTp1 = cursor.getString(cursor.getColumnIndex("INPUT_TP1"));
		final String inputTp3 = cursor.getString(cursor.getColumnIndex("INPUT_TP3"));
		final String inputTp7 = cursor.getString(cursor.getColumnIndex("INPUT_TP7"));
		final String inputRmk = cursor.getString(cursor.getColumnIndex("INPUT_RMK"));
		final String overMonth = cursor.getString(cursor.getColumnIndex("OVER_MONTH"));
		final String csItemCd = cursor.getString(cursor.getColumnIndex("CS_ITEM_CD"));
		//2015 0406 yowonsm 추가된 컬럼 , 디폴드값 변경됐냐에 따라 비고란 표시
		final String defVal = cursor.getString(cursor.getColumnIndex("DEF_VAL"));
		final String defValSt = cursor.getString(cursor.getColumnIndex("DEF_VAL_ST"));
		//PartCheckListWidgetData mWidgetData = widgetData.get(cursor.getPosition());
		///2015 0406 yowonsm 추가된 컬럼 , 디폴드값 변경됐냐에 따라 비고란 표시
		//final String defVal = cursor.getString(cursor.getColumnIndex("DEF_VAL"));

		final String monthChk = cursor.getString(cursor.getColumnIndex("MONTH_CHK"));

		//20181218 이전점검항목/월 추가
		final String preWorkMM = cursor.getString(cursor.getColumnIndex("PRE_WORK_MM"));
		final String preInputTp = cursor.getString(cursor.getColumnIndex("PRE_INPUT_TP"));

		final String headerIf = cursor.getString(cursor.getColumnIndex("HEADER_IF"));
		final String stdSt = cursor.getString(cursor.getColumnIndex("STD_ST"));
		final String insp_method = cursor.getString(cursor.getColumnIndex("INSP_METHOD"));

		holder.tv_wt_checkNm.setText(smartDesc);
		if (monthChk.equals("Y")) {
			holder.tv_wt_checkNm.setTextColor(Color.BLUE);
			holder.tv_wt_checkNm.setTypeface(holder.tv_wt_checkNm.getTypeface(), Typeface.BOLD);

		} else {
			holder.tv_wt_checkNm.setTextColor(Color.BLACK);
			holder.tv_wt_checkNm.setTypeface(Typeface.DEFAULT);
		}

		if (headerIf.equals("1")) {
			holder.ll_wt_checkList.setVisibility(View.GONE);
			holder.ll_wt_checkList_rmk.setVisibility(View.GONE);
			holder.ll_wt_header.setVisibility(View.VISIBLE);
			holder.tv_wt_headerNm.setText(smartDesc);
		}
		else {
			holder.ll_wt_checkList.setVisibility(View.VISIBLE);
			holder.ll_wt_checkList_rmk.setVisibility(View.VISIBLE);
			holder.ll_wt_header.setVisibility(View.GONE);
		}

		String checkType = "";
		if("1".equals(insp_method)){
			checkType = "HEADER";
		}else if("2".equals(insp_method)){
			checkType = "육안";
		}else if("3".equals(insp_method)){
			checkType = "측정";
		}else if("4".equals(insp_method)){
			checkType = "시험";
		}
		holder.checkTypeTextView.setText(checkType);
		holder.checkCycleTextView.setText(stdSt);
		holder.prevCheckMonthTextView.setText(preWorkMM);


		unCheckedBg(widgetData.get(position).isUnChecked());

		widgetData.get(position).setInputTp(inputTp);
		widgetData.get(position).setInputTp3(inputTp3);
		widgetData.get(position).setInputTp1(inputTp1);
		widgetData.get(position).setInputTp7(inputTp7);
		holder.tv_wt_rmk.setText(inputRmk);

		switch (inputTp.isEmpty() ? 0 : Integer.valueOf(inputTp)) {
			case 0:
				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
				break;

			case 1:
				// 상태 (abc)
				holder.rdg_wt_abc.setVisibility(View.VISIBLE);
				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
				holder.et_wt_numeric.setVisibility(View.INVISIBLE);

				//mWidgetData.setInputTp1(inputTp1);
				holder.rdg_wt_abc.check(getResId(inputTp1, "1"));
				holder.rdg_wt_abc.setFocusable(false);
				if (isChangedDefaultValue(inputTp1, defVal)) {
					holder.ll_wt_checkList_rmk.setVisibility(View.GONE);

				} else {
					if (overMonth.equals("Y")) {
						holder.ll_wt_checkList_rmk.setVisibility(View.GONE);
					} else {
						holder.ll_wt_checkList_rmk.setVisibility(View.VISIBLE);
					}
				}

				break;
			case 3:
				// 수치 edittext

				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
				holder.et_wt_numeric.setVisibility(View.VISIBLE);
				//mWidgetData.setInputTp3(inputTp3);
				holder.et_wt_numeric.setText(inputTp3);

				break;
			case 7:
				// 유무 (ox)
				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
				holder.rdg_wt_ox.setVisibility(View.VISIBLE);
				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
				holder.rdg_wt_ox.check(getResId(inputTp7, "7"));
				holder.rdg_wt_ox.setFocusable(false);
				//mWidgetData.setInputTp7(inputTp7);

				if (isChangedDefaultValue(inputTp7, defVal)) {
					holder.ll_wt_checkList_rmk.setVisibility(View.GONE);
				} else {
					if (overMonth.equals("Y")) {
						holder.ll_wt_checkList_rmk.setVisibility(View.GONE);
					} else {
						holder.ll_wt_checkList_rmk.setVisibility(View.VISIBLE);
					}
				}
				break;
		}
		// 이월여부 있는지 판단


		//holder.cb_wt_holdOver.setChecked(mWidgetData.isCheckBox());

		/*if (monthChkIf.equals("1")) {
			holder.cb_wt_holdOver.setVisibility(View.VISIBLE);
		} else {
			holder.cb_wt_holdOver.setVisibility(View.INVISIBLE);
		}

		if (overMonth.equals("Y")) {
			holder.cb_wt_holdOver.setChecked(true);
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);

		} else {
			holder.cb_wt_holdOver.setChecked(false);

			if (inputTp.equals("1")) {
				holder.rdg_wt_abc.setVisibility(View.VISIBLE);
				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
				holder.et_wt_numeric.setVisibility(View.INVISIBLE);

			} else if (inputTp.equals("3")) {
				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
				holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
				holder.et_wt_numeric.setVisibility(View.VISIBLE);

			} else if (inputTp.equals("7")) {
				holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
				holder.rdg_wt_ox.setVisibility(View.VISIBLE);
				holder.et_wt_numeric.setVisibility(View.INVISIBLE);
			}

			holder.cb_wt_holdOver.setClickable(false);


		}*/

		if (inputTp.equals("1")) {
			holder.rdg_wt_abc.setVisibility(View.VISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);

		} else if (inputTp.equals("3")) {
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.INVISIBLE);
			holder.et_wt_numeric.setVisibility(View.VISIBLE);

		} else if (inputTp.equals("7")) {
			holder.rdg_wt_abc.setVisibility(View.INVISIBLE);
			holder.rdg_wt_ox.setVisibility(View.VISIBLE);
			holder.et_wt_numeric.setVisibility(View.INVISIBLE);
		}

	}

	boolean isChangedDefaultValue(String value, String defVal) {
		if (value.equals(defVal)) {
			return true;
		}
		return false;
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
}
