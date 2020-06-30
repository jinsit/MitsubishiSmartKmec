package com.jinsit.kmec.DM.DM;

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
import com.jinsit.kmec.CM.CM_SelectPermissionCheck_StartActivity;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class DM_DM00_R00 extends Activity implements OnClickListener{

	private TextView btn_dm_registerOt;
	private TextView btn_dm_registerAttends;
	private TextView btn_dm_approveOt;
	private TextView btn_dm_registerSelect;
	private TextView btn_dm_registerManagement;
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm00_r00);
		activityInit();
	}


	private void activityInit() {
		context = this;
		// 근태현황, 근태등록으로 인하여 btn_dm_registerOt, btn_dm_registerAttends, btn_dm_approveOt 사용안함. 2019-04-22 이창헌
		btn_dm_registerOt = (TextView)findViewById(R.id.btn_dm_registerOt);
		btn_dm_registerOt.setOnClickListener(this);
		btn_dm_registerOt.setVisibility(View.GONE);

		btn_dm_registerAttends = (TextView)findViewById(R.id.btn_dm_registerAttends);
		btn_dm_registerAttends.setOnClickListener(this);
		btn_dm_registerAttends.setVisibility(View.GONE);

		btn_dm_approveOt = (TextView)findViewById(R.id.btn_dm_approveOt);
		btn_dm_approveOt.setOnClickListener(this);
		btn_dm_approveOt.setVisibility(View.GONE);

		btn_dm_registerSelect = (TextView)findViewById(R.id.btn_dm_registerSelect);
		btn_dm_registerSelect.setOnClickListener(this);

		btn_dm_registerManagement = (TextView)findViewById(R.id.btn_dm_registerManagement);
		btn_dm_registerManagement.setOnClickListener(this);
		//타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("근태관리");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_dm_registerOt:
				intent = new Intent(this, DM_DM01_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_dm_registerAttends:
				intent = new Intent(this, DM_DM02_R01.class);
				startActivity(intent);
				break;
			case R.id.btn_dm_approveOt:

				CM_SelectPermissionCheck_StartActivity pCheck= new CM_SelectPermissionCheck_StartActivity(context);
				String ret = pCheck.permissionCheck("DM_DM03_R00");
				if(ret.equals("1")){
					//권한있음
					intent = new Intent(this, DM_DM03_R00.class);
					startActivity(intent);
				}else if(ret.equals("0")){
					//권한없음
					SimpleDialog sm01 = new SimpleDialog(context, "알림","접근권한이 없습니다.");
					sm01.show();
				}else{
					//네트워크에러
					SimpleDialog sm01 = new SimpleDialog(context, "알림","네트워크 연결 에러입니다. WIFI나 LTE를 확인하시고 다시 시도하십시오.");
					sm01.show();
				}
				break;

			case R.id.btn_dm_registerSelect:
				intent = new Intent(this, DM_DM04_R00.class);
				startActivity(intent);
				break;
			case R.id.btn_dm_registerManagement:
				intent = new Intent(this, DM_DM05_R00.class);
				startActivity(intent);
				break;
			/*intent = new Intent(this, DM_DM03_R00.class);
			startActivity(intent);*/
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