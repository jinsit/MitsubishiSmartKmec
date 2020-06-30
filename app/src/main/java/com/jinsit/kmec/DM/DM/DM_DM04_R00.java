package com.jinsit.kmec.DM.DM;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.DatePicker01;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.res.Resources.getSystem;

public class DM_DM04_R00 extends Activity implements OnClickListener,
        OnItemClickListener {

    private Context context;

    private TextView tv_dm04_year;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private Spinner dm_dm_spinner;
    private TextView btn_dm04_select;
    private ListView lv_dm_selectList;

    private ProgressDialog progress;
    private CommonSession commonSession;


    private EasyJsonList ejl01, ejl02;

    private OnClickListener onClickListener;
    private List<DM_DM04_R00_ITEM01> itemList01;
    private List<DM_DM04_R00_ITEM02> itemList02;
    private DM_DM04_R00_Adapter02 adapter02;

    private List<String> array_spinner_nm;          // 주차명 List
    private String selected_week_dt;                // 선택한 주차 값
    private boolean popup_show = false;                     // 개인 근태현황 팝업 출력 여부.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_dm04_r00);
        activityInit();
    }


    protected void activityInit() {
        getInstances();
        context = this;
        commonSession = new CommonSession(context);

        //타이틀 바
        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("근태 현황");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);
        itemList01 = new ArrayList<DM_DM04_R00_ITEM01>();
        itemList02 = new ArrayList<DM_DM04_R00_ITEM02>();
        array_spinner_nm = new ArrayList<>();
    }

    protected void getInstances() {
        tv_dm04_year = (TextView) findViewById(R.id.tv_dm04_year);
        lv_dm_selectList = (ListView) findViewById(R.id.lv_dm_selectList);
        btn_dm04_select = (TextView) findViewById(R.id.btn_dm04_select);
        dm_dm_spinner = (Spinner) findViewById(R.id.dm_dm_spinner);
        setEvents();
    }

    protected void setEvents() {
        lv_dm_selectList.setOnItemClickListener(this);
        btn_dm04_select.setOnClickListener(this);
        setConfig();

        yearClick();        // DatePicker 년도만 표시.
    }

    private void setConfig() {
        ActivityAdmin.getInstance().addActivity(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        if (position > -1 && popup_show == false) {
            popup_show = true;
            final DM_DM04_R01P dm04_popup = new DM_DM04_R01P(context, selected_week_dt, itemList02.get(position).EMP_ID, itemList02.get(position).EMP_NM);
            dm04_popup.show();
            dm04_popup.requestPopup();
            dm04_popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    popup_show = false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dm04_year:
                yearClick();
                break;
            case R.id.btn_dm04_select:
                if (selected_week_dt.equals("")) {
                    AlertView.showAlert("주차를 선택 해주세요.", context);
                    return;
                }
                progress(true);
                new selectWeekWork().execute("basicWorkTime");
                break;
            default:
                break;
        }
    }

    public class selectFewWeek extends AsyncTask<String, Integer, String> {
        GetHttp http = new GetHttp();
        JSONObject returnJson01;

        @Override
        protected String doInBackground(String... params) {
            // 1. basicWorkTime
            if (params[0].equals("basicWorkTime")) {
                try {
                    String param_url_01 = WebServerInfo.getUrl()
                            + "dm/selectFewWeek.do";

                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("workYYYY", tv_dm04_year.getText().toString()));

                    returnJson01 = http.getPost(param_url_01, arguments, true);

                    try {
                        ejl01 = new EasyJsonList(
                                returnJson01.getJSONArray("dataList"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params[0];
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int cur_position = 0;

            // 1. basicWorkTime
            if (result.equals("basicWorkTime")) {

                try {
                    itemList01.clear();
                    array_spinner_nm.clear();
                    selected_week_dt = "";

                    int jsonSize = returnJson01.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        itemList01.add(new DM_DM04_R00_ITEM01(
                                ejl01.getValue(i, "YEAR_WEEK_DT"),
                                ejl01.getValue(i, "YEAR_WEEK_NM"),
                                ejl01.getValue(i, "CUR_DATE")));
                        array_spinner_nm.add(ejl01.getValue(i, "YEAR_WEEK_NM"));
                        if (!ejl01.getValue(i, "CUR_DATE").equals("")) {
                            cur_position = i;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList01.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, array_spinner_nm);
                dm_dm_spinner.setAdapter(adapter);
                dm_dm_spinner.setSelection(cur_position);
                dm_dm_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selected_week_dt = itemList01.get(position).getYEAR_WEEK_DT();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
//                progress(false);
            }
        }
    }


    public class selectWeekWork extends AsyncTask<String, Integer, String> {
        GetHttp http = new GetHttp();
        JSONObject returnJson02;

        @Override
        protected String doInBackground(String... params) {
            // 1. basicWorkTime
            if (params[0].equals("basicWorkTime")) {
                try {
                    String param_url_02 = WebServerInfo.getUrl()
                            + "dm/selectWeekWork.do";

                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("workDtFrTo", selected_week_dt));
                    arguments.add(new BasicNameValuePair("empNo", commonSession.getEmpId()));
//                    arguments.add(new BasicNameValuePair("empNo", "301640"));

                    returnJson02 = http.getPost(param_url_02, arguments, true);

                    try {
                        ejl02 = new EasyJsonList(
                                returnJson02.getJSONArray("dataList"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params[0];
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 1. basicWorkTime
            if (result.equals("basicWorkTime")) {

                try {
                    itemList02.clear();
//					AlertView.showAlert("TEST", context);

                    int jsonSize = returnJson02.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        itemList02.add(new DM_DM04_R00_ITEM02(
                                ejl02.getValue(i, "EMP_ID"),
                                ejl02.getValue(i, "EMP_NM"),
                                ejl02.getValue(i, "WEEK_WORK_HR_TOT_1"),
                                ejl02.getValue(i, "WEEK_WORK_ACH_1"),
                                ejl02.getValue(i, "WEEK_WORK_ACH_LAW_1"),
                                ejl02.getValue(i, "WEEK_WORK_HR_TOT_2"),
                                ejl02.getValue(i, "WEEK_WORK_ACH_2"),
                                ejl02.getValue(i, "WEEK_WORK_ACH_LAW_2")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList02.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                }

                adapter02 = new DM_DM04_R00_Adapter02(context,
                        R.layout.dm_dm04_r00_adapter, itemList02, onClickListener);
                DM_DM04_R00.this.lv_dm_selectList.setAdapter(adapter02);
                progress(false);
            }
        }
    }


    private void progress(Boolean isActivated) {
        if (isActivated) {
            DM_DM04_R00.this.progress = ProgressDialog.show(
                    context, "알림", "조회 중입니다.");
        } else {
            DM_DM04_R00.this.progress.dismiss();
        }
    }

    private void yearClick() {
        // 현재시간의 년도를 Default로 설정.
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat nowFormat = new SimpleDateFormat("yyyy");
        String formatYear = nowFormat.format(date);
        tv_dm04_year.setText(formatYear);
        // 주차 가져오는 AsyncTask
        new selectFewWeek().execute("basicWorkTime");
        // 년도의 Datepicker
        tv_dm04_year.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Dialog_MinWidth, mDataSetListener, Integer.parseInt(tv_dm04_year.getText().toString()), 1, 1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                yeerPicker(dialog); //년도만 표시.
                dialog.show();
            }
        });
        // 년도 변경 시 TextView 변경 후 주차 다시 가져오기.
        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = String.valueOf(year);
                tv_dm04_year.setText(date);
                new selectFewWeek().execute("basicWorkTime");
            }
        };
    }

    private void yeerPicker(DatePickerDialog dialog) {
        try {
            Field[] datePickerDialogFields = DatePickerDialog.class
                    .getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dialog);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            int daySpinnerId = Resources.getSystem()
                                    .getIdentifier("day", "id", "android");
                            if (daySpinnerId != 0) {
                                View daySpinner = datePicker.findViewById(daySpinnerId);
                                if (daySpinner != null) {
                                    daySpinner.setVisibility(View.GONE);
                                }
                            }

                            int MonthSpinnerId = Resources.getSystem()
                                    .getIdentifier("month", "id", "android");
                            if (MonthSpinnerId != 0) {
                                View monthSpinner = datePicker
                                        .findViewById(MonthSpinnerId);
                                if (monthSpinner != null) {
                                    monthSpinner.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if ("mDayPicker".equals(datePickerField.getName())
                                    || "mDaySpinner".equals(datePickerField.getName())
                                    || "mMonthPicker".equals(datePickerDialogField.getName())
                                    || "mMonthSpinner".equals(datePickerDialogField.getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
        }
    }

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

    @Override
    public void onResume() {
        super.onResume();
        naviPref = new HomeNaviPreference(context);
        navigationInit();
    }

}


	
	
