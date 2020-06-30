package com.jinsit.kmec.WO;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinsit.kmec.R;

/**
 * WO_WT00_R01F_Adatper01
 * 
 * @discription 작업대상목록 탭 커서 어댑터 인터넷이 안되거나 작업대상을 조회했을 때 length가 0 일 때 이 어댑터를 사용하여
 *              로컬 디비에 있는 정기점검 항목만 리스트에 셋한다.
 * @author 원성민
 *
 */
public class WO_WT00_R00_Adapter01 extends SimpleCursorAdapter {
	private final Context mContext;
	private final int mLayout;
	public Cursor mCursor;
	private final LayoutInflater mLayoutInflater;
	public ViewHolder holder;

	private class ViewHolder {

		public TextView tv_wo_jobTime, tv_wo_bldgNm, tv_wo_workNm, tv_wo_st,
		tv_wo_checkSt, tv_wo_carNo;
	}

	public WO_WT00_R00_Adapter01(Context context, int layout, Cursor c,
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
		holder.tv_wo_jobTime = (TextView)view.findViewById(R.id.tv_wo_jobTime);
		holder.tv_wo_workNm = (TextView)view.findViewById(R.id.tv_wo_workNm);
		holder.tv_wo_carNo = (TextView)view.findViewById(R.id.tv_wo_carNo);
		holder.tv_wo_bldgNm = (TextView)view.findViewById(R.id.tv_wo_bldgNm);
		holder.tv_wo_st = (TextView)view.findViewById(R.id.tv_wo_st);
		holder.tv_wo_checkSt = (TextView)view.findViewById(R.id.tv_wo_checkSt);
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
		
		
		holder.tv_wo_jobTime.setText(tv_wo_jobTime);
		holder.tv_wo_workNm.setText(tv_wo_workNm);
		holder.tv_wo_carNo.setText(tv_wo_carNo);
		holder.tv_wo_bldgNm.setText(tv_wo_bldgNm);
		holder.tv_wo_st.setText(tv_wo_st + " " + tv_wo_csFR);
		if(tv_wo_workNm.equals("정기점검")){
			holder.tv_wo_checkSt.setVisibility(View.VISIBLE);
			holder.tv_wo_checkSt.setText("점검상황 " + tv_wo_yCNT + "/"+ tv_wo_tCNT);
		}else{
			holder.tv_wo_checkSt.setVisibility(View.INVISIBLE);
		}
				
	}
}
