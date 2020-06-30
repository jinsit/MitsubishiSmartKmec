package com.jinsit.kmec.DM.DM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.trxn.TrxnDocument;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DM_DM05_R01P extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ProgressDialog progress;
    private Context context;

    private String empId;
    private String selectDt;
    private String status_cd;
    private String office_chk;
    private String work_tm_nm, base_limit_mi, base_limit_mi_10, base_limit_mi_20;
    private String rep_st;                              // 상신구분자 0:계획, 1:상신, 9:거절
    private boolean timeWorkModifyYn;                   // 시간조정 수정 또는 추가 체크.

    private EasyJsonList ejl01, ejl_sp_status, ejl_watchkeeping, ejl_matser, ejl_detail_day, ejl_detail_night, ejl_delete;
    private DM_DM05_R01P_Adapter01 adapter01;
    private DM_DM05_R01P_Adapter02 adapter02;
    private ArrayList<DM_DM05_R01P_ITEM01> itemList01;
    private ArrayList<DM_DM05_R01P_ITEM02> itemList02;
    private ArrayList<DM_DM05_R01P_ITEM03> itemList03;

    private ArrayList<String> array_sp_status_cd;          // 근무 상태코드
    private ArrayList<String> array_sp_status_nm;          // 근무 상태명

    private TextView tv_dm05_workDt_v;
    private Spinner sp_dm05_isAttended_v;
    private TextView tv_dm05_totalWorkDt_v;
    private ListView lv_dm05_workList;

    private TextView tv_dm05_tab1;
    private TextView tv_dm05_tab2;
    private TextView btn_dm05_troubleshooting;
    private TextView btn_dm05_earlywork;
    private TextView btn_dm05_workSave;
    private TextView btn_dm05_timeSave;
    private TextView btn_dm05_timeSend;
    private TextView btn_dm05_r01p_adapter_delete;
    private TextView tv_dm05_r01p_adapter_workTime_fr;
    private TextView tv_dm05_r01p_adapter_workTime_to;
    private TextView tv_dm05_errmsg;

    public int clickItemPosition;
    public int selectTimePosition;

    private View.OnClickListener onClickListener;

    private CheckBox check_dm05_day;
    private CheckBox check_dm05_night;
    private CheckBox check_dm05_daySix;
    private CheckBox check_dm05_homecall;

    private CheckBox check_dm05_r01p_adapter_select;

    private LinearLayout lin_dm_check;
    private LinearLayout lin_dm_button;
    private LinearLayout lin_dm_workSave;
    private LinearLayout lin_dm_timeSave;

    private static int TAB_SELECTWORK = 1;
    private static int TAB_WORKTIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_dm05_r01p);
        activityInit();

        new selectWorkMaster().execute();
        btnSelect(TAB_SELECTWORK);
    }

    protected void activityInit() {
        context = this;
        selectDt = getIntent().getExtras().getString("selectDt");
        empId = getIntent().getExtras().getString("empId");

        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("근태 관리");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);

        itemList01 = new ArrayList<DM_DM05_R01P_ITEM01>();
        itemList02 = new ArrayList<DM_DM05_R01P_ITEM02>();
        itemList03 = new ArrayList<DM_DM05_R01P_ITEM03>();
        array_sp_status_cd = new ArrayList<>();
        array_sp_status_nm = new ArrayList<>();
        onClickListener = this;
        getInstances();
    }

    private void getInstances() {
        tv_dm05_workDt_v = (TextView) findViewById(R.id.tv_dm05_workDt_v);
        sp_dm05_isAttended_v = (Spinner) findViewById(R.id.sp_dm05_isAttended_v);
        tv_dm05_totalWorkDt_v = (TextView) findViewById(R.id.tv_dm05_totalWorkDt_v);
        lv_dm05_workList = (ListView) findViewById(R.id.lv_dm05_workList);
        tv_dm05_r01p_adapter_workTime_fr = (TextView) findViewById(R.id.tv_dm05_r01p_adapter_workTime_fr);
        tv_dm05_r01p_adapter_workTime_to = (TextView) findViewById(R.id.tv_dm05_r01p_adapter_workTime_to);
        tv_dm05_errmsg = (TextView) findViewById(R.id.tv_dm05_errmsg);

        tv_dm05_tab1 = (TextView) findViewById(R.id.tv_dm05_tab1);
        tv_dm05_tab2 = (TextView) findViewById(R.id.tv_dm05_tab2);
        btn_dm05_troubleshooting = (TextView) findViewById(R.id.btn_dm05_troubleshooting);
        btn_dm05_earlywork = (TextView) findViewById(R.id.btn_dm05_earlywork);
        btn_dm05_workSave = (TextView) findViewById(R.id.btn_dm05_workSave);
        btn_dm05_timeSave = (TextView) findViewById(R.id.btn_dm05_timeSave);
        btn_dm05_timeSend = (TextView) findViewById(R.id.btn_dm05_timeSend);

        check_dm05_day = (CheckBox) findViewById(R.id.check_dm05_day);
        check_dm05_night = (CheckBox) findViewById(R.id.check_dm05_night);
        check_dm05_daySix = (CheckBox) findViewById(R.id.check_dm05_daySix);
        check_dm05_homecall = (CheckBox) findViewById(R.id.check_dm05_homecall);

        check_dm05_r01p_adapter_select = (CheckBox) findViewById(R.id.check_dm05_r01p_adapter_select);

        lin_dm_check = (LinearLayout) findViewById(R.id.lin_dm_check);
        lin_dm_button = (LinearLayout) findViewById(R.id.lin_dm_button);
        lin_dm_workSave = (LinearLayout) findViewById(R.id.lin_dm_workSave);
        lin_dm_timeSave = (LinearLayout) findViewById(R.id.lin_dm_timeSave);

        setEvents();
    }

    private void setEvents() {
        tv_dm05_tab1.setOnClickListener(this);
        tv_dm05_tab2.setOnClickListener(this);
        lv_dm05_workList.setOnItemClickListener(this);
        btn_dm05_troubleshooting.setOnClickListener(this);
        btn_dm05_earlywork.setOnClickListener(this);
        btn_dm05_workSave.setOnClickListener(this);
        btn_dm05_timeSave.setOnClickListener(this);
        btn_dm05_timeSend.setOnClickListener(this);

        dataset();
    }

    private void dataset() {
        tv_dm05_workDt_v.setText(selectDt);
        new selectWatchKeeping().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dm05_tab1:
                btnSelect(TAB_SELECTWORK);
                break;
            case R.id.tv_dm05_tab2:
                btnSelect(TAB_WORKTIME);
                break;
            case R.id.btn_dm05_troubleshooting:
                addTimeWork("CA21", "고장수리지원");
                break;
            case R.id.btn_dm05_earlywork:
                addTimeWork("CD11", "조기출근");
                break;
            case R.id.btn_dm05_workSave:
                if (rep_st == null || rep_st.equals("0")) {
                    saveXml("jobSelection");
                } else {
                    AlertView.showAlert("이미 상신된 내역은 저장할 수 없습니다.", context);
                }
                break;
            case R.id.btn_dm05_timeSave:
                if (saveValidation == true) {
                    if (rep_st.equals("0") || rep_st.equals("9")) {
                        saveXml("timeWork");
                    } else {
                        AlertView.showAlert("이미 상신된 내역은 저장할 수 없습니다.", context);
                    }
                } else {
                    AlertView.showAlert("경고 메세지를 확인 해주십시오.", context);
                }
                break;
            case R.id.btn_dm05_timeSend:
                if (saveValidation == true) {
                    if (timeWorkModifyYn == true) {
                        AlertView.showAlert("수정된 내용이 저장되어 있지 않습니다.", context);
                    } else {
                        SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, "상신하시겠습니까?", new SimpleYesNoDialog.btnClickListener() {
                            @Override
                            public void onButtonClick() {
                                new sendTimeWork().execute();
                            }
                        });
                        ynDialog.show();
                    }
                } else {
                    AlertView.showAlert("경고 메세지를 확인 해주십시오.", context);
                }

                break;
            case R.id.btn_dm05_r01p_adapter_delete:
                TextView button = (TextView) v;
                clickItemPosition = Integer.parseInt(button.getTag(R.id.btn_dm05_r01p_adapter_delete).toString());
                String workTime = "";
                if (!itemList02.get(clickItemPosition).getCS_TM_FR().equals("") && !itemList02.get(clickItemPosition).getCS_TM_TO().equals("")) {
                    workTime = itemList02.get(clickItemPosition).getCS_TM_FR() + "~" + itemList02.get(clickItemPosition).getCS_TM_TO() + itemList02.get(clickItemPosition).getCS_TM();
                }
                String msg = "작업명 : " + itemList02.get(clickItemPosition).getWORK_NM() + "\n" + "작업시간 : " + workTime + "\n" + "삭제하시겠습니까?";
                SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, msg, new SimpleYesNoDialog.btnClickListener() {
                    @Override
                    public void onButtonClick() {
                        if (itemList02.get(clickItemPosition).getS_NO() != null && !itemList02.get(clickItemPosition).getS_NO().equals("")) {
                            sNo = itemList02.get(clickItemPosition).getS_NO();
                            workCd = itemList02.get(clickItemPosition).getWORK_CD();
                            new deleteTimeWork().execute();
                        } else {
                            itemList02.remove(clickItemPosition);
                            adapter02.notifyDataSetChanged();
                            AlertView.showAlert("삭제되었습니다.", context);
                        }
                    }
                });
                ynDialog.show();

                break;
            case R.id.ln_dm05_r01p_adapter_workTime:
                LinearLayout lnTime = (LinearLayout) v;
                selectTimePosition = Integer.parseInt(lnTime.getTag(R.id.ln_dm05_r01p_adapter_workTime).toString());
                showTimePicker(selectTimePosition);
                break;
        }
    }


    private void btnSelect(int idx) {
        switch (idx) {
            case 1:
                if (timeWorkModifyYn == true) {
                    SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, "작업선택으로 이동 시 변경된 내역이 사라집니다. 이동 하시겠습니까?", new SimpleYesNoDialog.btnClickListener() {
                        @Override
                        public void onButtonClick() {
                            tv_dm05_tab1.setBackgroundResource(R.drawable.tab_dm_selectwork_on);
                            tv_dm05_tab2.setBackgroundResource(R.drawable.tab_dm_timechange_off);
                            tv_dm05_tab1.setEnabled(false);
                            tv_dm05_tab2.setEnabled(true);
                            lin_dm_check.setVisibility(View.VISIBLE);
                            lin_dm_button.setVisibility(View.GONE);
                            lin_dm_workSave.setVisibility(View.VISIBLE);
                            lin_dm_timeSave.setVisibility(View.GONE);
                            sp_dm05_isAttended_v.setEnabled(true);
                            progress(true);
                            new selectWatchKeeping().execute();
                            new jobSelection().execute("basicWorkTime");
                            timeWorkModifyYn = false;
                        }
                    });
                    ynDialog.show();
                } else {
                    tv_dm05_tab1.setBackgroundResource(R.drawable.tab_dm_selectwork_on);
                    tv_dm05_tab2.setBackgroundResource(R.drawable.tab_dm_timechange_off);
                    tv_dm05_tab1.setEnabled(false);
                    tv_dm05_tab2.setEnabled(true);
                    lin_dm_check.setVisibility(View.VISIBLE);
                    lin_dm_button.setVisibility(View.GONE);
                    lin_dm_workSave.setVisibility(View.VISIBLE);
                    lin_dm_timeSave.setVisibility(View.GONE);
                    sp_dm05_isAttended_v.setEnabled(true);
                    progress(true);
                    new selectWatchKeeping().execute();
                    new jobSelection().execute("basicWorkTime");
                }
                break;
            case 2:
                tv_dm05_tab1.setBackgroundResource(R.drawable.tab_dm_selectwork_off);
                tv_dm05_tab2.setBackgroundResource(R.drawable.tab_dm_timechange_on);
                tv_dm05_tab1.setEnabled(true);
                tv_dm05_tab2.setEnabled(false);
                lin_dm_check.setVisibility(View.GONE);
                lin_dm_button.setVisibility(View.VISIBLE);
                lin_dm_workSave.setVisibility(View.GONE);
                lin_dm_timeSave.setVisibility(View.VISIBLE);
                sp_dm05_isAttended_v.setEnabled(false);
                progress(true);
                new selectWorkMaster().execute();
                new selectTimeWork().execute();
                timeWorkModifyYn = false;
                break;
        }
    }


    public void setTotalTime() {
        String minTime = "";
        String maxTime = "";
        String minDt;
        String maxDt;
        String str_Total = "";
        if (itemList01.size() > 0) {
            minDt = itemList01.get(0).getJOB_TM_FR();
            maxDt = itemList01.get(0).getJOB_TM_FR();
            int totalTime = 0;
            int itemList01_Size = itemList01.size();

            for (int i = 0; i < itemList01_Size; i++) {
                // 0 : 같은 날짜
                // 1 : A가 B보다 이후 날짜
                // -1 : A가 B보다 이전 날짜
                String X = itemList01.get(i).getJOB_TM_FR();
                if (minDt.compareTo(X) >= 0) {
                    minDt = X;
                    minTime = itemList01.get(i).getJOB_TM_MI_FR();
                }
                if (maxDt.compareTo(X) <= 0) {
                    maxDt = X;
                    maxTime = itemList01.get(i).getJOB_TM_MI_TO();
                }
                totalTime = totalTime + Integer.parseInt(itemList01.get(i).getJOB_TM_DIFF_MM());
            }
            str_Total = minTime + "~" + maxTime + "(" + totalTime + "분)";
        }
        tv_dm05_totalWorkDt_v.setText(str_Total);
    }

    private void addTimeWork(final String addCd, final String addNm) {
        final DM_DM05_R02P dm05_r02p = new DM_DM05_R02P(context, addNm);
        dm05_r02p.show();
        dm05_r02p.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!dm05_r02p.bldgNo.equals("") && !dm05_r02p.carNo.equals("")) {
                    itemList02.add(new DM_DM05_R01P_ITEM02(
                            "20",
                            "",
                            empId,
                            selectDt,
                            addCd,
                            addNm,
                            addCd,
                            "",
                            "",
                            "",
                            dm05_r02p.bldgNo,
                            dm05_r02p.bldgNm,
                            dm05_r02p.carNo,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "1",
                            "",
                            "",
                            "1",
                            "Y"
                    ));
                    adapter02.notifyDataSetChanged();
                    timeWorkModifyYn = true;
                    validationTimeWork();

                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() == adapter01) {
            adapter01.setChecked(position);
            adapter01.notifyDataSetChanged();
        }
    }

    // 작업선택 조회
    public class jobSelection extends AsyncTask<String, Integer, String> {
        GetHttp http = new GetHttp();
        JSONObject returnJson01;

        @Override
        protected String doInBackground(String... params) {
            // 1. basicWorkTime
            if (params[0].equals("basicWorkTime")) {
                try {
                    String param_url_02 = WebServerInfo.getUrl()
                            + "dm/jobSelection.do";
                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("empId", empId));
                    arguments.add(new BasicNameValuePair("workDt", selectDt));

//                    arguments.add(new BasicNameValuePair("empId", "301200"));
//                    arguments.add(new BasicNameValuePair("workDt", "2019-05-21"));
                    returnJson01 = http.getPost(param_url_02, arguments, true);
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
            // 1. basicWorkTime
            if (result.equals("basicWorkTime")) {

                try {
                    itemList01.clear();

                    int jsonSize = returnJson01.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        itemList01.add(new DM_DM05_R01P_ITEM01(
                                ejl01.getValue(i, "CS_EMP_ID"),
                                ejl01.getValue(i, "WORK_DT"),
                                ejl01.getValue(i, "JOB_NO"),
                                ejl01.getValue(i, "WORK_CD"),
                                ejl01.getValue(i, "WORK_NM"),
                                ejl01.getValue(i, "JOB_TM_FR"),
                                ejl01.getValue(i, "JOB_TM_TO"),
                                ejl01.getValue(i, "JOB_TM_MI_FR"),
                                ejl01.getValue(i, "JOB_TM_MI_TO"),
                                ejl01.getValue(i, "JOB_TM_DIFF_MM"),
                                ejl01.getValue(i, "JOB_TM_NM"),
                                ejl01.getValue(i, "BLDG_NO"),
                                ejl01.getValue(i, "BLDG_NM"),
                                ejl01.getValue(i, "CAR_NO"),
                                ejl01.getValue(i, "REF_CONTR_NO"),
                                ejl01.getValue(i, "SUPPORT_CD")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList01.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                }

                adapter01 = new DM_DM05_R01P_Adapter01(context,
                        R.layout.dm_dm05_r01p_adapter01, itemList01);
                DM_DM05_R01P.this.lv_dm05_workList.setAdapter(adapter01);
                progress(false);

                setTotalTime();
            }
        }
    }

    //  시간조정 마스터 조회
    public class selectWorkMaster extends AsyncTask<Void, String, Void> {
        GetHttp http = new GetHttp();
        JSONObject returnJson;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String param_url = WebServerInfo.getUrl()
                        + "dm/selectWorkMaster.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                arguments.add(new BasicNameValuePair("empId", empId));
                arguments.add(new BasicNameValuePair("workDt", selectDt));

                returnJson = http.getPost(param_url, arguments, true);

                try {
                    ejl_matser = new EasyJsonList(
                            returnJson.getJSONArray("dataList"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            try {
                work_tm_nm = ejl_matser.getValue(0, "WORK_TM_NM");
                status_cd = ejl_matser.getValue(0, "ATTEND_CD");
                rep_st = ejl_matser.getValue(0, "REP_ST");
                base_limit_mi = ejl_matser.getValue(0, "BASE_LIMIT_MI");
                base_limit_mi_10 = ejl_matser.getValue(0, "BASE_LIMIT_MI_10");
                base_limit_mi_20 = ejl_matser.getValue(0, "BASE_LIMIT_MI_20");

                // 기존 근태구분 spinner에 동일한 근태코드 선택
                for (int i = 0; i < array_sp_status_cd.size(); i++) {
                    if (status_cd.equals(array_sp_status_cd.get(i))) {
                        sp_dm05_isAttended_v.setSelection(i);
                    }
                }
                tv_dm05_totalWorkDt_v.setText(work_tm_nm);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 시간조정(정상근무, 당직) 조회
    public class selectTimeWork extends AsyncTask<Void, Integer, Void> {
        GetHttp http = new GetHttp();
        JSONObject returnJson_day;
        JSONObject returnJson_night;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String param_url_day = WebServerInfo.getUrl()
                        + "dm/selectWorkDetailDay.do";
                String param_url_night = WebServerInfo.getUrl()
                        + "dm/selectWorkDetailNight.do";
                List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                arguments.add(new BasicNameValuePair("empId", empId));
                arguments.add(new BasicNameValuePair("workDt", selectDt));
//                arguments.add(new BasicNameValuePair("empId", "301200"));
//                arguments.add(new BasicNameValuePair("workDt", "2019-02-22"));
                returnJson_day = http.getPost(param_url_day, arguments, true);
                returnJson_night = http.getPost(param_url_night, arguments, true);

                try {
                    ejl_detail_day = new EasyJsonList(
                            returnJson_day.getJSONArray("dataList"));
                    ejl_detail_night = new EasyJsonList(
                            returnJson_night.getJSONArray("dataList"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                itemList02.clear();

                int jsonSize_day = returnJson_day.getJSONArray("dataList")
                        .length();
                for (int i = 0; i < jsonSize_day; i++) {
                    itemList02.add(new DM_DM05_R01P_ITEM02(
                            ejl_detail_day.getValue(i, "WORK_TP"),
                            ejl_detail_day.getValue(i, "S_NO"),
                            ejl_detail_day.getValue(i, "EMP_ID"),
                            ejl_detail_day.getValue(i, "ATTEND_DT"),
                            ejl_detail_day.getValue(i, "WORK_CD"),
                            ejl_detail_day.getValue(i, "WORK_NM"),
                            ejl_detail_day.getValue(i, "DUTY_WORK_CD"),
                            ejl_detail_day.getValue(i, "CS_TM_FR"),
                            ejl_detail_day.getValue(i, "CS_TM_TO"),
                            ejl_detail_day.getValue(i, "CS_TM"),
                            ejl_detail_day.getValue(i, "BLDG_NO"),
                            ejl_detail_day.getValue(i, "BLDG_NM"),
                            ejl_detail_day.getValue(i, "CAR_NO"),
                            ejl_detail_day.getValue(i, "REG_CONTR_NO"),
                            ejl_detail_day.getValue(i, "SUPPORT_CD"),
                            ejl_detail_day.getValue(i, "RMK"),
                            ejl_detail_day.getValue(i, "JOB_NO"),
                            ejl_detail_day.getValue(i, "JOB_TM_FR"),
                            ejl_detail_day.getValue(i, "JOB_TM_TO"),
                            null
                            , null
                            , null
                            , null
                            , "0"
                            , "0"
                            , "N"));
                }

                int jsonSize_night = returnJson_night.getJSONArray("dataList")
                        .length();
                for (int j = 0; j < jsonSize_night; j++) {
                    itemList02.add(new DM_DM05_R01P_ITEM02(
                            ejl_detail_night.getValue(j, "WORK_TP"),
                            ejl_detail_night.getValue(j, "S_NO"),
                            ejl_detail_night.getValue(j, "EMP_ID"),
                            ejl_detail_night.getValue(j, "ATTEND_DT"),
                            ejl_detail_night.getValue(j, "WORK_CD"),
                            ejl_detail_night.getValue(j, "WORK_NM"),
                            ejl_detail_night.getValue(j, "DUTY_WORK_CD"),
                            ejl_detail_night.getValue(j, "CS_TM_FR"),
                            ejl_detail_night.getValue(j, "CS_TM_TO"),
                            ejl_detail_night.getValue(j, "CS_TM"),
                            ejl_detail_night.getValue(j, "BLDG_NO"),
                            ejl_detail_night.getValue(j, "BLDG_NM"),
                            ejl_detail_night.getValue(j, "CAR_NO"),
                            ejl_detail_night.getValue(j, "REG_CONTR_NO"),
                            ejl_detail_night.getValue(j, "SUPPORT_CD"),
                            ejl_detail_night.getValue(j, "RMK"),
                            ejl_detail_night.getValue(j, "JOB_NO"),
                            ejl_detail_night.getValue(j, "JOB_TM_FR"),
                            ejl_detail_night.getValue(j, "JOB_TM_TO"),
                            ejl_detail_night.getValue(j, "MIN_DT")
                            , ejl_detail_night.getValue(j, "MAX_DT")
                            , ejl_detail_night.getValue(j, "REQUIRED_CHK_TM")
                            , ejl_detail_night.getValue(j, "REQUIRED_CHK_BLDG_NO")
                            , ejl_detail_night.getValue(j, "EDIT_CHK")
                            , ejl_detail_night.getValue(j, "DEL_CHK")
                            , "N"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (itemList02.size() == 0) {
                alert("조회된 데이타가 없습니다", context);
            }

            adapter02 = new DM_DM05_R01P_Adapter02(context,
                    R.layout.dm_dm05_r01p_adapter02, itemList02, onClickListener);
            DM_DM05_R01P.this.lv_dm05_workList.setAdapter(adapter02);
            validationTimeWork();
            progress(false);
        }
    }

    //  사용자 근태상태 및 일직,숙직 여부 조회
    public class selectWatchKeeping extends AsyncTask<Void, String, Void> {
        GetHttp http = new GetHttp();
        JSONObject returnJson03;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String param_url_02 = WebServerInfo.getUrl()
                        + "dm/selectWatchKeeping.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                arguments.add(new BasicNameValuePair("empId", empId));
                arguments.add(new BasicNameValuePair("workDt", selectDt));
//                arguments.add(new BasicNameValuePair("workDt", "2019-05-07"));
                returnJson03 = http.getPost(param_url_02, arguments, true);

                try {
                    ejl_watchkeeping = new EasyJsonList(
                            returnJson03.getJSONArray("dataList"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            try {
                int jsonSize = returnJson03.getJSONArray("dataList")
                        .length();
                for (int i = 0; i < jsonSize; i++) {
                    itemList03.add(new DM_DM05_R01P_ITEM03(ejl_watchkeeping.getValue(i, "ATTEND_CD")
                            , ejl_watchkeeping.getValue(i, "DATE_IF")
                            , ejl_watchkeeping.getValue(i, "OFFICE_WORK_CHK")
                            , ejl_watchkeeping.getValue(i, "HOLLY_IF")
                            , ejl_watchkeeping.getValue(i, "DAY_DUTY_CODE")
                            , ejl_watchkeeping.getValue(i, "DAY_DUTY_NAME")
                            , ejl_watchkeeping.getValue(i, "DAY_DUTY_IF")
                            , ejl_watchkeeping.getValue(i, "NIGHT_DUTY_CODE")
                            , ejl_watchkeeping.getValue(i, "NIGHT_DUTY_NAME")
                            , ejl_watchkeeping.getValue(i, "NIGHT_DUTY_IF")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            office_chk = itemList03.get(0).OFFICE_WORK_CHK;
            // OFFICE_WORK_CHK = 1 사무실(일직, 숙직)    OFFICE_WORK_CHK = 2 집(일직(6인이하), 홈콜대기)
            if (office_chk.equals("1")) {
                check_dm05_day.setVisibility(View.VISIBLE);
                check_dm05_night.setVisibility(View.VISIBLE);
                check_dm05_daySix.setVisibility(View.GONE);
                check_dm05_homecall.setVisibility(View.GONE);

                check_dm05_day.setChecked(itemList03.get(0).getDAY_DUTY_IF().equals("1"));
                check_dm05_night.setChecked(itemList03.get(0).getNIGHT_DUTY_IF().equals("1"));
            } else {
                check_dm05_day.setVisibility(View.GONE);
                check_dm05_night.setVisibility(View.GONE);
                check_dm05_daySix.setVisibility(View.VISIBLE);
                check_dm05_homecall.setVisibility(View.VISIBLE);

                check_dm05_daySix.setChecked(itemList03.get(0).getDAY_DUTY_IF().equals("1"));
                check_dm05_homecall.setChecked(itemList03.get(0).getNIGHT_DUTY_IF().equals("1"));
            }

            new selectWorkStatus().execute();
        }
    }

    // 근태구분
    public class selectWorkStatus extends AsyncTask<Void, String, Void> {
        GetHttp http = new GetHttp();
        JSONObject returnJson_status;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String param_url = WebServerInfo.getUrl()
                        + "dm/selectWorkStatus.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                arguments.add(new BasicNameValuePair("mainCd", "C0124"));           // 근태구분 조회 프로시저의 입력파라미터값 "C0124" 고정.

                returnJson_status = http.getPost(param_url, arguments, true);

                try {
                    ejl_sp_status = new EasyJsonList(
                            returnJson_status.getJSONArray("dataList"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            int Attend_position = 0;
            try {
                array_sp_status_cd.clear();
                array_sp_status_nm.clear();

                int jsonSize = returnJson_status.getJSONArray("dataList")
                        .length();
                for (int i = 0; i < jsonSize; i++) {
                    array_sp_status_cd.add(ejl_sp_status.getValue(i, "SUB_CD"));
                    array_sp_status_nm.add(ejl_sp_status.getValue(i, "TITLE"));
                    if (itemList03.get(0).ATTEND_CD.equals(array_sp_status_cd.get(i))) {
                        Attend_position = i;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, array_sp_status_nm);
            sp_dm05_isAttended_v.setAdapter(adapter);
            sp_dm05_isAttended_v.setSelection(Attend_position);
            sp_dm05_isAttended_v.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    status_cd = array_sp_status_cd.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    // 작업선택, 시간조정 저장
    private String inputXml01, inputXml02;
    private DM_DM05_R01P_Header01 DM_DM05_R01P_Header01;
    private ArrayList<DM_DM05_R01P_Detail01> DM_DM05_R01P_Detail01 = new ArrayList<DM_DM05_R01P_Detail01>();
    private DM_DM05_R01P_Header02 DM_DM05_R01P_Header02;
    private ArrayList<DM_DM05_R01P_Detail02> DM_DM05_R01P_Detail02 = new ArrayList<DM_DM05_R01P_Detail02>();

    private void saveXml(String param) {
        if (param.equals("jobSelection")) {
            TrxnDocument document = new TrxnDocument();
            try {
                String day_if, night_if;
                if (office_chk.equals("1")) {
                    day_if = returnBoolen(check_dm05_day.isChecked());
                    night_if = returnBoolen(check_dm05_night.isChecked());
                } else {
                    day_if = returnBoolen(check_dm05_daySix.isChecked());
                    night_if = returnBoolen(check_dm05_homecall.isChecked());
                }
                DM_DM05_R01P_Header01 = new DM_DM05_R01P_Header01(empId, selectDt + " 00:00:00.000", status_cd, itemList03.get(0).getDAY_DUTY_CODE(), day_if
                        , itemList03.get(0).getNIGHT_DUTY_CODE(), night_if);
                DM_DM05_R01P_Detail01.clear();
                for (int i = 0; i < itemList01.size(); i++) {
                    if (adapter01.getChecked(i) == true) {
                        DM_DM05_R01P_Detail01.add(new DM_DM05_R01P_Detail01(itemList01.get(i).getJOB_NO()
                                , Integer.toString(i)
                                , itemList01.get(i).getWORK_CD()
                                , itemList01.get(i).getJOB_TM_FR()
                                , itemList01.get(i).getJOB_TM_TO()
                                , itemList01.get(i).getBLDG_NO()
                                , itemList01.get(i).getCAR_NO()
                                , itemList01.get(i).getREF_CONTR_NO()
                                , itemList01.get(i).getSUPPORT_CD()));
                    }
                }
                document.setDM_DM05_R01P_Header01(DM_DM05_R01P_Header01);
                document.setDM_DM05_R01P_Detail01(DM_DM05_R01P_Detail01);
                inputXml01 = document.toXmlWorkSelection();
            } catch (Exception ex) {
                AlertView.showAlert(ex.getMessage(), context);
                return;
            }
            progress(true);
            new savejobSelection().execute("jobSelection");
        } else if (param.equals("timeWork")) {
            TrxnDocument document = new TrxnDocument();
            try {
                DM_DM05_R01P_Header02 = new DM_DM05_R01P_Header02(empId, selectDt, status_cd, rep_st, base_limit_mi, base_limit_mi_10, base_limit_mi_20);
                DM_DM05_R01P_Detail02.clear();
                for (int i = 0; i < itemList02.size(); i++) {
                    DM_DM05_R01P_Detail02.add(new DM_DM05_R01P_Detail02(itemList02.get(i).getWORK_TP()
                            , itemList02.get(i).getS_NO()
                            , itemList02.get(i).getWORK_CD()
                            , itemList02.get(i).getDUTY_WORK_CD()
                            , itemList02.get(i).getCS_TM_FR()
                            , itemList02.get(i).getCS_TM_TO()
                            , itemList02.get(i).getCS_TM().substring(1, itemList02.get(i).getCS_TM().length() - 2)       // ex) (60분)으로 가져오기때문에 숫자 60만 전달해야함.
                            , itemList02.get(i).getBLDG_NO()
                            , itemList02.get(i).getCAR_NO()
                            , itemList02.get(i).getREF_CONTR_NO()
                            , itemList02.get(i).getSUPPORT_CD()
                            , itemList02.get(i).getJOB_NO()
                            , itemList02.get(i).getJOB_TM_FR()
                            , itemList02.get(i).getJOB_TM_TO()));
                }
                document.setDM_DM05_R01P_Header02(DM_DM05_R01P_Header02);
                document.setDM_DM05_R01P_Detail02(DM_DM05_R01P_Detail02);
                inputXml02 = document.toXmlTimeWork();
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            progress(true);
            new savejobSelection().execute("timeWork");
        }
    }

    public class savejobSelection extends AsyncTask<String, Integer, String> {
        String rtn = "";
        String msg = "";

        @Override
        protected String doInBackground(String... params) {
            if (params[0].equals("jobSelection")) {
                try {
                    GetHttp getHttp = new GetHttp();
                    String param_url = WebServerInfo.getUrl() + "dm/savejobSelection.do";
                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                    arguments.add(new BasicNameValuePair("inputXml", inputXml01));
                    arguments.add(new BasicNameValuePair("empId", empId));
                    JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
                    try {
                        EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                        int jsonSize = returnJson.getJSONArray("dataList").length();

                        if (jsonSize > 0) {
                            rtn = ejl.getValue(0, "RTN_CD");
                            msg = ejl.getValue(0, "RTN_MSG");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        msg = e.getMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = e.getMessage();
                }
                return params[0];

            } else if (params[0].equals("timeWork")) {
                try {
                    GetHttp getHttp = new GetHttp();
                    String param_url = WebServerInfo.getUrl() + "dm/saveTimeWork.do";
                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                    arguments.add(new BasicNameValuePair("inputXml", inputXml02));
                    arguments.add(new BasicNameValuePair("empId", empId));
                    JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
                    try {
                        EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                        int jsonSize = returnJson.getJSONArray("dataList").length();

                        if (jsonSize > 0) {
                            rtn = ejl.getValue(0, "RTN_CD");
                            msg = ejl.getValue(0, "RTN_MSG");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        msg = e.getMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = e.getMessage();
                }
                return params[0];
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String title = "";
            progress(false);
            if (result.equals("jobSelection")) {
                title = "작업선택";
            } else if (result.equals("timeWork")) {
                title = "시간조정";
            }
            // rtn = 1 정상, rtn = 0 실패
            if ("1".equals(rtn)) {
                SimpleDialog MessageBox = new SimpleDialog(context, "알림", title + "을 저장했습니다.", new SimpleDialog.btnClickListener() {
                    @Override
                    public void onButtonClick() {
                        btnSelect(TAB_WORKTIME);
                    }
                });
                MessageBox.setCancelable(false);
                MessageBox.show();
            } else {
                SimpleDialog MessageBox = new SimpleDialog(context, "알림", title + "저장 실패\n오류 메시지 : " + msg, new SimpleDialog.btnClickListener() {
                    @Override
                    public void onButtonClick() {
                    }
                });
                MessageBox.setCancelable(false);
                MessageBox.show();
            }
        }
    }

    // 시간조정(고장수리지원, 일직당직) 삭제
    private String sNo, workCd;

    public class deleteTimeWork extends AsyncTask<Void, String, Void> {
        String rtn = "";
        String msg = "";
        GetHttp http = new GetHttp();
        JSONObject returnJson;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String param_url = WebServerInfo.getUrl()
                        + "dm/deleteTimeWork.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", empId));
                arguments.add(new BasicNameValuePair("workDt", selectDt));
                arguments.add(new BasicNameValuePair("sNo", sNo));
                arguments.add(new BasicNameValuePair("workCd", workCd));
                arguments.add(new BasicNameValuePair("useId", empId));

                returnJson = http.getPost(param_url, arguments, true);

                try {
                    EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    int jsonSize = returnJson.getJSONArray("dataList").length();

                    if (jsonSize > 0) {
                        rtn = ejl.getValue(0, "RTN_CD");
                        msg = ejl.getValue(0, "RTN_MSG");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg = e.getMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = e.getMessage();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            if ("1".equals(rtn)) {
                AlertView.showAlert("삭제되었습니다.", context);
                adapter02.notifyDataSetChanged();
            } else {
                AlertView.showAlert("오류 메시지 : " + msg, context);
            }
        }
    }

    public class sendTimeWork extends AsyncTask<Void, String, Void> {
        String rtn = "";
        String msg = "";
        GetHttp http = new GetHttp();
        JSONObject returnJson;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String param_url = WebServerInfo.getUrl()
                        + "dm/sendTimeWork.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", empId));
                arguments.add(new BasicNameValuePair("workDt", selectDt));
                arguments.add(new BasicNameValuePair("useId", empId));

                returnJson = http.getPost(param_url, arguments, true);

                try {
                    EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    int jsonSize = returnJson.getJSONArray("dataList").length();

                    if (jsonSize > 0) {
                        rtn = ejl.getValue(0, "RTN_CD");
                        msg = ejl.getValue(0, "RTN_MSG");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg = e.getMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            if ("1".equals(rtn)) {
                AlertView.showAlert("상신 완료", context);
                btnSelect(TAB_WORKTIME);
            } else {
                AlertView.showAlert("오류 메시지 : " + msg, context);
            }
        }
    }

    private String saveErrorMsg = "";
    private Boolean saveValidation;
    private String startDt;
    private String endDt;

    private void validationTimeWork() {
        int totalTime = 0;
        tv_dm05_errmsg.setVisibility(View.VISIBLE);
        for (int i = 0; i < itemList02.size(); i++) {
            // 시간 조정 가능한 내역
            if (itemList02.get(i).getWORK_TP().equals("10") || (itemList02.get(i).getWORK_TP().equals("20") && itemList02.get(i).getREQUIRED_CHK_TM().equals("1"))) {
                // 가장 처음 작업의 시작시간이 오전 08:30이 아닌 경우.
                if (i==0) {
                    try {
                        String firstStartTime = selectDt + " " + itemList02.get(i).getCS_TM_FR();
                        String staticStartTime = selectDt + " 08:30";
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date firstST = dateFormat.parse(firstStartTime);
                        Date staticST = dateFormat.parse(staticStartTime);
                        long startTimeDiff = (firstST.getTime() - staticST.getTime()) / 1000 / 60;
                        if(startTimeDiff < 0) {
                            saveErrorMsg = "경고 : 첫번째 작업의 시작시간은 08:30부터 설정할 수 있습니다.";
                            tv_dm05_errmsg.setText(saveErrorMsg);
                            saveValidation = false;
                            return;
                        }
                    }catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                // 작업시간이 비어있는 경우
                if (itemList02.get(i).getCS_TM_FR().equals("") || itemList02.get(i).getCS_TM_TO().equals("")) {
                    saveErrorMsg = "경고 : 작업 시간이 비어있는 항목이 있습니다.";
                    tv_dm05_errmsg.setText(saveErrorMsg);
                    saveValidation = false;
                    return;
                }
                // 일반 작업의 시간이 연속적이지 않은 경우
                if (i < itemList02.size() - 1 && itemList02.get(i).getWORK_TP().equals("10") && itemList02.get(i + 1).getWORK_TP().equals("10")) {
                    String before_dt = itemList02.get(i).getCS_TM_TO();
                    String after_dt = itemList02.get(i + 1).getCS_TM_FR();
                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        Date bfDt = dFormat.parse(selectDt + " " + before_dt);
                        Date afDt = dFormat.parse(selectDt + " " + after_dt);
                        if ((bfDt.compareTo(afDt)) != 0) {
                            saveErrorMsg = "경고 : 작업 시간 연속성 오류";
                            tv_dm05_errmsg.setText(saveErrorMsg);
                            saveValidation = false;
                            return;
                        }
                    } catch (ParseException e) {
                        e.getMessage();
                    }
                }
                totalTime += Integer.parseInt(itemList02.get(i).getCS_TM().substring(1, itemList02.get(i).getCS_TM().length() - 2));        // 전체 작업시간
            }
        }
        // 전체 작업시간이 30분단위가 아닌경우
        if (totalTime % 30 != 0) {
            saveErrorMsg = "경고 : 전체 작업시간의 단위가 30분이 아닙니다.";
            tv_dm05_errmsg.setText(saveErrorMsg);
            saveValidation = false;
            return;
        }
        saveErrorMsg = "";
        tv_dm05_errmsg.setText(saveErrorMsg);
        tv_dm05_errmsg.setVisibility(View.GONE);
        saveValidation = true;
    }

    private void showTimePicker(int position) {
        int fromHour, fromMinute, toHour, toMinute;
        String job_fr = "";
        String job_to = "";
        if (itemList02.get(position).getS_NO().equals("") && (itemList02.get(position).getCS_TM_FR().equals("") || itemList02.get(position).getCS_TM_TO().equals(""))) {
            Calendar currCal = Calendar.getInstance();
            fromHour = Integer.valueOf(currCal.get(Calendar.HOUR_OF_DAY));
            fromMinute = Integer.valueOf(currCal.get(Calendar.MINUTE));
            toHour = Integer.valueOf(currCal.get(Calendar.HOUR_OF_DAY));
            toMinute = Integer.valueOf(currCal.get(Calendar.MINUTE));
        } else {
            fromHour = Integer.valueOf(itemList02.get(position).getCS_TM_FR().substring(0, 2));
            fromMinute = Integer.valueOf(itemList02.get(position).getCS_TM_FR().substring(3, 5));
            toHour = Integer.valueOf(itemList02.get(position).getCS_TM_TO().substring(0, 2));
            toMinute = Integer.valueOf(itemList02.get(position).getCS_TM_TO().substring(3, 5));
            job_fr = itemList02.get(position).getJOB_TM_FR();
            job_to = itemList02.get(position).getJOB_TM_TO();
        }

        String workTp = itemList02.get(position).getWORK_TP();
        boolean isEarlyWork = itemList02.get(position).getWORK_CD().equals("CD11") ? true : false;
        Intent intent = new Intent(DM_DM05_R01P.this, TimePickerStartEndActivity.class);
        intent.putExtra("isEarlyWork", isEarlyWork);
        intent.putExtra("workTp", workTp);
        intent.putExtra("selectDt", selectDt);
        intent.putExtra("base_limit_mi", base_limit_mi);
        intent.putExtra("job_fr", job_fr);
        intent.putExtra("job_to", job_to);
        intent.putExtra("fromHour", fromHour);
        intent.putExtra("fromMinute", fromMinute);
        intent.putExtra("toHour", toHour);
        intent.putExtra("toMinute", toMinute);
        startActivityForResult(intent, 0);
    }

    private String returnBoolen(Boolean b) {
        String value;
        if (b == true) {
            value = "1";
        } else {
            value = "0";
        }
        return value;
    }


    /**
     * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:

                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    int fromHour = bundle.getInt("fromHour");
                    int fromMinute = bundle.getInt("fromMinute");
                    int toHour = bundle.getInt("toHour");
                    int toMinute = bundle.getInt("toMinute");
                    startDt = bundle.getString("startDt");
                    endDt = bundle.getString("endDt");
                    String date_diff = bundle.getString("date_diff");

                    String fhour = String.valueOf(fromHour);
                    if (fromHour < 10) {
                        fhour = "0" + fhour;
                    }
                    String fmin = String.valueOf(fromMinute);
                    if (fromMinute < 10) {
                        fmin = "0" + fmin;
                    }
                    String fdateTime = fhour + ":" + fmin;

                    String thour = String.valueOf(toHour);
                    if (toHour < 10) {
                        thour = "0" + thour;
                    }
                    String tmin = String.valueOf(toMinute);
                    if (toMinute < 10) {
                        tmin = "0" + tmin;
                    }
                    String tdateTime = thour + ":" + tmin;

                    itemList02.get(selectTimePosition).setCS_TM(date_diff);
                    itemList02.get(selectTimePosition).setCS_TM_FR(fdateTime);
                    itemList02.get(selectTimePosition).setCS_TM_TO(tdateTime);
                    adapter02.notifyDataSetChanged();
                    timeWorkModifyYn = true;
                    validationTimeWork();
                    break;
                }
        }

    }

    @Override
    public void onBackPressed() {
        if (timeWorkModifyYn == true) {
            SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, "뒤로가기 시 변경된 내역이 사라집니다. 이동 하시겠습니까?", new SimpleYesNoDialog.btnClickListener() {
                @Override
                public void onButtonClick() {
                    Intent intent = new Intent(context, DM_DM05_R00.class);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            ynDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private void alert(String msg, Context context) {
        AlertView.showAlert(msg, context);
    }

    private void progress(Boolean isActivated) {
        if (isActivated) {
            DM_DM05_R01P.this.progress =
                    ProgressDialog.show(context, "알림", "조회 중입니다.");
        } else {
            DM_DM05_R01P.this.progress.dismiss();
        }
    }
};