package com.jinsit.kmec.comm.jinLib;

import com.jinsit.kmec.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;

public class AlertView {
	public static void showError(String message, Context ctx)
	{
		showAlert("에러", message, ctx);
	}

	public static void showAlert(String message, Context ctx)
	{
		showAlert("알림", message, ctx);
	}

	public static void showAlert(String title, String message, Context ctx)
	{
		//Create a builder
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);//얼러트뷰 사용하는곳에선 취소안되게 확인버튼 눌러야만 취소 가능토록 변경
		//add buttons and listener
		EmptyListener pl = new EmptyListener();
		builder.setPositiveButton("확인", pl);
		//Create the dialog

		AlertDialog ad = builder.create();
		//show
		ad.show();
	}

	public static void showAlert( String message, Context ctx, OnDismissListener dismissListener)
	{
		//Create a builder
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("알림");
		builder.setMessage(message);
		//add buttons and listener
		EmptyListener pl = new EmptyListener();
		builder.setPositiveButton("확인", pl);
		//Create the dialog

		AlertDialog ad = builder.create();
		//show
		ad.setOnDismissListener(dismissListener);
		ad.show();
	}

	public static void alert( Context context
			, String title, String message
			, OnClickListener ocl){

		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title.isEmpty() ? "알림" : title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("확인", ocl);

		AlertDialog alertDialog;
		alertDialog = builder.create();
		alertDialog.show();

	}
	public static void confirm( Context context
			, String title, String message
			, OnClickListener ocl01){

		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title.isEmpty() ? "알림" : title);
		builder.setMessage(message);
		builder.setCancelable(false);
		EmptyListener pl = new EmptyListener();
		builder.setPositiveButton("예", ocl01);
		builder.setNegativeButton("아니오", pl);

		AlertDialog alertDialog;
		alertDialog = builder.create();
		alertDialog.show();
	}

	public static void deleteConfirm( Context context
			, String title, String message
			, OnClickListener yesEvent
	){
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		EmptyListener pl = new EmptyListener();
		ad.setTitle(title);
		ad.setMessage(message);
		ad.setCancelable(false);
		//ad.setIcon(R.drawable.delete);
		ad.setPositiveButton("예", yesEvent);
		ad.setNegativeButton("아니오", pl);
		ad.show();
	}

	public static void confirmYN(  Context context
			, String title, String message
			, OnClickListener yesEvent
			, OnClickListener noEvent
	){

		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(message);
		ad.setCancelable(false);
		ad.setPositiveButton("예", yesEvent);
		ad.setNegativeButton("아니오", noEvent);
		ad.show();
	}
}


class EmptyListener implements android.content.DialogInterface.OnClickListener {
	public void onClick(DialogInterface arg0, int arg1) {
		arg0.dismiss();
	}
}
