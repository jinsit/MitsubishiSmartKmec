package com.jinsit.kmec.WO.WT.RI;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinsit.kmec.R;

/**
 * WO_WT00_R01_Adatper01
 * 
 * @discription 작업대상화면의 네비게이션 드로우 커서 어댑터
 * @author 원성민
 *
 */
public class WT_RI01_R00_Adapter01 extends SimpleCursorAdapter {
	private final Context mContext;
	private final int mLayout;
	public Cursor mCursor;
	private final LayoutInflater mLayoutInflater;
	public ViewHolder holder;

	private class ViewHolder {

		public TextView tv_wo_routineBldgNm, tv_wo_routineICnt,tv_wo_routineEText;
	}

	public WT_RI01_R00_Adapter01(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mLayout = layout;
		this.mCursor = c;
		this.mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mLayoutInflater.inflate(mLayout, null);
		holder = new ViewHolder();
		holder.tv_wo_routineBldgNm = (TextView)view.findViewById(R.id.tv_wo_routineBldgNm);
		holder.tv_wo_routineICnt = (TextView)view.findViewById(R.id.tv_wo_routineICnt);
		holder.tv_wo_routineEText = (TextView)view.findViewById(R.id.tv_wo_routineEText);
		view.setTag(holder);

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		mCursor = cursor;
		holder = (ViewHolder) view.getTag();
		final int position = mCursor.getPosition();
		String tv_wo_routineBldgNm = cursor.getString(cursor
				.getColumnIndex("BLDG_NM"));
		String iCnt = cursor.getString(cursor
				.getColumnIndex("I_CNT"));
		String tCnt = cursor.getString(cursor
				.getColumnIndex("T_CNT"));
		String tv_wo_routineEText = cursor.getString(cursor.getColumnIndex("E_TEXT"));
		holder.tv_wo_routineBldgNm.setText(tv_wo_routineBldgNm);
		holder.tv_wo_routineICnt.setText(iCnt + " / " + tCnt);
		holder.tv_wo_routineEText.setText(tv_wo_routineEText);
	}
}
