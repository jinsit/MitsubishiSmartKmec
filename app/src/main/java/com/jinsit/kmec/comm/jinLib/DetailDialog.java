package com.jinsit.kmec.comm.jinLib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SearchElevResData;
import com.jinsit.kmec.IR.NT.NoticeResponseData;

public class DetailDialog extends Dialog {
	Activity context;
	Object obj;
	NoticeResponseData noticeData;
	public DetailDialog(Context c, Object mData) {
		super(c);
		// TODO Auto-generated constructor stub
		context  = (Activity)c;
		obj = mData;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_detail);
		
		
		SearchElevResData noticeData = (SearchElevResData)obj;
			
		
	
		TextView tv_NoticeTime = (TextView) findViewById(R.id.tv_noticeTime);
		TextView tv_NoticeTo = (TextView) findViewById(R.id.tv_noticeTo);
		TextView tv_NoticeFrom = (TextView) findViewById(R.id.tv_noticeFrom);
		TextView tv_NoticeContent = (TextView) findViewById(R.id.tv_noticeContent);
	
		tv_NoticeTime.setText(noticeData.getBLDG_NO());
		tv_NoticeTo.setText(noticeData.getCAR_NO());
		tv_NoticeFrom.setText(noticeData.getDONG_CAR_NO());
		//tv_NoticeContent.setText(noticeData.getContents());
		// activityInitialize();

	}

	private void activityInitialize() {
		// TODO Auto-generated method stub

	}

}
