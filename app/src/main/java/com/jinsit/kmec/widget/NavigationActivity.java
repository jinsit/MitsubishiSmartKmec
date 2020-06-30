package com.jinsit.kmec.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
public class NavigationActivity extends FragmentActivity implements OnClickListener{
    Context context;
    private TextView navigationEmptyTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        this.context = this;
        navigationEmptyTextView = (TextView)findViewById(R.id.navigationEmptyTextView);
        navigationEmptyTextView.setOnClickListener(this);
    }



    private HomeNavigation homeNavi;
    private FrameLayout testnavi;
    private HomeNaviPreference naviPref;

    private void setToggleNavi() {
        boolean isHide = naviPref.isHide();

        if(isHide){
            homeNavi.setVisibility(View.GONE);
        }else{
            homeNavi.setVisibility(View.VISIBLE);
        }

        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int totalHeight = ScreenUtil.getRealScreenHeight(this);
        int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
        int viewArea = naviHeight / 6;
        int setPadding = totalHeight - viewArea - naviHeight;
        if (isHide) {
            testnavi.setPadding(0, setPadding, 0, 0);
            testnavi.setAlpha((float) 0.5);
        } else {
            testnavi.setPadding(0, 0, 0, 0);
            testnavi.setAlpha((float) 1);
        }*/
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigationEmptyTextView:
                naviPref.setHide(!naviPref.isHide());
                homeNavi.setToggleNavi();
                break;

        }
    }
}