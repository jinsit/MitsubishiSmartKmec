package com.jinsit.kmec.IP.IS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.CM.CM_SearchElev;

import com.jinsit.kmec.IR.PI.IR_PI02_R00;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
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

import java.util.ArrayList;
import java.util.List;

public class IP_IS04_R00 extends Activity implements View.OnClickListener {

    private Context context;

    private CommonSession cs;
    private String empID;
    private String is_bldgNo;
    private TextView tv_is_bldgNm;
    private TextView btn_is_bldgNm;

    private String is_CarNo;
    private String old_CarNo;
    private TextView tv_is_CarNo;
    private TextView btn_is_CarNo;

    private ListView lv_is_selectInfoTrans;
    private View.OnClickListener onClickListener;

    private TextView is_inqueryStartDate;
    private TextView btn_startCalendar;
    private TextView is_inqueryEndDate;
    private TextView btn_endCalendar;
    private TextView btn_inquery;

    // POPUP
    private EasyJsonList ejl01;
    private EasyJsonList ejl02;
    private List<IP_IS04_R00_Item01> itemList;
    private ProgressDialog progress;
    private ListAdapter adapter01;

    private List<IP_IS04_R00_ErrorMsg> errorList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_is04_r00);
        activityInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        naviPref = new HomeNaviPreference(context);
        navigationInit();
    }

    protected void activityInit() {
        getInstances();
        context = this;
        cs = new CommonSession(context);
        empID = cs.getEmpId();

        android.app.ActionBar abar = getActionBar();
        abar.setTitle("승관원전송결과 조회");
        abar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, abar);
        itemList = new ArrayList<IP_IS04_R00_Item01>();
        errorList = new ArrayList<IP_IS04_R00_ErrorMsg>();

        this.setStartDate(DateUtil.nowDate());
        this.setEndDate(DateUtil.nowDate());
    }

    protected void getInstances() {
        tv_is_bldgNm = (TextView) findViewById(R.id.tv_is_bldgNm);
        btn_is_bldgNm = (TextView) findViewById(R.id.btn_is_bldgNm);
        tv_is_CarNo = (TextView) findViewById(R.id.tv_is_CarNo);
        btn_is_CarNo = (TextView) findViewById(R.id.btn_is_CarNo);
        lv_is_selectInfoTrans = (ListView) findViewById(R.id.lv_is_selectInfoTrans);

        is_inqueryStartDate = (TextView) findViewById(R.id.et_inqueryStartDate);
        btn_startCalendar = (TextView) findViewById(R.id.btn_startCalendar);
        is_inqueryEndDate = (TextView) findViewById(R.id.et_inqueryEndDate);
        btn_endCalendar = (TextView) findViewById(R.id.btn_endCalendar);
        btn_inquery = (TextView) findViewById(R.id.btn_inquery);

        setEvents();
    }

    protected void setEvents() {
        btn_is_bldgNm.setOnClickListener(this);
        btn_is_CarNo.setOnClickListener(this);

        is_inqueryStartDate.setOnClickListener(this);
        btn_startCalendar.setOnClickListener(this);
        is_inqueryEndDate.setOnClickListener(this);
        btn_endCalendar.setOnClickListener(this);
        btn_inquery.setOnClickListener(this);
        setConfig();
    }

    private void setConfig() {
        ActivityAdmin.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_is_bldgNm:
                searchBldg();
                break;
            case R.id.btn_is_CarNo:
                if (is_bldgNo == null) {
                    AlertView.showAlert("건물명을 먼저 입력하셔야 합니다.", context);
                    return;
                }
                searchCarno();
                break;
            case R.id.btn_startCalendar:
                getCalendar(0);
                break;
            case R.id.btn_endCalendar:
                getCalendar(1);
                break;
            case R.id.btn_inquery:
                searchResult();
                break;
            case R.id.et_inqueryStartDate:
                getCalendar(0);
                break;
            case R.id.et_inqueryEndDate:
                getCalendar(1);
                break;
            default:
                break;
        }
    }

    private void getCalendar(int fromTo) {

        Intent intent = new Intent(IP_IS04_R00.this,
                com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
        startActivityForResult(intent, fromTo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    this.setStartDate(bundle.getString("dateSelected"));
                    break;
                } else {
                    break;
                }
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    this.setEndDate(bundle.getString("dateSelected"));
                    break;
                } else {
                    break;
                }
        }
    }

    private void setStartDate(String inqueryStartDate) {
        this.is_inqueryStartDate.setText(inqueryStartDate);
    }

    public void setEndDate(String inqueryEndDate) {
        this.is_inqueryEndDate.setText(inqueryEndDate);
    }

    private void searchBldg() {
        final CM_SearchBldg eBldg = new CM_SearchBldg(this);
        eBldg.show();
        eBldg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

                String no = eBldg.getBldgNo();
                if (no.equals("") || no == null) {
                } else {
                    setBldgNo(eBldg.getCsDeptNm(), eBldg.getBldgNo(), eBldg.getBldgNm());
                }
            }

        });
    }

    private void setBldgNo(String departmentName, String bldgNo, String bldgNm) {
        this.tv_is_bldgNm.setText(bldgNm);
        is_bldgNo = bldgNo;         // 호기명 검색 시 건물명을 기준으로 조회하기 때문에 변수 저장.
    }

    private void searchCarno() {
        final CM_SearchElev eCarno = new CM_SearchElev(context, is_bldgNo);
        eCarno.show();
        eCarno.elevSearch();
        eCarno.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                old_CarNo = is_CarNo;
                String no = eCarno.getReturnStr();
                if (no.equals("") || no == null) {
                } else {
                    setCarno(eCarno.getReturnStr(), eCarno.getReturnModelNm());
                }
            }

        });
    }

    private void setCarno(String CarNo, String ModelNm) {
        this.tv_is_CarNo.setText(CarNo);
        is_CarNo = CarNo;         // 호기명 검색 시 건물명을 기준으로 조회하기 때문에 변수 저장.
    }

    private void searchResult() {
        if (is_CarNo == null || is_CarNo == "") {
            AlertView.showAlert("호기명을 입력하셔야 합니다.", context);
            return;
        } else {
            progress(true);
            new IP_IS04_R00.selectInfo().execute("basicWorkTime");
        }
    }

    public class selectInfo extends AsyncTask<String, Integer, String> {

        GetHttp http = new GetHttp();
        JSONObject returnJson01;

        @Override
        protected String doInBackground(String... params) {

            // 1. bagicWorkTime
            if (params[0].equals("basicWorkTime")) {
                try {
                    String param_url_01 = WebServerInfo.getUrl()
                            + "ip/selectMngSendResult.do";

                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("csEmpId", IP_IS04_R00.this.empID));
                    arguments.add(new BasicNameValuePair("fromDt", IP_IS04_R00.this.is_inqueryStartDate.getText().toString()));
                    arguments.add(new BasicNameValuePair("toDt", IP_IS04_R00.this.is_inqueryEndDate.getText().toString()));
                    arguments.add(new BasicNameValuePair("bldgNo", IP_IS04_R00.this.is_bldgNo));
                    arguments.add(new BasicNameValuePair("carNo", IP_IS04_R00.this.tv_is_CarNo.getText().toString()));

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
            return "None";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 1. bagicWorkTime
            if (result.equals("basicWorkTime")) {

                try {
                    itemList.clear();
//					AlertView.showAlert("TEST", context);

                    int jsonSize = returnJson01.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        itemList.add(new IP_IS04_R00_Item01(
                                ejl01.getValue(i, "WORK_DT"),
                                ejl01.getValue(i, "BLDG_NO"),
                                ejl01.getValue(i, "BLDG_NM"),
                                ejl01.getValue(i, "CAR_NO"),
                                ejl01.getValue(i, "ELEVATOR_NO"),
                                ejl01.getValue(i, "SEND_DATE"),
                                ejl01.getValue(i, "SEND_YN"),
                                ejl01.getValue(i, "SEND_YN_NM"),
                                ejl01.getValue(i, "CS_EMP_ID"),
                                ejl01.getValue(i, "JOB_NO")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (itemList.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                }
//				KeyboardUtil.hideKeyboard(et_pi_inqueryText, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);

                adapter01 = new IP_IS04_R00_Adapter01(context,
                        R.layout.ip_is04_r00_adapter01, itemList, onClickListener);
                IP_IS04_R00.this.lv_is_selectInfoTrans.setAdapter(adapter01);
                progress(false);
                lv_is_selectInfoTrans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (itemList.get(position).getSEND_YN().equals("N")
                                || itemList.get(position).getSEND_YN().equals("W")
                                || itemList.get(position).getSEND_YN_NM().equals("전송실패")
                                || itemList.get(position).getSEND_YN_NM().equals("확인중")) {
                            csEmpId = itemList.get(position).getCS_EMP_ID();
                            workDt = itemList.get(position).getWORK_DT();
                            jobNo = itemList.get(position).getJOB_NO();
                            new IP_IS04_R00.errorMsg().execute("basicWorkTime");
                        }
                    }
                });
            }
        }
    }// end of SelectData inner-class

    private String csEmpId;
    private String workDt;
    private String jobNo;

    public class errorMsg extends AsyncTask<String, Integer, String> {
        GetHttp http = new GetHttp();
        JSONObject returnJson;

        @Override
        protected String doInBackground(String... params) {
            // 1. basicWorkTime
            if (params[0].equals("basicWorkTime")) {
                try {
                    String param_url_01 = WebServerInfo.getUrl()
                            + "ip/selectMngMsgResult.do";

                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("csEmpId", csEmpId));
                    arguments.add(new BasicNameValuePair("workDt", workDt));
                    arguments.add(new BasicNameValuePair("jobNo", jobNo));

                    returnJson = http.getPost(param_url_01, arguments, true);

                    try {
                        ejl02 = new EasyJsonList(
                                returnJson.getJSONArray("dataList"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params[0];
            }
            return "None";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 1. basicWorkTime
            String Msg = "";
            if (result.equals("basicWorkTime")) {
                try {
                    errorList.clear();
                    int jsonSize = returnJson.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        errorList.add(new IP_IS04_R00_ErrorMsg(
                                ejl02.getValue(i, "MSG_ID"),
                                ejl02.getValue(i, "MSG_DESC")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                    return;
                }
                for (int row = 0; row < errorList.size(); row++) {
                    if (row == 0) {
                        Msg = errorList.get(row).getMsg_Id() + " : " + errorList.get(row).getMsg_Desc();
                    } else {
                        Msg = Msg + "\n\n" + errorList.get(row).getMsg_Id() + " : " + errorList.get(row).getMsg_Desc();
                    }
                }
                AlertView.showAlert("승관원 Error 메시지", Msg, context);

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private void progress(Boolean isActivated) {
        if (isActivated) {
            this.progress = android.app.ProgressDialog.show(
                    context, "알림", "조회 중입니다.");
        } else {
            this.progress.dismiss();
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
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naviPref.setHide(!naviPref.isHide());
                homeNavi.setToggleNavi();

            }
        });
    }

}
