package com.jinsit.kmec.DM.DM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.jinsit.kmec.IP.IS.IP_IS02_R01P;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CV.CalendarGridAdapter;
import com.jinsit.kmec.comm.CV.DayInfo;
import com.jinsit.kmec.comm.CV.InspectionPlanHistoryData;
import com.jinsit.kmec.comm.CalendarGridNoDataActivity;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DM_DM05_R00 extends Activity {

    //uiInstances
    Context context;
    TextView tv_dm05_YM_value;
    TextView btn_dm05_calendar;
    TextView btn_dm05_search;
    GridView gl_dm05_calender;

    //arguments
    Map<String, String> argMap;

    //Http
    private EasyJsonList ejl;
    private EasyJsonList selectEjl;

    //utils
    private ProgressDialog progress;
    private CommonSession cs;

    //calender
    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;

    private List<String> myList;
    private ArrayList<DayInfo> mDayList;
    private Calendar mThisMonthCalendar;
    private DM_DM05_R00_Adapter01 mCalendarAdapter;
    private ArrayList<DM_DM05_R00_ITEM01> iphData;

    private String selectDt;
    String csEmpId;
    private boolean intentCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_dm05_r00);

        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("근태 내역조회");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);
        getInstances();
        manageCalConfig();
    }

    @Override
    public void onResume() {
        super.onResume();
        intentCheck = false;
        naviPref = new HomeNaviPreference(context);
        navigationInit();
    }

    private void getInstances() {

        context = this;
        cs = new CommonSession(context);
        csEmpId = cs.getEmpId();
        tv_dm05_YM_value = (TextView) findViewById(R.id.tv_dm05_YM_value);
        btn_dm05_calendar = (TextView) findViewById(R.id.btn_dm05_calendar);
        btn_dm05_search = (TextView) findViewById(R.id.btn_dm05_search);
        gl_dm05_calender = (GridView) findViewById(R.id.gl_dm05_calender);
        setEvents();
    }

    private void setEvents() {
        tv_dm05_YM_value.setOnClickListener(listner);
        btn_dm05_search.setOnClickListener(listner);
        btn_dm05_calendar.setOnClickListener(listner);
        setDefaultValues();
    }

    private void setDefaultValues() {
        tv_dm05_YM_value.setText(DateUtil.getYM());
        setConfig();
    }

    private void setConfig() {
        ActivityAdmin.getInstance().addActivity(this);
    }

    private void manageCalConfig() {
        mDayList = new ArrayList<DayInfo>();
        myList = new ArrayList<String>();
        gl_dm05_calender.setOnItemClickListener(itemListener);
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);

    }


    OnClickListener listner = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //달력
                case R.id.tv_dm05_YM_value:
                    startActivityForResult(new Intent(context, CalendarGridNoDataActivity.class), 99);
                    break;

                case R.id.btn_dm05_calendar:
                    startActivityForResult(new Intent(context, CalendarGridNoDataActivity.class), 99);
                    break;
                //조회
                case R.id.btn_dm05_search:
                    String strDate = tv_dm05_YM_value.getText().toString();
                    int year = Integer.parseInt(strDate.substring(0, 4));
                    int month = Integer.parseInt(strDate.substring(5, 7));

                    mThisMonthCalendar.set(year, month - 1, 1);
                    getCalendar(mThisMonthCalendar);
                    break;
            }
        }
    };


    OnItemClickListener itemListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
            selectDt = myList.get(position);
            if (intentCheck == false) {
                intentCheck = true;
                new dataTrans().execute("selectWorkDate");
            }
        }
    };

    private void getCalendar(Calendar calendar) {
        progress(true);
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;

        DayInfo day;

        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);
            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);

        }
        new selectWorkMonth().execute();
    }


    public class selectWorkMonth extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                iphData = new ArrayList<DM_DM05_R00_ITEM01>();

                GetHttp http = new GetHttp();
                String param_url = WebServerInfo.getUrl() + "dm/selectWorkMonth.do";

                String workMm = tv_dm05_YM_value.getText().toString();
//                csEmpId = "302081";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", csEmpId));
                arguments.add(new BasicNameValuePair("workMm", workMm));
                JSONObject returnJson = http.getPost(param_url, arguments, true);
                myList.clear();

                try {
                    ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));

                    iphData.clear();
                    int jsonSize = returnJson.getJSONArray("dataList").length();
                    for (int i = 0; i < jsonSize; i++) {
                        iphData.add(new DM_DM05_R00_ITEM01(
                                ejl.getValue(i, "WORK_DT"),
                                ejl.getValue(i, "WORK_DAY"),
                                ejl.getValue(i, "WEEKDAY"),
                                ejl.getValue(i, "DAY_NM1"),
                                ejl.getValue(i, "DAY_NM2"),
                                ejl.getValue(i, "DAY_NM3"),
                                ejl.getValue(i, "WORK_HR"),
                                ejl.getValue(i, "REP_NM")));

                        myList.add(ejl.getValue(i, "WORK_DT"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            initCalendarAdapter();
        }
    }

    private void initCalendarAdapter() {
        mCalendarAdapter = new DM_DM05_R00_Adapter01(context, R.layout.dm_dm05_r00_adapter01, mDayList, iphData);
        gl_dm05_calender.setAdapter(mCalendarAdapter);
        progress(false);
    }

    public class dataTrans extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GetHttp http = new GetHttp();
//            String param_url = WebServerInfo.getUrl()+"dm/"+params+".do";
//
//            List<NameValuePair> arguments = new ArrayList<NameValuePair>();
//            arguments.add(new BasicNameValuePair("csEmpId" , cs.getEmpId()));
//            arguments.add(new BasicNameValuePair("attendDt", selectDt));
//            JSONObject returnJson = http.getPost(param_url, arguments, true);
//            try {
//                selectEjl = new EasyJsonList(returnJson.getJSONArray("dataList"));
//                int jsonSize = returnJson.getJSONArray("dataList").length();
//
//            }catch (JSONException e){
//                e.printStackTrace();
//            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent intent = new Intent(context, DM_DM05_R01P.class);
            intent.putExtra("selectDt", selectDt);
            intent.putExtra("empId", csEmpId);
            startActivityForResult(intent, 98);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 99:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String strDate = bundle.getString("dateSelected");
                    tv_dm05_YM_value.setText(strDate.substring(0, 7));

                    int year = Integer.parseInt(strDate.substring(0, 4));
                    int month = Integer.parseInt(strDate.substring(5, 7));

                    mThisMonthCalendar.set(year, month - 1, 1);
                    getCalendar(mThisMonthCalendar);
                    break;
                }
                break;

            case 98:
                getCalendar(mThisMonthCalendar);
                break;
        }
    }


    //utils
    private void alert(String msg, Context context) {
        AlertView.showAlert(msg, context);
    }

    private void progress(Boolean shallActivated) {
        if (shallActivated) {
            DM_DM05_R00.this.progress =
                    ProgressDialog.show(context, "알림", "조회 중입니다.");
        } else {
            DM_DM05_R00.this.progress.dismiss();
        }
    }


    //navigation
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
            testnavi.setPadding(0, setPadding, 0, 0);
            testnavi.setAlpha((float) 0.5);
        } else {
            testnavi.setPadding(0, 0, 0, 0);
            testnavi.setAlpha((float) 1);
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

};
