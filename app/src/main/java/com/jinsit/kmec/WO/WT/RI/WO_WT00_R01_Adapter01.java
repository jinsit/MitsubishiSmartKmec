package com.jinsit.kmec.WO.WT.RI;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;

/**
 * WO_WT00_R01_Adatper01
 * 
 * @discription 작업대상 상세정보의 각 파트별 리스트 커서어댑터
 * @author 원성민
 *
 */
public class WO_WT00_R01_Adapter01 extends SimpleCursorAdapter {
	private final Context mContext;
	private final int mLayout;
	public Cursor mCursor;
	private final LayoutInflater mLayoutInflater;
	public ViewHolder holder;

	private class ViewHolder {

		public TextView tv_wt_nfcPlcNm, tv_wt_jobStNm;
		private LinearLayout ll_wt_partBg;
	}

	public WO_WT00_R01_Adapter01(Context context, int layout, Cursor c,
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
		holder.tv_wt_nfcPlcNm = (TextView) view
				.findViewById(R.id.tv_wt_nfcPlcNm);
		holder.tv_wt_jobStNm = (TextView) view.findViewById(R.id.tv_wt_jobStNm);
		holder.ll_wt_partBg = (LinearLayout)view.findViewById(R.id.ll_wt_partBg);
		view.setTag(holder);

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		mCursor = cursor;
		holder = (ViewHolder) view.getTag();
		final int position = mCursor.getPosition();
		String tv_wt_nfcPlcNm = cursor.getString(cursor
				.getColumnIndex("NFC_PLC_NM"));
		String tv_wt_jobStNm = cursor.getString(cursor
				.getColumnIndex("JOB_ST_NM"));
		
		holder.tv_wt_nfcPlcNm.setText(tv_wt_nfcPlcNm);
		//holder.tv_wt_jobStNm.setText(tv_wt_jobStNm);
		if(tv_wt_jobStNm.equals("미진행")){
			holder.ll_wt_partBg.setBackgroundResource(R.drawable.lv_ri_bar_before_bg);
		}else if(tv_wt_jobStNm.equals("완료")){
			holder.ll_wt_partBg.setBackgroundResource(R.drawable.lv_ri_bar_finish_bg);
		}else if(tv_wt_jobStNm.equals("진행중")){
			holder.ll_wt_partBg.setBackgroundResource(R.drawable.lv_ri_bar_ing_bg);
		}
	}
}
