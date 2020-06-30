package com.jinsit.kmec.comm.jinLib;


import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jinsit.kmec.R;


public class SignActivity extends Activity implements OnClickListener
{
	private Gesture gesture;
	private GestureOverlayView gestureOverayView;
	private Button buttonDone, buttonReinput;
	private TextView Tx_title;
	
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
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sign_gesture); // setContentView 가 반드시 이 위치에
												// 있어야 정상 동작함.
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.titlebar);
		
		buttonDone = (Button)findViewById(R.id.done);
		buttonReinput = (Button)findViewById(R.id.Btn_Reinput);
		buttonReinput.setOnClickListener(this);
		gestureOverayView = (GestureOverlayView)findViewById(R.id.gestures_overlay);
		gestureOverayView.addOnGestureListener(new SignGestureListener());
		
		
	}
	
	public void onBack(final View view){
		SignActivity.this.setResult(RESULT_CANCELED);
		SignActivity.this.finish();
	}
	
	public void saveGesture(final View v)
	{
		Intent intent =new Intent();
		try{
			String sign = SignUtil.encodeToBase64SignBmp(this.gesture);
			intent.putExtra("sign", sign);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		this.setResult(RESULT_OK,intent);
		this.finish();
	}
	
	public void cancelGesture(final View v){
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
			
			break;
		}
		
	}
}
