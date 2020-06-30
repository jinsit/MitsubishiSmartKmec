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
 * LocalAdminInfo_Adapter
 * 
 * @discription 관리자 정보 로칼
 * @author 원성민
 *
 */
public class LocalAdminInfo_Adapter extends SimpleCursorAdapter {
	private final Context mContext;
	private final int mLayout;
	public Cursor mCursor;
	private final LayoutInflater mLayoutInflater;
	public ViewHolder holder;

	private class ViewHolder {

		public TextView tv_adminName, tv_adminSP,tv_adminEmail,tv_adminCall;
	}

	public LocalAdminInfo_Adapter(Context context, int layout, Cursor c,
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
		holder.tv_adminName = (TextView)view.findViewById(R.id.tv_adminName);
		holder.tv_adminSP = (TextView)view.findViewById(R.id.tv_adminSP);
		holder.tv_adminEmail = (TextView)view.findViewById(R.id.tv_adminEmail);
		holder.tv_adminCall = (TextView)view.findViewById(R.id.tv_adminCall);
		view.setTag(holder);

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		mCursor = cursor;
		holder = (ViewHolder) view.getTag();
		final int position = mCursor.getPosition();
		String clientNm = cursor.getString(cursor
				.getColumnIndex("CLIENT_NM"));
		String mobile = cursor.getString(cursor
				.getColumnIndex("MOBILE"));
		String mailAddr = cursor.getString(cursor
				.getColumnIndex("MAIL_ADDR"));
		String phone = cursor.getString(cursor
				.getColumnIndex("PHONE"));
				
		holder.tv_adminName.setText(clientNm);
		holder.tv_adminSP.setText((mobile));
		holder.tv_adminEmail.setText(mailAddr);
		holder.tv_adminCall.setText((phone));
	}
}
