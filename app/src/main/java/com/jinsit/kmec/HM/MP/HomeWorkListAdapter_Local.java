package com.jinsit.kmec.HM.MP;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;

public class HomeWorkListAdapter_Local extends SimpleCursorAdapter {

	private final Context mContext;
	private final int mLayout;
	public Cursor mCursor;
	private final LayoutInflater mLayoutInflater;
	public ViewHolder holder;

	private class ViewHolder {

		public TextView tv_home_bldgNm, tv_home_workNm,tv_home_carNo;
		public ImageView iv_home_jobDivision;
	}

	public HomeWorkListAdapter_Local(Context context, int layout, Cursor c,
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
		holder.iv_home_jobDivision = (ImageView)view.findViewById(R.id.iv_home_jobDivision);
		holder.tv_home_bldgNm = (TextView)view.findViewById(R.id.tv_home_bldgNm);
		holder.tv_home_workNm = (TextView)view.findViewById(R.id.tv_home_workNm);
		view.setTag(holder);
		return view;
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		mCursor = cursor;
		holder = (ViewHolder) view.getTag();
		final int position = mCursor.getPosition();
		String tv_wo_jobTime = cursor.getString(cursor.getColumnIndex("WORK_DT"));
		String tv_wo_workNm = cursor.getString(cursor.getColumnIndex("WORK_NM"));
		String tv_wo_carNo = cursor.getString(cursor.getColumnIndex("CAR_NO"));
		String tv_wo_bldgNm = cursor.getString(cursor.getColumnIndex("BLDG_NM"));
		String tv_wo_st = cursor.getString(cursor.getColumnIndex("ST"));
		String tv_wo_csFR = cursor.getString(cursor.getColumnIndex("CS_FR"));
		String tv_wo_yCNT = cursor.getString(cursor.getColumnIndex("Y_CNT"));
		String tv_wo_tCNT = cursor.getString(cursor.getColumnIndex("T_CNT"));


		if(tv_wo_bldgNm.equals("")){
			holder.tv_home_bldgNm.setText("빌딩정보가 없습니다.");
		}else{
			holder.tv_home_bldgNm.setText(tv_wo_bldgNm+ " " + tv_wo_carNo);
		}
		holder.tv_home_workNm.setText(tv_wo_workNm);
		if(tv_wo_workNm.equals("정기점검")){
			holder.iv_home_jobDivision.setImageResource(R.drawable.division_check);
		}else if(tv_wo_workNm.equals("고장수리")){
			holder.iv_home_jobDivision.setImageResource(R.drawable.division_breakdown);
		}else{
			holder.iv_home_jobDivision.setImageResource(R.drawable.division_normal);
		}

	}



}
