package com.jinsit.kmec.WO.WT.RI;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ImageResize;


public class SimpleYesNoDialog  extends AlertDialog{
	
	public btnClickListener yesClickListener;
	public btnClickListener noClickListener;
	public OnItemClickListener listener ;
	String msg;
	private String yesMsg;
	private String noMsg;
	ListView lv_dialogList;
	ListAdapter ladpater =null;
	Button btn_dialogBtn_n , btn_dialogBtn_y;
	boolean isSelector = false;
	
	public interface btnClickListener{
		void onButtonClick();
	}
	
	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		super.setMessage(message);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comm_yn_dialog);
	
		TextView message = (TextView)findViewById(R.id.tv_dialogMessage);
		btn_dialogBtn_n = (Button)findViewById(R.id.btn_dialogBtn_n);
		btn_dialogBtn_y = (Button)findViewById(R.id.btn_dialogBtn_y);
		if(message!=null)message.setText(msg);
		if(yesMsg != null){
			btn_dialogBtn_y.setText(yesMsg);
		}
		if(noMsg != null){
			btn_dialogBtn_n.setText(noMsg);
		}

		btn_dialogBtn_n.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(noClickListener!=null)noClickListener.onButtonClick();
				cancel();
			}
			
		});
		
		btn_dialogBtn_y.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(yesClickListener!=null)yesClickListener.onButtonClick();
				cancel();
			}
			
		});
	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		super.setView(view);
	}

	public SimpleYesNoDialog(Context context, String message, btnClickListener cancelListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.yesClickListener = cancelListener;
		this.msg = message;
		
	}

	public SimpleYesNoDialog(Context context, String message, btnClickListener yesClickListener, btnClickListener noClickListener, String yesBtnMsg, String noBtnMsg) {
		super(context);
		// TODO Auto-generated constructor stub
		this.yesClickListener = yesClickListener;
		this.noClickListener = noClickListener;
		this.yesMsg = yesBtnMsg;
		this.noMsg = noBtnMsg;
		this.msg = message;

	}
	

	

}
