package com.jinsit.kmec.IP.IS;

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


public class IP_IS00_R00 extends Activity {

    private Context context;
    private TextView tv01_is_ManageInspectionPlan;
    private TextView tv02_is_searchInspectionPlan;
    private TextView tv03_is_makeInspectionPlan;
    private TextView tv04_is_confirmInfoTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_is00_r00);
        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("점검스케줄");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);
        getInstances();
    }

    private void getInstances() {
        context = this;
        tv01_is_ManageInspectionPlan = (TextView) findViewById(R.id.tv01_is_ManageInspectionPlan);
        tv02_is_searchInspectionPlan = (TextView) findViewById(R.id.tv02_is_searchInspectionPlan);
        tv03_is_makeInspectionPlan = (TextView) findViewById(R.id.tv03_is_makeInspectionPlan);
        tv04_is_confirmInfoTrans = (TextView) findViewById(R.id.tv04_is_confirmInfoTrans);
        setEvents();
    }

    private void setEvents() {
        tv01_is_ManageInspectionPlan.setOnClickListener(clickListener);
        tv02_is_searchInspectionPlan.setOnClickListener(clickListener);
        tv03_is_makeInspectionPlan.setOnClickListener(clickListener);
        tv04_is_confirmInfoTrans.setOnClickListener(clickListener);
        setConfig();
    }

    private void setConfig() {
        ActivityAdmin.getInstance().finishLastMenuActivites();
        ActivityAdmin.getInstance().addMenuActivity(this);
        ActivityAdmin.getInstance().addActivity(this);
    }


    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv01_is_ManageInspectionPlan:
                    startActivity(new Intent(context, IP_IS01_R00.class));
                    break;
                case R.id.tv02_is_searchInspectionPlan:
                    startActivity(new Intent(context, IP_IS02_R00.class));
                    break;
                case R.id.tv03_is_makeInspectionPlan:
                    startActivity(new Intent(context, IP_IS03_R00.class));
                    break;
                case R.id.tv04_is_confirmInfoTrans:
                    startActivity(new Intent(context, IP_IS04_R00.class));
                    break;
            }
        }
    };
    private HomeNavigation homeNavi;
    private FrameLayout testnavi;
    boolean isHide;
    private HomeNaviPreference naviPref;

    private void setToggleNavi() {
        boolean isHide = naviPref.isHide();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int totalHeight = ScreenUtil.getRealScreenHeight(this);
        ;
        int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
        int viewArea = naviHeight / 6;
        int setPadding = totalHeight - viewArea - naviHeight;
        if (isHide) {
            testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
        } else {
            testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
        }
    }

    private void navigationInit() {
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
    public void onResume() {
        super.onResume();
        naviPref = new HomeNaviPreference(context);
        navigationInit();
    }
};