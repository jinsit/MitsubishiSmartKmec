package com.jinsit.kmec.IP.IS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
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

public class IP_IS01_R00 extends Activity {

    //uiInstances
    private Context context;
    private TextView et01_is_calendar;
    private TextView btn_is_calendar;
    private TextView btn02_is_selectData;
    private TextView tv02_is_totalFigures_v;
    private TextView tv03_is_figuresToBeCompleted_v;
    private TextView tv04_is_figuresToComplete_v;

    //Http
    private EasyJsonMap ejm;
    private EasyJsonList ejl01;
    private EasyJsonList ejl02;
    private EasyJsonList ejl03;
    private JSONObject returnJson;
    private List<IP_IS01_R00_Item01> itemList01;
    private List<IP_IS01_R00_Item02> itemList02;
    private ListAdapter adpater01;
    private ListAdapter adpater02;

    //ListView
    private ListView lv01_is_toBeCompletedJobs;
    private ListView lv02_is_allJobs;

    //utils
    private ProgressDialog progress;
    private CommonSession cs;

    //Update Date Parameter
    private View.OnClickListener updateClickListener;
    private Map<String, String> argMap = new HashMap<String, String>();
    private String jobNo;
    private String workDt;
    private String newDt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_is01_r00);
        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("점검계획/실적관리");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);
        getInstances();
        setEvents();
    }

    private void getInstances() {

        context = this;
        cs = new CommonSession(context);
        et01_is_calendar = (TextView) findViewById(R.id.et01_is_calendar);
        btn_is_calendar = (TextView) findViewById(R.id.btn_is_calendar);
        btn02_is_selectData = (TextView) findViewById(R.id.btn02_is_selectData);

        tv02_is_totalFigures_v = (TextView) findViewById(R.id.tv02_is_totalFigures_v);
        tv03_is_figuresToBeCompleted_v = (TextView) findViewById(R.id.tv03_is_figuresToBeCompleted_v);
        tv04_is_figuresToComplete_v = (TextView) findViewById(R.id.tv04_is_figuresToComplete_v);

        lv01_is_toBeCompletedJobs = (ListView) findViewById(R.id.lv01_is_toBeCompletedJobs);
        lv02_is_allJobs = (ListView) findViewById(R.id.lv02_is_allJobs);

        itemList01 = new ArrayList<IP_IS01_R00_Item01>();
        itemList02 = new ArrayList<IP_IS01_R00_Item02>();

        lv01_is_toBeCompletedJobs.setClickable(false);
        lv01_is_toBeCompletedJobs.setFocusable(false);
    }

    private void setEvents() {
        et01_is_calendar.setOnClickListener(clickListener);
        btn02_is_selectData.setOnClickListener(clickListener);
        btn_is_calendar.setOnClickListener(clickListener);
        updateClickListener = this.clickListener;
        setDefaultValues();
    }

    private void setDefaultValues() {
        et01_is_calendar.setText(DateUtil.getYM());
        setConfig();
    }

    private void setConfig() {
        ActivityAdmin.getInstance().addActivity(this);
    }

    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.et01_is_calendar:
                    startActivityForResult(new Intent(context, CalendarGridNoDataActivity.class), 99);
                    break;

                case R.id.btn_is_calendar:
                    startActivityForResult(new Intent(context, CalendarGridNoDataActivity.class), 99);
                    break;
                //조회
                case R.id.btn02_is_selectData:
                    IP_IS01_R00.this.progress =
                            android.app.ProgressDialog.show(context, "알림", "조회 중입니다...");
                    String rchYM = et01_is_calendar.getText().toString();
                    new SelectData().execute("header", rchYM);
                    new SelectData().execute("list01", rchYM);
                    new SelectData().execute("list02", rchYM);
                    break;
                case R.id.tv_is_adapter_updateDt:
                    TextView update = (TextView) v;
                    String job_no = v.getTag(R.id.tag_ip_is00_job_no).toString();
                    String work_dt = v.getTag(R.id.tag_ip_is00_work_dt).toString();
                    String bldg_nm = v.getTag(R.id.tag_ip_is00_bldg_nm).toString();
                    String user_id = v.getTag(R.id.tag_ip_is00_user_id).toString();

                    updateClick( job_no,  work_dt,  bldg_nm,  user_id);
            }
        }
    };


    private class SelectData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            GetHttp http = new GetHttp();
            String url = WebServerInfo.getUrl() + "ip/selectInspectionManagement.do";

            String csEmpId = cs.getEmpId();
            String workMm = et01_is_calendar.getText().toString();

            List<NameValuePair> arguments = new ArrayList<NameValuePair>();
            arguments.clear();
            arguments.add(new BasicNameValuePair("csEmpId", csEmpId));
            arguments.add(new BasicNameValuePair("workMm", workMm));


            //전체,완료,미처리 대수
            if (params[0].equals("header")) {
                arguments.add(new BasicNameValuePair("selTp", "1"));

                //미처리작업대상
            } else if (params[0].equals("list01")) {
                arguments.add(new BasicNameValuePair("selTp", "2"));

                //전체
            } else if (params[0].equals("list02")) {
                arguments.add(new BasicNameValuePair("selTp", "3"));

            }

            //Http
            returnJson = http.getPost(url, arguments, true);

            if (params[0].equals("header")) {
                try {
                    ejm = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //미처리작업대상
            } else if (params[0].equals("list01")) {
                try {
                    ejl01 = new EasyJsonList(returnJson.getJSONArray("dataList"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (params[0].equals("list02")) {
                try {
                    ejl02 = new EasyJsonList(returnJson.getJSONArray("dataList"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return params[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            //전체,완료,미처리 대수
            if (result.equals("header")) {
                try {

                    tv02_is_totalFigures_v.setText(ejm.getValue("T_CNT"));
                    tv03_is_figuresToBeCompleted_v.setText(ejm.getValue("E_CNT"));
                    tv04_is_figuresToComplete_v.setText(ejm.getValue("L_CNT"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //미처리작업대상
            } else if (result.equals("list01")) {
                setAdapter01();
                //전체
            } else if (result.equals("list02")) {
                setAdapter02();
            }

        }

    }//end of SelectData


    private void setAdapter01() {

        try {
            itemList01.clear();
            int jsonSize = returnJson.getJSONArray("dataList").length();
            for (int i = 0; i < jsonSize; i++) {
                itemList01.add(new IP_IS01_R00_Item01(ejl01.getValue(i, "WORK_DT")
                                , ejl01.getValue(i, "CAR_NO")
                                , ejl01.getValue(i, "BLDG_NM")
                                , ejl01.getValue(i, "JOB_NO")
                                , ejl01.getValue(i, "CS_EMP_ID")
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adpater01 = new IP_IS01_R00_Adapter01(context, R.layout.ip_is01_r00_adapter01, itemList01, updateClickListener);
        lv01_is_toBeCompletedJobs.setAdapter(adpater01);
    }


    private void setAdapter02() {
        try {
            itemList02.clear();
            int jsonSize = returnJson.getJSONArray("dataList").length();

            for (int i = 0; i < jsonSize; i++) {
                itemList02.add(new IP_IS01_R00_Item02(ejl02.getValue(i, "BLDG_NM")
                                , ejl02.getValue(i, "END_VAL")
                                , ejl02.getValue(i, "D_CNT")
                                , ejl02.getValue(i, "T_CNT")
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adpater02 = new IP_IS01_R00_Adapter02(context, R.layout.ip_is01_r00_adapter02, itemList02);
        lv02_is_allJobs.setAdapter(adpater02);


        lv02_is_allJobs.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, String> paraMap = new HashMap<String, String>();

                try {
                    paraMap.put("selTp", "1");
                    paraMap.put("csEmpId", ejl02.getValue(position, "CS_EMP_ID"));
                    paraMap.put("workMm", ejl02.getValue(position, "WORK_MM"));
                    paraMap.put("bldgNo", ejl02.getValue(position, "BLDG_NO"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                IP_IS01_R01P d01 = new IP_IS01_R01P(context, paraMap);
                d01.show();

            }
        });
        IP_IS01_R00.this.progress.dismiss();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 99:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String strDate = bundle.getString("dateSelected");
                    et01_is_calendar.setText(strDate.substring(0, 7));
                    break;
                }
            case 00:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    newDt = bundle.getString("dateSelected");

                    long now = System.currentTimeMillis();
                    Date today = null;
                    Date furture = null;
                    SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    try {
                        today = format.parse(cs.getWorkDt());
                        furture = format.parse(newDt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int compare = today.compareTo(furture);
                    if(compare > 0){
                        AlertView.showAlert("금일 이전 날짜로는 변경이 불가능합니다.", context);
                    }else{
                        updateAlert();          // 변경 알림 메세지
                    }
//                    if (furture.after(today)) {
//                        updateAlert();          // 변경 알림 메세지
//                    }else {
//                        AlertView.showAlert("금일 이후로 변경해야 합니다.", context);
//                    }
                    break;
                }
        }
    }

    public void updateClick(String job_no, String work_dt, String bldg_nm, String user_id) {
        context = this;
        try {
            argMap.clear();
            argMap.put("jobNo", job_no);
            argMap.put("workDt", work_dt);
            argMap.put("bldgNm", bldg_nm);
            argMap.put("userId", user_id);
            startActivityForResult(new Intent(context, CalendarGridNoDataActivity.class), 00);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UpdateDates extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl() + "ip/updateWorkRestart.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", argMap.get("userId")));                   // 점검자
                arguments.add(new BasicNameValuePair("workDt", newDt));                                  // 변경 날짜
                arguments.add(new BasicNameValuePair("workDtYet", argMap.get("workDt")));                   // 기존 미처리 날짜
                arguments.add(new BasicNameValuePair("jobNoYet", argMap.get("jobNo")));                    // 작업번호
                arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));                          // 작업자

                JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

                try {
                    ejl03 = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    int jsonSize = returnJson.getJSONArray("dataList").length();
                    for (int i = 0; i < jsonSize; i++) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            new SelectData().execute("list01", et01_is_calendar.getText().toString());       // ListView 재조회.
        }
    }

    private void updateAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("알림");
        alertDialogBuilder
                .setMessage(argMap.get("bldgNm") + "\n"
                        + "기존일자 : " + argMap.get("workDt") + "\n"
                        + "변경일자 : " + newDt + "\n" + "변경하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new UpdateDates().execute();        // 정기점검계획수정 - 일자변경.
                            }
                        })
                .setNegativeButton("아니오"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    private void alert(String msg, Context context) {
        AlertView.showAlert(msg, context);
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
