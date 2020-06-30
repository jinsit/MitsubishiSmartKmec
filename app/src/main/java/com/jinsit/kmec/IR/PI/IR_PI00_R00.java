package com.jinsit.kmec.IR.PI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_PI00_R00 extends Activity implements OnClickListener{

	private TextView btn_pi_partsClaimCondition;
	private TextView btn_pi_partsDeliveryCondition;
	private TextView btn_pi_partsStockCondition;
	private TextView btn_pi_partsPriceCondition;
	private TextView btn_pi_partsInfoCondition;
	private TextView btn_pi_partsRequest;

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi00_r00);
		activityInit();
	}

	private void activityInit() {
		context= this;
		btn_pi_partsClaimCondition = (TextView)findViewById(R.id.btn_pi_partsClaimCondition);
		btn_pi_partsClaimCondition.setOnClickListener(this);

		btn_pi_partsDeliveryCondition = (TextView)findViewById(R.id.btn_pi_partsDeliveryCondition);
		btn_pi_partsDeliveryCondition.setOnClickListener(this);

		btn_pi_partsStockCondition = (TextView)findViewById(R.id.btn_pi_partsStockCondition);
		btn_pi_partsStockCondition.setOnClickListener(this);

		btn_pi_partsPriceCondition = (TextView)findViewById(R.id.btn_pi_partsPriceCondition);
		btn_pi_partsPriceCondition.setOnClickListener(this);

		btn_pi_partsInfoCondition = (TextView)findViewById(R.id.btn_pi_partsInfoCondition);
		btn_pi_partsInfoCondition.setOnClickListener(this);

		btn_pi_partsRequest = (TextView)findViewById(R.id.btn_pi_partsRequest);
		btn_pi_partsRequest.setOnClickListener(this);
		//타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("부품정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
		//ActivityAdmin.setActMovementSys(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_pi_partsClaimCondition:
				intent = new Intent(this, IR_PI01_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_pi_partsDeliveryCondition:
				intent = new Intent(this, IR_PI02_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_pi_partsStockCondition:
				intent = new Intent(this, IR_PI03_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_pi_partsPriceCondition:
				intent = new Intent(this, IR_PI04_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_pi_partsInfoCondition:
				intent = new Intent(this, IR_PI05_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_pi_partsRequest:
				intent = new Intent(this, IR_PI06_R00.class);
				startActivity(intent);
				break;

			default:
				break;
		}
	}

	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;

	private void setToggleNavi(){
		boolean isHide = naviPref.isHide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int totalHeight = ScreenUtil.getRealScreenHeight(this);;
		int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
		int viewArea = naviHeight/6;
		int setPadding = totalHeight-viewArea-naviHeight;
		if(isHide){
			testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
		}else{
			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
		}
	}

	private void navigationInit(){
		testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
		homeNavi = new HomeNavigation(context, null);
		homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		homeNavi.setToggleNavi();
		Button navi = (Button) homeNavi.getBtn_naviHOME();
		navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				naviPref.setHide(!naviPref.isHide());
				homeNavi.setToggleNavi();

			}
		});
	}
	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
}
