package com.jinsit.kmec.IR.PI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
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

public class IR_PI05_R00 extends Activity implements OnClickListener {

    private Context context;

    private LinearLayout ll_pi_bldgNm;
    private LinearLayout ll_pi_CarNo;
    private LinearLayout ll_pi_header;

    private String pi_bldgNo;
    private TextView tv_pi_bldgNm;
    private TextView btn_pi_bldgNm;

    private String pi_CarNo;
    private String old_CarNo;
    private TextView tv_pi_CarNo;
    private TextView btn_pi_CarNo;

    private ListView lv_pi_partsInfoCondition;

    private OnClickListener onClickListener;
    // POP1
    private EasyJsonList ejl01;
    private List<IR_PI05_R00_Item> itemList;

    private ProgressDialog progress;
    private ListAdapter adpater01;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ir_pi05_r00);
        activityInit();
    }

    protected void activityInit() {
        getInstances();
        context = this;
        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("부품정보현황");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);
        selTp = "1";
        itemList = new ArrayList<IR_PI05_R00_Item>();
    }

    protected void getInstances() {
        tv_pi_bldgNm = (TextView) findViewById(R.id.tv_pi_bldgNm);
        btn_pi_bldgNm = (TextView) findViewById(R.id.btn_pi_bldgNm);
        tv_pi_CarNo = (TextView) findViewById(R.id.tv_pi_CarNo);
        btn_pi_CarNo = (TextView) findViewById(R.id.btn_pi_CarNo);
        ll_pi_bldgNm = (LinearLayout) findViewById(R.id.ll_pi_bldgNm);
        ll_pi_CarNo = (LinearLayout) findViewById(R.id.ll_pi_CarNo);
        ll_pi_header = (LinearLayout) findViewById(R.id.ll_pi_header);

        lv_pi_partsInfoCondition = (ListView) findViewById(R.id.lv_pi_partsInfoCondition);

        headerVisible(false);
        setEvents();
    }

    protected void setEvents() {
        btn_pi_bldgNm.setOnClickListener(this);
        btn_pi_CarNo.setOnClickListener(this);

        setConfig();
    }

    private void setConfig() {
        ActivityAdmin.getInstance().addActivity(this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_pi_bldgNm:
                searchBldg();
                break;
            case R.id.btn_pi_CarNo:
                if (pi_bldgNo == null) {
                    AlertView.showAlert("건물명을 먼저 입력하셔야 합니다.", context);
                    return;
                }
                searchCarno();


            default:
                break;
        }
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
        this.tv_pi_bldgNm.setText(bldgNm);
        pi_bldgNo = bldgNo;         // 호기명 검색 시 건물명을 기준으로 조회하기 때문에 변수 저장.
    }

    private void searchCarno() {


        final CM_SearchElev eCarno = new CM_SearchElev(context, pi_bldgNo);
        eCarno.show();
        eCarno.elevSearch();
        eCarno.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                old_CarNo = pi_CarNo;
                String no = eCarno.getReturnStr();
                if (no.equals("") || no == null) {
                } else {
                    setCarno(eCarno.getReturnStr(), eCarno.getReturnModelNm());
                }
                if (pi_CarNo != old_CarNo) {                    // 선택한 호기와 이전 호기 값이 다를 경우 실행. ex) 호기 조회 누르고 선택없이 팝업창 닫을 경우 실행하지 않음.   - 이창헌
                    progress(true);
                    new selectInfo().execute("bagicWorkTime");
                }

//                progress(false);
            }

        });
    }

    private void setCarno(String CarNo, String ModelNm) {
        this.tv_pi_CarNo.setText(CarNo);
        pi_CarNo = CarNo;         // 호기명 검색 시 건물명을 기준으로 조회하기 때문에 변수 저장.
    }


    public class selectInfo extends AsyncTask<String, Integer, String> {

        GetHttp http = new GetHttp();
        JSONObject returnJson01;

        @Override
        protected String doInBackground(String... params) {

            // 1. bagicWorkTime
            if (params[0].equals("bagicWorkTime")) {
                try {
                    String param_url_01 = WebServerInfo.getUrl()
                            + "ir/selectPartsItem.do";

                    List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                    arguments.add(new BasicNameValuePair("carNo", IR_PI05_R00.this.tv_pi_CarNo.getText().toString()));

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
            if (result.equals("bagicWorkTime")) {

                try {
                    itemList.clear();
//					AlertView.showAlert("TEST", context);

                    int jsonSize = returnJson01.getJSONArray("dataList")
                            .length();
                    for (int i = 0; i < jsonSize; i++) {
                        itemList.add(new IR_PI05_R00_Item(
                                ejl01.getValue(i, "PITEM_NO"),
                                ejl01.getValue(i, "DS_DRAW_NO"),
                                ejl01.getValue(i, "GL_NO"),
                                ejl01.getValue(i, "DS_DRAW_NM"),
                                ejl01.getValue(i, "QTY"),
                                ejl01.getValue(i, "ITEM_NO"),
                                ejl01.getValue(i, "REMARK")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (itemList.size() == 0) {
                    AlertView.showAlert("조회된 데이타가 없습니다", context);
                }

                adpater01 = new IR_PI05_R00_Adapter(context,
                        R.layout.ir_pi05_r00_adapter, itemList, onClickListener);
                IR_PI05_R00.this.lv_pi_partsInfoCondition
                        .setAdapter(adpater01);
                progress(false);
                headerVisible(true);
            }
        }
    }// end of SelectData inner-class

    private void progress(Boolean isActivated) {
        if (isActivated) {
            IR_PI05_R00.this.progress = android.app.ProgressDialog.show(
                    context, "알림", "조회 중입니다.");
        } else {
            IR_PI05_R00.this.progress.dismiss();
        }
    }

    // 2019-01-21 이창헌. ListView 헤더 표시
    private Boolean isBack = true;             // true : 뒤로가기. false : 헤더 표시

    private void headerVisible(Boolean status) {
        if (status) {
            // 건물명, 호기명 숨김 / 헤더 표시
            ll_pi_bldgNm.setVisibility(View.GONE);
            ll_pi_CarNo.setVisibility(View.GONE);
            ll_pi_header.setVisibility(View.VISIBLE);
            isBack = false;
        } else {
            // 건물명, 호기명 표시 / 헤더 숨김
            ll_pi_bldgNm.setVisibility(View.VISIBLE);
            ll_pi_CarNo.setVisibility(View.VISIBLE);
            ll_pi_header.setVisibility(View.GONE);
            isBack = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isBack) {
            super.onBackPressed();
        }
        // 조회 시 Header 부분 표시 구분.
        headerVisible(false);
    }

    private String selTp;
    private String itemNo;


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

}
