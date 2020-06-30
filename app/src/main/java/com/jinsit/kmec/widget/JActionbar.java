package com.jinsit.kmec.widget;


import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.PreferenceUtil;

public class JActionbar {

	public interface OnOrderByClickListener{
		void onOrderByClick(boolean isAsc);
	}

	private OnOrderByClickListener onOrderByClickListener;

	public static void setActionBar(Context context, ActionBar aBar){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View mCustomView = mInflater.inflate(R.layout.jactionbar,null);
		//TextView titleTextView = (TextView)mCustomView.findViewById(R.id.titleTextView);
		TextView versionTextView = (TextView)mCustomView.findViewById(R.id.versionTextView);
		TextView sortTextView = (TextView)mCustomView.findViewById(R.id.sortTextView);
		sortTextView.setVisibility(View.GONE);
		CheckBox checkBox = (CheckBox)mCustomView.findViewById(R.id.sortCheckBox);
		checkBox.setVisibility(View.GONE);
		versionTextView.setText(ApplicationInfo.getVersionLogin(context));
		ActionBar.LayoutParams params = 
				new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.RIGHT);
		aBar.setCustomView(mCustomView,params);
		aBar.setDisplayShowCustomEnabled(true);
	}

	public void setActionBarSort(final Context context, ActionBar aBar, String sort, OnOrderByClickListener listener){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View mCustomView = mInflater.inflate(R.layout.jactionbar,null);
		TextView versionTextView = (TextView)mCustomView.findViewById(R.id.versionTextView);
		onOrderByClickListener = listener;
		TextView sortTextView = (TextView)mCustomView.findViewById(R.id.sortTextView);
		sortTextView.setText(sort);
		CheckBox checkBox = (CheckBox)mCustomView.findViewById(R.id.sortCheckBox);
		checkBox.setChecked(PreferenceUtil.instance(context).getOrderByASC());
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceUtil.instance(context).putOrderByASC(isChecked);
				if(onOrderByClickListener != null)
				onOrderByClickListener.onOrderByClick(isChecked);
			}
		});

		versionTextView.setText(ApplicationInfo.getVersionLogin(context));
		ActionBar.LayoutParams params =
				new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.RIGHT);
		aBar.setCustomView(mCustomView,params);
		aBar.setDisplayShowCustomEnabled(true);
	}

}
