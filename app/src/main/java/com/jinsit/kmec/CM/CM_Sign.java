package com.jinsit.kmec.CM;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.SignUtil;


public class CM_Sign extends AlertDialog implements OnClickListener,OnDismissListener
{
	public CM_Sign(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private Context context;

	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private Gesture gesture;
	private GestureOverlayView gestureOverayView;
	private TextView buttonDone, buttonReinput;

	private class SignGestureListener implements GestureOverlayView.OnGestureListener
	{
		private static final float LENGTH_THRESHOLD = 120.0f;

		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
			// TODO Auto-generated method stub

		}

		public void onGestureCancelled(GestureOverlayView overlay,
									   MotionEvent event) {
			// TODO Auto-generated method stub

		}
		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			gesture = overlay.getGesture();
			if(gesture.getLength() < LENGTH_THRESHOLD)
			{
				overlay.clear(false);
			}else{
				buttonDone.setEnabled(true);
				//buttonDone.setBackgroundResource(R.drawable.selector_btn_receipt);
			}
		}

		public void onGestureStarted(GestureOverlayView overlay,
									 MotionEvent event) {
			buttonDone.setEnabled(false);
			//buttonDone.setBackgroundResource(R.drawable.btn_reinput_c);
			gesture = null;
		}



	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cm_sign); // setContentView 가 반드시 이 위치에
		// 있어야 정상 동작함.


		buttonDone = (TextView)findViewById(R.id.done);
		buttonDone.setOnClickListener(this);
		buttonReinput = (TextView)findViewById(R.id.Btn_Reinput);
		buttonReinput.setOnClickListener(this);
		gestureOverayView = (GestureOverlayView)findViewById(R.id.gestures_overlay);
		gestureOverayView.addOnGestureListener(new SignGestureListener());

		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);

		tv01_popTitle.setText("고객 서명");
		signString = "";

	}


	public void saveGesture(final View v)
	{
		try{
			signString = SignUtil.encodeToBase64SignBmp(this.gesture);
		}
		catch(Exception ex){
			ex.printStackTrace();
			signString = "";
		}
		dismiss();
	}

	public void cancelGesture(final View v){
		signString = "";
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.Btn_Reinput:
				gestureOverayView.clear(true);
				buttonDone.setEnabled(false);
				//buttonDone.setBackgroundResource(R.drawable.btn_reinput_c);
				gesture = null;
				signString = "";
				break;
			case R.id.btn_popClose:
				gesture = null;
				signString = "";
				dismiss();
				break;
			case R.id.done:
				saveGesture(arg0);
				break;

		}

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
	}



	private String signString;

	public String getSignString(){
		return signString;
	}
}
