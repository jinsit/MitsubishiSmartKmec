package com.jinsit.kmec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jinsit.kmec.GK.LI.GK_LI00_R00;
import com.jinsit.kmec.HM.MP.HM_MP00_R00;
import com.jinsit.kmec.comm.CommonSession;


public class Intro extends Activity {

	private CommonSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        session = new CommonSession(this);
        initialize();
    }
    
	private void initialize() {
		Handler startHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(session.isLoggined()){
					finish();
					startActivity(new Intent(Intro.this,
							GK_LI00_R00.class));
					startActivity(new Intent(Intro.this,
							HM_MP00_R00.class));
				}else if(!session.isLoggined()){
					finish();
					startActivity(new Intent(Intro.this,
							GK_LI00_R00.class));
				}
			};
		};
		startHandler.sendEmptyMessageDelayed(0, 2000); // 2초후 시작
	}
};