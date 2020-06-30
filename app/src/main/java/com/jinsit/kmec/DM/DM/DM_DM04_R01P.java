package com.jinsit.kmec.DM.DM;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DM_DM04_R01P extends Dialog implements View.OnClickListener {
    public DM_DM04_R01P(Context context, String YEAR_WEEK_DT, String emp_Id, String emp_Nm) {
        super(context);
        this.context = context;
        this.selected_week_dt = YEAR_WEEK_DT;
        this.emp_Id = emp_Id;
        this.emp_Nm = emp_Nm;
    }

    private Context context;
    // /title 위젯
    private TextView tv01_popTitle;
    private TextView btn_popClose;

    private ListView lv_dm04_r01_weekData;
    private TextView tv_dm04_r01_total;
    private ProgressDialog progress;
    private CommonSession commonSession;
    private android.app.ProgressDialog ProgressDialog;


    private EasyJsonList ejl01;

    private List<DM_DM04_R01P_ITEM01> itemList01;
    private DM_DM04_R01P_Adapter01 adapter01;

    private String selected_week_dt;
    private String emp_Id;
    private String emp_Nm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dm_dm04_r01p);

        activityInit();
    }


    protected void activityInit() {

        tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
        btn_popClose = (TextView) findViewById(R.id.btn_popClose);

        tv01_popTitle.setText("개인 근태 현황 (" + emp_Nm + ")");
        itemList01 = new ArrayList<DM_DM04_R01P_ITEM01>();

        lv_dm04_r01_weekData = (ListView) findViewById(R.id.lv_dm04_r01_weekData);
        tv_dm04_r01_total = (TextView) findViewById(R.id.tv_dm04_r01_total);

        btn_popClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_popClose:
                this.dismiss();
                break;
            default:
                break;
        }
    }


    public class selectWeekWork extends AsyncTask<String, Integer, String> {
        GetHttp http = new GetHttp();
        JSONObject returnJson01;

        @Override
        protected String doInBackground(String... params) {
            // 1. basicWorkTime
            if (params[0].equals("basicWorkTime")) {
                try {
                    String param_url_01 = WebServerInfo.getUrl()
                            + "dm/selectEmpWeek.do";

                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("workDtFrTo", selected_week_dt));
                    arguments.add(new BasicNameValuePair("empNo", emp_Id));

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
            // 1. basicWorkTime
            if (result.equals("basicWorkTime")) {

                try {
                    itemList01.clear();

                    int jsonSize = returnJson01.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        itemList01.add(new DM_DM04_R01P_ITEM01(
                                ejl01.getValue(i, "YEAR_WEEK_NM"),
                                ejl01.getValue(i, "WEEK_DAY_NM"),
                                ejl01.getValue(i, "WORK_DT"),
                                ejl01.getValue(i, "WORK_HH")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (itemList01.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                }

                adapter01 = new DM_DM04_R01P_Adapter01(context,
                        R.layout.dm_dm04_r01p_adapter, itemList01);
                DM_DM04_R01P.this.lv_dm04_r01_weekData.setAdapter(adapter01);
                Double total;
                total = Double.parseDouble(itemList01.get(7).WORK_HH) + Double.parseDouble(itemList01.get(15).WORK_HH);
                tv_dm04_r01_total.setText(total.toString());
                progress(false);
            }
        }
    }
    public void requestPopup() {
        progress(true);
        new selectWeekWork().execute("basicWorkTime");
    }

    private void progress(Boolean isActivated) {
        if (isActivated) {
            this.ProgressDialog = android.app.ProgressDialog.show(getContext(), "알림", "조회중 입니다.");
        } else {
            this.ProgressDialog.dismiss();
        }
    }
}


	
	
