package com.jinsit.kmec.IP.IS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.IR.CI.IR_CI03_R02;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.DB.MasterDataDownload;

public class IP_IS02_R01P extends AlertDialog {

    //parameters
    Context context;
    Map<String, String> paraMap;

    //uiInstances
    TextView tv01_popTitle;
    TextView btn_popClose;
    ListView lv01_is02_p01;

    //Http
    private EasyJsonList ejl;

    //adapter
    private JSONObject returnJson;
    private List<IP_IS02_R01P_Item01> itemList;
    private ListAdapter adpater;

    private boolean onPopUp;

    //입력 변수
    private String empId;
    private String workDt;
    private String jobNo;
    private String bldgNo;
    private String bldgNm;
    private String carNo;

    protected IP_IS02_R01P(Context context, Map<String, String> paraMap) {
        super(context);
        this.context = context;
        this.paraMap = paraMap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_is02_r01p);
        getInstances();
        tv01_popTitle.setText("작업대상목록");
        new Database().execute(paraMap.get("csEmpId").toString()
                , paraMap.get("workDt").toString()
        );
    }

    private void getInstances() {

        context = getContext();
        itemList = new ArrayList<IP_IS02_R01P_Item01>();
        tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
        btn_popClose = (TextView) findViewById(R.id.btn_popClose);
        lv01_is02_p01 = (ListView) findViewById(R.id.lv01_is02_p01);
        setEvents();
    }

    private void setEvents() {
        btn_popClose.setOnClickListener(listener);
    }


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_popClose:
                    cancel();
                    break;
                default:
                    System.out.println("[개발자Msg] out of range of CASE");
                    break;
            }

        }
    };


    private class Database extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            GetHttp http = new GetHttp();
            String url = WebServerInfo.getUrl() + "ip/selectInspectionObjectList.do";

            List<NameValuePair> arguments = new ArrayList<NameValuePair>();
            arguments.clear();
            arguments.add(new BasicNameValuePair("csEmpId", params[0]));
            arguments.add(new BasicNameValuePair("workDt", params[1]));

            returnJson = http.getPost(url, arguments, true);

            try {
                ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
            } catch (JSONException e) {
                e.printStackTrace();
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
            setData();
        }
    }


    private void setData() {

        try {
            itemList.clear();
            int jsonSize = returnJson.getJSONArray("dataList").length();
            for (int i = 0; i < jsonSize; i++) {
                itemList.add(new IP_IS02_R01P_Item01(
                                ejl.getValue(i, "WORK_DT")
                                , ejl.getValue(i, "BLDG_NM")
                                , ejl.getValue(i, "CAR_NO")
                                , ejl.getValue(i, "WORK_NM")
                                , ejl.getValue(i, "JOB_ST_NM")
                                , ejl.getValue(i, "CS_TM_FR")
                                , ejl.getValue(i, "Y_CNT")
                                , ejl.getValue(i, "T_CNT")
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adpater = new IP_IS02_R01P_Adapter01(context, R.layout.listitem_job, itemList);
        lv01_is02_p01.setAdapter(adpater);

        lv01_is02_p01.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Map<String, String> argMap = new HashMap<String, String>();
                try {
                    argMap.put("workDt", ejl.getValue(arg2, "WORK_DT"));
                    argMap.put("yCnt", ejl.getValue(arg2, "Y_CNT"));
                    argMap.put("bldgNo", ejl.getValue(arg2, "BLDG_NO"));
                    argMap.put("bldgNm", ejl.getValue(arg2, "BLDG_NM"));
                    argMap.put("carNo", ejl.getValue(arg2, "CAR_NO"));
                    argMap.put("contrTpNm", ejl.getValue(arg2, "CONTR_TP_NM"));
                    argMap.put("inspTimeBcNm", ejl.getValue(arg2, "INSP_TIME_BC_NM"));

                    // Item 클릭 시 입력변수(점검표 생성 및 조회)
                    jobNo = ejl.getValue(arg2, "JOB_NO");
                    empId = ejl.getValue(arg2, "CS_EMP_ID");
                    workDt = ejl.getValue(arg2, "WORK_DT");
                    bldgNo = ejl.getValue(arg2, "BLDG_NO");
                    bldgNm = ejl.getValue(arg2, "BLDG_NM");
                    carNo = ejl.getValue(arg2, "CAR_NO");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                // 작업대상정보
//				IP_IS02_R02P d02 = new IP_IS02_R02P(context, argMap);
//				d02.show();

                // 점검계획표 생성 후, 점검계획 내역 조회로 화면전환.        by.이창헌
                new CreatePartCheckListAsync().execute();

            }
        });
    }

    private class CreatePartCheckListAsync extends AsyncTask<Void, Void, Boolean> {
        private android.app.ProgressDialog ProgressDialog;
        private EasyJsonMap dataMap;
        private EasyJsonMap msgMap;
        private String exceptionMsg = null;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                GetHttp http = new GetHttp();
                String paramUrl = WebServerInfo.getUrl() + "ip/createPartCheckList.do";
                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", empId));
                arguments.add(new BasicNameValuePair("workDt", workDt));
                arguments.add(new BasicNameValuePair("jobNo", jobNo));

                JSONObject jsonObj = http.getPost(paramUrl, arguments, true);

                try {
                    msgMap = new EasyJsonMap(jsonObj.getJSONObject("msgMap"));
                    dataMap = new EasyJsonMap(jsonObj.getJSONObject("dataMap"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    exceptionMsg = e.toString();
                    return false;
                }
            } catch (Exception e) {
                exceptionMsg = e.toString();
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.ProgressDialog = android.app.ProgressDialog.show(context, "점검항목 생성", "점검항목을 생성 중 입니다.");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            this.ProgressDialog.dismiss();
            if (result) {
                try {
                    boolean isError = msgMap.getValue("errCd").equals("0") ? true : false;//0이면 정상
                    if (isError) {
                        if (dataMap.getValue("RTN").equals("0")) {
                            //정상 : 점검항목 생성됨
                            SimpleDialog sm01 = new SimpleDialog(context, "알림", "점검항목이 생성되었습니다.", new SimpleDialog.btnClickListener() {
                                @Override
                                public void onButtonClick() {
                                    Intent intent = new Intent(context, IR_CI03_R02.class);
                                    intent.putExtra("empId", empId);
                                    intent.putExtra("workDt", workDt);
                                    intent.putExtra("bldgNo", bldgNo);
                                    intent.putExtra("bldgNm", bldgNm);
                                    intent.putExtra("carNo", carNo);
                                    intent.putExtra("isCI", false);
                                    context.startActivity(intent);
                                }
                            });
                            sm01.show();

                        } else {
                            //실패 : 점검항목 생성X
                            alert("점검항목을 생성실패 하였습니다. 관리자에게 문의하세요", context);
                        }
                    } else {
                        alert(msgMap.getValue("errMsg"), context);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                if (exceptionMsg != null) {
                    String errMsg = "";

                    try {
                        if (msgMap != null) errMsg = msgMap.getValue("errMsg");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    alert(exceptionMsg + errMsg, context);
                }
            }
        }

        private void alert(String msg, Context context) {
            if (!onPopUp) {
                onPopUp = true;
                AlertView.showAlert(msg, context, new android.content.DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        onPopUp = false;
                    }
                });
            }
        }
    }
}

