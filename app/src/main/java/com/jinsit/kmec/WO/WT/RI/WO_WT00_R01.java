package com.jinsit.kmec.WO.WT.RI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SelectInspector;
import com.jinsit.kmec.CM.SearchAdminInfo;
import com.jinsit.kmec.CM.SelectPendCode;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.DB.UpdateTable;
import com.jinsit.kmec.IR.ES.InspectorByTeam;
import com.jinsit.kmec.IR.ES.InspectorByTeam_Adapter;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM01;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.GPSService;
import com.jinsit.kmec.comm.JSONObjectAndException;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.SimpleDialogDetail;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.infrastructure.InspectType;
import com.jinsit.kmec.service.RealTimeJobHistoryService;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

/**
 * WO_WT00_R01
 *
 * @author 원성민
 * @discription 작업대상상세정보 현재 작업상태, 진행현황을 보여준다.
 */
public class WO_WT00_R01 extends Activity implements OnClickListener,
        OnItemClickListener, OnDismissListener {
    private static final int REGISTER_PEND = 0;
    private static final int REGISTER_TRANSFER = 1;
    private static final int REGISTER_HELP = 2;

    private static final String JOB_ACT_MOVE = "11";
    private static final String JOB_ACT_START = "15";
    private static final String JOB_ACT_ARRIVE = "31";
    private static final String JOB_ACT_COMPLETE = "39";
    private String jobAct = "";
    private ListView lv_wt_jobStatus = null;

    private ProgressDialog ProgressDialog;
    //private ArrayList<WorkTargetData> workTargetData;
    private WO_WT00_R01_ITEM00 workTargetData;
    private ArrayList<WO_WT00_R01_ITEM01> workStatusData;
    WO_WT00_R01_ITEM01 nfcPosition;

    ArrayList<RoutineCheckListData> routineCheckListItem;
    private WO_WT00_R01_Adapter01 wO_WT00_R01_Adapter01;
    private EasyJsonList ejl;
    private EasyJsonMap ejm;
    private RoutineCheckListData routineCheckListData;
    Context context;
    Activity activity;
    TextView tv_wt_date, tv_wt_workNm, tv_wt_bldgInfo, tv_wt_addr,
            tv_wt_csDeptNm, tv_wt_empNm1, tv_wt_empHp1, tv_wt_empNm2,
            tv_wt_empHp2, tv_wt_st, tv_wt_cs_fr, tv_wt_moveTm, tv_wt_arriveTm,
            tv_wt_completeTm;
    Button btn_wt_adminInfo, btn_wt_jobComplete, btn_wt_sendData;
    private LinearLayout linear_wt_mainInspector;    //주점검자, /부점검자 클릭 리스너
    private LinearLayout linear_wt_subInspector;
    private RadioGroup radioGroupInspector;
    private RadioButton radioButtonSingleInsp, radioButtonDoubleInsp;
    private TextView tv_mainInspName, tv_mainInspCode, tv_subInspName, tv_subInspCode;
    private String InspTyp; //점검타입 CsTp

    private String jobNo, empId, workDt, carNo;//
    String nfcPlc;
    String refControlNo;
    String reservSt;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private String ndefMsg;
    private String tagUid = "";


    private CommonSession commonSession;
    private boolean progressWorking;
    private boolean onPopUp;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ip_wo_r00, menu);
        return true;
    }


    private boolean isJobStatusCheck() {
        boolean jobStatus = false;
        for (int i = 0; i < workStatusData.size(); i++) {
            if (!workStatusData.get(i).getJOB_ST().equals("00") && !workStatusData.get(i).getJOB_ST().equals("39")) {
                jobStatus = true;
                break;
            }
        }
        return jobStatus;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // String[] param = {empId, workDt, jobNo};
        String[] param = {empId, workDt, jobNo};
        if (id == R.id.menu_ip_wo_pendWork) {
            // /미처리
//			if(isJobStatusCheck()){
//				SimpleDialog sm01 = new SimpleDialog(context, "알림","점검항목 진행 중에는 미처리를 할 수 없습니다. 현재 점검중인 작업을 완료하고 미처리하세요");
//				sm01.show();
//				return false;
//			}

            SelectPendCode selectPendCode = new SelectPendCode(context, param,
                    activity, workStatusData);
            selectPendCode.show();

            return true;

        } else if (id == R.id.menu_ip_wo_transferWork) {
            // 이관
//			if(isJobStatusCheck()){
//				SimpleDialog sm01 = new SimpleDialog(context, "알림","점검항목 진행 중에는 이관을 할 수 없습니다. 현재 점검중인 작업을 완료하고 이관하세요");
//				sm01.show();
//				return false;
//			}
            CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
                    context, REGISTER_TRANSFER, param, activity);
            cM_SelectInspector.show();
            return true;
        } else if (id == R.id.menu_ip_wo_helpWork) {
            // 지원요청
            if (reservSt.equals("2")) {
                SimpleDialog sm01 = new SimpleDialog(context, "알림", "지원요청이 불가능한 작업입니다.");
                sm01.show();
                return false;
            }
			/*if(isJobStatusCheck()){
				SimpleDialog sm01 = new SimpleDialog(context, "알림","점검항목 진행후에는 지원요청을 할 수 없습니다.");
				sm01.show();
				return false;
			}*/
            CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
                    context, REGISTER_HELP, param, activity);
            cM_SelectInspector.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null)
            enableTagWriteMode();
        naviPref = new HomeNaviPreference(context);
        navigationInit();
        localDetailSet();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.e("onRestart", "onRestart  =");
    }

    ;

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            disableTagWriteMode();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub

        if (progressWorking) {
            Toast.makeText(context, "현재모드에서는 NFC TAG를 읽을 수 없습니다", Toast.LENGTH_LONG);
            return;
        }
        if (onPopUp) {
            Toast.makeText(context, "현재모드에서는 NFC TAG를 읽을 수 없습니다", Toast.LENGTH_LONG);
            return;
        }

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] duration = {50, 100, 200, 300};
        vib.vibrate(duration, -1);

        // Tag writing mode
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            tagUid = NfcParser.getTagId(detectedTag);
            ndefMsg = NfcParser.getTagText(detectedTag);
            Log.e("tagUid", tagUid);
            Log.e("ndefMsg", ndefMsg);

            if (!tagUid.isEmpty()) {

                if (NetworkStates.isNetworkStatus(context)) {
                    //new OfficeNfcCheckAsync().execute();
                    nfcTagEvent(ndefMsg);
                } else {
                    nfcTagEvent(ndefMsg);
                }

            }

            //nfcTagEvent(ndefMsg);

//			if(nfcPlc.equals("")){
//
//			}else{
//
//			}
//			if (!ndefMsg.isEmpty()) {
//			nfcTagEvent(ndefMsg);
//			} else {
//				// 비어있을 때 작업
//
//				AlertView.showAlert("등록되지 않은 태그입니다. NFC등록을 먼저 하십시오.", context);
//				SimpleYesNoDialog synd = new SimpleYesNoDialog(context,
//						"등록되지 않은 태그입니다. 태그등록 하시겠습니까?", new btnClickListener() {
//							@Override
//							public void onButtonClick() {
//								// TODO Auto-generated method stub
//								Intent intent = new Intent(context,IR_NM00_R00.class);
//								startActivity(intent);
//								finish();
//							}
//						});
//				synd.show();
//			}


        }

    }

    private void enableTagWriteMode() {
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[]{tagDetected};
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
                mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mNfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wo_wt00_r01);
        activityInit();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    private void activityInit() {
        // TODO Auto-generated method stub
        activity = this;
        context = this;
        android.app.ActionBar aBar = getActionBar();

        aBar.setTitle("작업대상정보");
        aBar.setDisplayShowHomeEnabled(false);
        JActionbar.setActionBar(this, aBar);
        commonSession = new CommonSession(context);
        jobNo = getIntent().getExtras().getString("jobNo");
        workDt = getIntent().getExtras().getString("workDt");
        // Log.e("jobNo", jobNo);
        lv_wt_jobStatus = (ListView) findViewById(R.id.lv_wt_jobStatus);
        lv_wt_jobStatus.setOnItemClickListener(this);
        tv_wt_date = (TextView) findViewById(R.id.tv_wt_date);
        tv_wt_workNm = (TextView) findViewById(R.id.tv_wt_workNm);
        tv_wt_bldgInfo = (TextView) findViewById(R.id.tv_wt_bldgInfo);
        tv_wt_addr = (TextView) findViewById(R.id.tv_wt_addr);
        tv_wt_csDeptNm = (TextView) findViewById(R.id.tv_wt_csDeptNm);
        tv_wt_empNm1 = (TextView) findViewById(R.id.tv_wt_empNm1);
        tv_wt_empHp1 = (TextView) findViewById(R.id.tv_wt_empHp1);
        tv_wt_empNm2 = (TextView) findViewById(R.id.tv_wt_empNm2);
        tv_wt_empHp2 = (TextView) findViewById(R.id.tv_wt_empHp2);
        tv_wt_st = (TextView) findViewById(R.id.tv_wt_st);
        tv_wt_cs_fr = (TextView) findViewById(R.id.tv_wt_cs_fr);
        tv_wt_moveTm = (TextView) findViewById(R.id.tv_wt_moveTm);
        tv_wt_moveTm.setOnClickListener(this);
        tv_wt_arriveTm = (TextView) findViewById(R.id.tv_wt_arriveTm);
        tv_wt_arriveTm.setOnClickListener(this);
        tv_wt_completeTm = (TextView) findViewById(R.id.tv_wt_completeTm);
        btn_wt_adminInfo = (Button) findViewById(R.id.btn_wt_adminInfo);
        btn_wt_adminInfo.setOnClickListener(this);
        btn_wt_jobComplete = (Button) findViewById(R.id.btn_wt_jobComplete);
        btn_wt_jobComplete.setOnClickListener(this);
        btn_wt_sendData = (Button) findViewById(R.id.btn_wt_sendData);
        btn_wt_sendData.setOnClickListener(this);

        //2인점검 부분추가
        linear_wt_mainInspector = (LinearLayout) findViewById(R.id.linear_wt_mainInspector);
        linear_wt_mainInspector.setOnClickListener(this);
        linear_wt_subInspector = (LinearLayout) findViewById(R.id.linear_wt_subInspector);
        linear_wt_subInspector.setOnClickListener(this);

        radioGroupInspector = (RadioGroup) findViewById(R.id.radioGroupInspector);
        radioButtonSingleInsp = (RadioButton) findViewById(R.id.radioButtonSingleInsp);
        radioButtonSingleInsp.setOnClickListener(this);
        radioButtonSingleInsp.setVisibility(View.GONE);
        radioButtonDoubleInsp = (RadioButton) findViewById(R.id.radioButtonDoubleInsp);
        radioButtonDoubleInsp.setOnClickListener(this);

        tv_mainInspName = (TextView) findViewById(R.id.tv_mainInspName);
        tv_mainInspCode = (TextView) findViewById(R.id.tv_mainInspCode);
        tv_subInspName = (TextView) findViewById(R.id.tv_subInspName);
        tv_subInspCode = (TextView) findViewById(R.id.tv_subInspCode);
        //2인점검 부분추가
        setConfig();
    }

    private void setConfig() {
        ActivityAdmin.getInstance().addActivity(this);
    }

    private void localDetailSet() {
        String query = new DatabaseRawQuery().selectJobDetail(commonSession.getEmpId(), workDt, jobNo);

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query,
                null); // 쿼리 날리고
        mCursor.moveToFirst();


        WO_WT00_R01_ITEM00 wO_WT00_R01_ITEM00 = new WO_WT00_R01_ITEM00();
        Field[] fields = wO_WT00_R01_ITEM00.getClass().getDeclaredFields();
        String name = "";
        String value = "";
        for (Field field : fields) {
            name = field.getName();
            try {
                value = mCursor.getString(mCursor.getColumnIndex(name));
                //value = field.get(partCheckListData.get(i)).toString();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                field.set(wO_WT00_R01_ITEM00, value);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        workTargetData = wO_WT00_R01_ITEM00;
        mCursor.close();
        db.close();
        dbHelper.close();
        dataSetting();
        localPartSet();
        local점검자Set();
    }

    private void local점검자Set() {
        String query = new DatabaseRawQuery().selectInspector(commonSession.getEmpId(), workDt, jobNo);

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
        mCursor.moveToFirst();

        //String csTp = mCursor.getString(1);
        String csTp = "2";    //2인점검 고정 20200304 yowonsm
        String main = mCursor.getString(2);
        String mainNm = mCursor.getString(3);
        String sub = mCursor.getString(4);
        String subNm = mCursor.getString(5);

        if (main != null) {
            tv_mainInspCode.setText(main);
            tv_mainInspName.setText(mainNm);
        } else {
            tv_mainInspCode.setText(commonSession.getMngUsrId());
            tv_mainInspName.setText(commonSession.getEmpNm());

        }
        if (sub != null) {
            tv_subInspCode.setText(sub);
            tv_subInspName.setText(subNm);
        } else {
            tv_subInspCode.setText("");
            tv_subInspName.setText("");
        }

        //레디오 그룹 셋팅해주기
        if ("1".equals(csTp)) {
            this.singleInspectorClick();
        } else {
            this.doubleInspectorClick();
        }

    }

    private void localPartSet() {
        String query = new DatabaseRawQuery().selectCheckListPart(commonSession.getEmpId(), workDt, jobNo);

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query,
                null); // 쿼리 날리고
        mCursor.moveToFirst();

        nfcPositionInit(mCursor);
        nfcWorkStateInit(mCursor);
        //workStateInit(mCursor);


        wO_WT00_R01_Adapter01 =
                new WO_WT00_R01_Adapter01(activity,
                        R.layout.listitem_wt_jobstatus, mCursor, new String[]{
                        "NFC_PLC_NM", "JOB_ST_NM"}, null, 0);
        lv_wt_jobStatus.setAdapter(wO_WT00_R01_Adapter01);

        db.close();
        dbHelper.close();
        listViewHeightSet(wO_WT00_R01_Adapter01, lv_wt_jobStatus);
        if (wO_WT00_R01_Adapter01.getCount() < 1) {
            // 기계실, 카, 피트 등이 없는거다. 이럴 때 점검항목생성하는 프로시저 호출 spdc402p;2 번
            SimpleDialog sm01 = new SimpleDialog(context, "알림",
                    "점검항목이 없습니다. 점검항목 생성 후 다운로드를 다시 진행하십시오.",
                    new SimpleDialog.btnClickListener() {
                        @Override
                        public void onButtonClick() {
                            // TODO Auto-generated method stub
                            new CreatePartCheckListAsync().execute();
                        }
                    });
            sm01.show();


        }

    }

    /**
     * 각파트별 점검항목 생성하기
     *
     * @author 원성민
     */
    private class CreatePartCheckListAsync extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog ProgressDialog;
        private EasyJsonMap dataMap;
        private EasyJsonMap msgMap;
        private String exceptionMsg = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {

                GetHttp http = new GetHttp();
                String paramUrl = WebServerInfo.getUrl()
                        + "ip/createPartCheckList.do";
                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", commonSession
                        .getEmpId()));
                arguments.add(new BasicNameValuePair("workDt", workDt));
                arguments.add(new BasicNameValuePair("jobNo", jobNo));

                JSONObject jsonObj = http.getPost(paramUrl, arguments, true);


                try {
                    msgMap = new EasyJsonMap(jsonObj.getJSONObject("msgMap"));
                    dataMap = new EasyJsonMap(jsonObj.getJSONObject("dataMap"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.ProgressDialog = android.app.ProgressDialog.show(
                    WO_WT00_R01.this, "점검항목 생성", "점검항목을 생성 중 입니다.");


        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            this.ProgressDialog.dismiss();
            if (result) {
                try {
                    boolean isError = msgMap.getValue("errCd").equals("0") ? true : false;//0이면 정상
                    if (isError) {

                        if (dataMap.getValue("RTN").equals("0")) {
                            //정상 : 점검항목 생성됨
                            SimpleDialog sm01 = new SimpleDialog(context, "알림",
                                    "점검항목이 생성되었습니다. 다운로드 화면으로 이동합니다.",
                                    new SimpleDialog.btnClickListener() {
                                        @Override
                                        public void onButtonClick() {
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(activity, MasterDataDownload.class);
                                            startActivity(intent);
                                        }
                                    });
                            sm01.show();
                        } else {
                            //실패 : 점검항목 생성 X
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


    }

    private void nfcWorkStateInit(Cursor mCursor) {
        workStatusData = new ArrayList<WO_WT00_R01_ITEM01>();
        WO_WT00_R01_ITEM01 wO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
        Field[] fields = wO_WT00_R01_ITEM01.getClass().getDeclaredFields();
        String name = "";
        String value = "";

        WO_WT00_R01_ITEM01 insertWO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
        if (mCursor != null && mCursor.getCount() != 0) {
            do {

                insertWO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
                for (Field field : fields) {
                    name = field.getName();
                    try {

                        value = mCursor.getString(mCursor.getColumnIndex(name));
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        field.set(insertWO_WT00_R01_ITEM01, value);
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                workStatusData.add(insertWO_WT00_R01_ITEM01);
            } while (mCursor.moveToNext());

        } else {

        }

    }

    private void nfcPositionInit(Cursor cursor) {
        // TODO Auto-generated method stub
        try {


            WO_WT00_R01_ITEM01 wO_WT00_R01_ITEM01 = new WO_WT00_R01_ITEM01();
            Field[] fields = wO_WT00_R01_ITEM01.getClass().getDeclaredFields();
            String name = "";
            String value = "";
            for (Field field : fields) {
                name = field.getName();
                try {
                    value = cursor.getString(cursor.getColumnIndex(name));

                    //value = field.get(partCheckListData.get(i)).toString();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    field.set(wO_WT00_R01_ITEM01, value);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            nfcPosition = wO_WT00_R01_ITEM01;
        } catch (Exception e) {
            nfcPosition = null;
        }

    }

    private void dataSetting() {
        tv_wt_date.setText(workTargetData.getWORK_DT());
        tv_wt_workNm.setText(workTargetData.getWORK_NM());
        tv_wt_bldgInfo.setText(workTargetData.getBLDG_NM() + "/"
                + workTargetData.getCAR_NO());
        tv_wt_addr.setText(workTargetData.getADDR());
        tv_wt_csDeptNm.setText(workTargetData.getCS_DEPT_NM());

        tv_wt_empNm1.setText(workTargetData.getMAIN_EMP_NM());
        tv_wt_empHp1.setText(workTargetData.getMAIN_EMP_PHONE());

        tv_wt_empNm2.setText(workTargetData.getSUB_EMP_NM());
        tv_wt_empHp2.setText(workTargetData.getSUB_EMP_PHONE());

        tv_wt_st.setText(workTargetData.getST());
        tv_wt_cs_fr.setText(workTargetData.getCS_FR());
        tv_wt_moveTm.setText(workTargetData.getMOVE_TM());
        tv_wt_arriveTm.setText(workTargetData.getARRIVE_TM());
        tv_wt_completeTm.setText(workTargetData.getCOMPLETE_TM());
        reservSt = workTargetData.getRESERV_ST();
        empId = workTargetData.getCS_EMP_ID();
        workDt = workTargetData.getWORK_DT();
        refControlNo = workTargetData.getREF_CONTR_NO();
        carNo = workTargetData.getCAR_NO();

        if (jobAct.equals(JOB_ACT_COMPLETE)) {
            enableSendDataButton();
        } else {
            enableJobCompleteButton();
        }
    }

    /**
     * 작업완료 등록
     *
     * @author 원성민
     */


    private void selectRoutineCheckTable() {

        routineCheckListItem = new ArrayList<RoutineCheckListData>();

        RoutineCheckListData mRoutineCheckListData = new RoutineCheckListData();
        Field[] fields = mRoutineCheckListData.getClass().getDeclaredFields();
        String name = "";
        String value = "";
        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = new DatabaseRawQuery().selectCheckTableList(commonSession.getEmpId(), workDt);
        Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
        mCursor.moveToFirst();

        if (mCursor != null && mCursor.getCount() != 0) {

            do {

                mRoutineCheckListData = new RoutineCheckListData();
                for (Field field : fields) {
                    name = field.getName();
                    try {

                        if (!name.equals("serialVersionUID"))
                            value = mCursor.getString(mCursor.getColumnIndex(name));
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        if (!name.equals("serialVersionUID"))
                            field.set(mRoutineCheckListData, value);
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                routineCheckListItem.add(mRoutineCheckListData);
            } while (mCursor.moveToNext());
        }


        int index = 0;
        for (int i = 0; i < routineCheckListItem.size(); i++) {
            if (refControlNo.equals(routineCheckListItem.get(i).getREF_CONTR_NO())
                    && workTargetData.getBLDG_NO().equals(routineCheckListItem.get(i).getBLDG_NO())) {
                index = i;
                break;
            }
        }
        routineCheckListData = routineCheckListItem.get(index);
        //new WorkCompleteAsync().execute();

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.tv_wt_moveTm:
                String moveTm = workTargetData.getMOVE_TM();
                if (moveTm.equals(""))
                    if (!onPopUp) {
                        onPopUp = true;
                        timeRegistration(JOB_ACT_MOVE);
                    }
                break;
            case R.id.tv_wt_arriveTm:
                String arriveTm = workTargetData.getARRIVE_TM();

                if (tv_wt_moveTm.getText().toString().equals("")) {
                    alert("출발을 먼저 하십시오.", context);
                    return;
                }

                if (arriveTm.equals(""))
                    if (!onPopUp) {
                        onPopUp = true;
                        timeRegistration(JOB_ACT_ARRIVE);
                    }
                break;

            case R.id.btn_wt_adminInfo:
                // 관리자정보 호출
                if (!onPopUp) {
                    onPopUp = true;

                    if (NetworkStates.isNetworkStatus(context)) {
                        SearchAdminInfo searchAdminInfo = new SearchAdminInfo(context,
                                workTargetData.getBLDG_NO());
                        searchAdminInfo.show();
                        searchAdminInfo.setOnDismissListener(this);
                    } else {
                        LocalAdminInfo localAdminInfo = new LocalAdminInfo(context, workTargetData.getBLDG_NO());
                        localAdminInfo.show();
                        localAdminInfo.setOnDismissListener(this);
                    }

                }


                break;
            case R.id.btn_wt_jobComplete:
                // 작업완료 등록
                boolean jobStatus = true;
                for (int i = 0; i < workStatusData.size(); i++) {
                    if (!workStatusData.get(i).getJOB_ST().equals("39")) {
                        jobStatus = false;
                        break;
                    }
                }

                if (jobStatus) {

                    if (!onPopUp) {
                        onPopUp = true;
                        jobComplete();
                    }
                } else {
                    Toast.makeText(context, "모든 점검항목을 완료해주세요", 2000).show();
                }

                break;

            //점검완료 후 활성화.. 결과전송버튼
            //점검완료가 되면 39로 완료 칠게 아니라...결과전송도 되야지 완료목록으로 이동할 수 있도록 수정해야 함
            case R.id.btn_wt_sendData:
                sendMngData();
                break;

            case R.id.radioButtonSingleInsp:
                //singleInspectorClick();
                AlertView.showAlert("1인점검으로 변경할 수 없습니다. \n 정기점검은 2인점검이 기본입니다.", context);
                break;

            case R.id.radioButtonDoubleInsp:
                doubleInspectorClick();
                break;

            case R.id.linear_wt_mainInspector:

                if (radioGroupInspector.getCheckedRadioButtonId() == R.id.radioButtonDoubleInsp)//2인 점검일 경우에만 선택할 수 있게
                {
                    if (isLocked == false) {
                        isLocked = true;
                        new InquireInspectorAsync().execute(1);
                    }

                } else {
                    AlertView.showAlert("1인 점검은 주 점검자를 변경할 수 없습니다.", context);
                }

                break;

            case R.id.linear_wt_subInspector:

                if (isLocked == false) {
                    isLocked = true;
                    new InquireInspectorAsync().execute(2);
                }

                break;

        }
    }


    private void singleInspectorClick() {
        InspTyp = "1";
        radioGroupInspector.check(R.id.radioButtonSingleInsp);
        linear_wt_subInspector.setVisibility(View.GONE);
        tv_subInspCode.setText("");
        tv_subInspName.setText("");

        tv_mainInspCode.setText(commonSession.getMngUsrId());
        tv_mainInspName.setText(commonSession.getEmpNm());
        updateInspector();
    }

    private void doubleInspectorClick() {
        InspTyp = "2";
        radioGroupInspector.check(R.id.radioButtonDoubleInsp);
        linear_wt_subInspector.setVisibility(View.VISIBLE);
        updateInspector();
    }

    private void jobComplete() {

        GPSService gs = new GPSService(context);

        if (gs.isGetLocation()) {
            // GPS상태체크
            // GPS가 켜져있지 않으면 작업을 시작할 수 없다.
            SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
                    "작업을 완료하시겠습니까?", new btnClickListener() {
                @Override
                public void onButtonClick() {
                    // TODO Auto-generated method stub
                    //jobAct = JOB_ACT_COMPLETE;

                    try {
                        CallService.startGPSService(context);
                        //new TimeStatusChangeAsync().execute();
                        ///여기 밑에 함수는 머지?????
                        updateJobComplete();

                        selectRoutineCheckTable();
                        upLoadDatabase();
                        //다음호기 이동팝업 삭제 -> '결과전송'버튼 활성화 '점검완료 버튼 비활성화
						/*NextWorkDialog nwd = new NextWorkDialog(context,
								"다음작업은 무엇입니까?", null, activity,
								routineCheckListData);
						nwd.show();*/
                    } catch (Exception ex) {
                        AlertView.showAlert("errMsg = " + ex.toString(), context);
                    }

                    // finish();
                    // new RoutineCheckListAsync().execute();
                }
            });
            ynDialog.show();
            ynDialog.setOnDismissListener(this);

        } else {

            new AlertDialog.Builder(context)
                    .setTitle("위치서비스 동의")
                    .setOnDismissListener(this)
                    .setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
                    .setNeutralButton("이동",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    context.startActivity(new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            }).show();

        }

    }

    private void enableJobCompleteButton() {
        btn_wt_jobComplete.setEnabled(true);
        btn_wt_jobComplete.setAlpha(1);
        btn_wt_sendData.setEnabled(false);
        btn_wt_sendData.setAlpha((float) 0.5);
        //점검완료를 하면 1, 2인점검을 수정할 수 없다.
        radioButtonDoubleInsp.setEnabled(true);
        radioButtonSingleInsp.setEnabled(true);
        linear_wt_mainInspector.setEnabled(true);
        linear_wt_subInspector.setEnabled(true);
    }

    private void enableSendDataButton() {
        btn_wt_jobComplete.setEnabled(false);
        btn_wt_jobComplete.setAlpha((float) 0.5);
        btn_wt_sendData.setEnabled(true);
        btn_wt_sendData.setAlpha(1);

        //점검완료를 하면 1, 2인점검을 수정할 수 없다.
        radioButtonDoubleInsp.setEnabled(false);
        radioButtonSingleInsp.setEnabled(false);
        linear_wt_mainInspector.setEnabled(false);
        linear_wt_subInspector.setEnabled(false);
        jobAct = JOB_ACT_COMPLETE;
    }

    /**
     * 주/부점검자 변경
     */
    private void updateInspector() {
        isDataChange = true;
        String query = new DatabaseRawQuery().updateInspector(InspTyp,
                tv_mainInspCode.getText().toString(),
                tv_mainInspName.getText().toString(),
                tv_subInspCode.getText().toString(),
                tv_subInspName.getText().toString(),
                commonSession.getEmpId(), workDt, jobNo);

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    ///작업완료
    private void updateJobComplete() {
        isDataChange = true;
        String query = new DatabaseRawQuery().updateWorkComplete(
                commonSession.getEmpId(), workDt, jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    ///점검항목 파트별 점검시작
    private void jobPartStart() {
        isDataChange = true;
        String query = new DatabaseRawQuery().updateStartPartCheck(
                commonSession.getEmpId(), workDt, jobNo, nfcPlc, DateUtil.nowDateFormat("HH:mm"),
                "31");

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();

    }

    private void timeRegistration(String action) {
        isDataChange = true;
        String dMsg = "";

        if (action.equals(JOB_ACT_MOVE)) {
            dMsg = "출발 하시겠습니까?";
            jobAct = JOB_ACT_MOVE;
        } else if (action.equals(JOB_ACT_ARRIVE)) {
            dMsg = "도착하였습니까?";
            jobAct = JOB_ACT_ARRIVE;
        }

        GPSService gs = new GPSService(context);

        if (gs.isGetLocation()) {
            //GPS상태체크
            //GPS가 켜져있지 않으면 작업을 시작할 수 없다.
            SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, dMsg,
                    new btnClickListener() {

                        @Override
                        public void onButtonClick() {
                            // TODO Auto-generated method stub
                            String query = "";
                            if (jobAct.equals(JOB_ACT_MOVE)) {
                                CallService.startTimerService(context);
                                if (NetworkStates.isNetworkStatus(context)) {
                                    new TimeStatusChangeAsync().execute();
                                } else {
                                    query = new DatabaseRawQuery().updateMoveTime(commonSession.getEmpId(), workDt,
                                            jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));

                                    KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                                            DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.execSQL(query);

                                    db.close();
                                    dbHelper.close();
                                    localDetailSet();
                                }

//								 query =new DatabaseRawQuery().updateMoveTime(commonSession.getEmpId(), workDt,
//										 jobNo, DateUtil.nowDateTime(),DateUtil.nowDateFormat("HH:mm"));

                            } else if (jobAct.equals(JOB_ACT_ARRIVE)) {
//								query =new DatabaseRawQuery().updateArriveTime
//										(commonSession.getEmpId(), workDt, jobNo,
//												DateUtil.nowDateTime(),DateUtil.nowDateFormat("HH:mm"));
                                CallService.stopTimerService(context);
                                CallService.startGPSService(context);
                                if (NetworkStates.isNetworkStatus(context)) {
                                    new TimeStatusChangeAsync().execute();
                                } else {
                                    query = new DatabaseRawQuery().updateArriveTime(commonSession.getEmpId(), workDt,
                                            jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));

                                    KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                                            DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.execSQL(query);

                                    db.close();
                                    dbHelper.close();
                                    localDetailSet();
                                }
                            }


//							KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
//									DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
//							SQLiteDatabase db = dbHelper.getWritableDatabase();
//							db.execSQL(query);
//
//							db.close();
//							dbHelper.close();
//							localDetailSet();
//							new TimeStatusChangeAsync().execute("bagicWorkTime");

                        }
                    });
            ynDialog.show();
            ynDialog.setOnDismissListener(this);

        } else {

            new AlertDialog.Builder(context)
                    .setTitle("위치서비스 동의")
                    .setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
                    .setNeutralButton("이동",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    context.startActivity(new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setOnDismissListener(this)
                    .show();
        }

    }


    private void nfcTagEvent(String ndefMessage) {
        // TODO Auto-generated method stub


        WT_RI00_R01_ITEM02 nfcItem = NfcPlcSelect.selectNfcTagId(context, tagUid, jobNo);

        if (ndefMessage != null && ApplicationInfo.isDebugMode() && ndefMessage.length() > 10) {
            String nfcPlc = ndefMessage.substring(ndefMessage.length() - 2);
            Log.e("nfcPlc no", "nfcPlc = " + nfcPlc);
            String carNo = ndefMessage.substring(11, 20);
            int indexOOf = ndefMessage.indexOf("&&&");
            Log.w("carNo no", "carNo = " + carNo);
            Log.e("indexOOf no", "indexOOf = " + indexOOf);
            nfcItem = NfcPlcSelect.selectNfcTagIdDebug(context, tagUid, jobNo, nfcPlc, carNo);
        }

        if (tv_wt_moveTm.getText().toString().equals("")) {
            if (!onPopUp) {
                onPopUp = true;
                timeRegistration(JOB_ACT_MOVE);
            }
        } else {


            if (nfcItem.getJOB_NO().equals("")) {
                // d
                alert("현재 작업과 태그한 NFC가 일치하지 않습니다.", context);
            } else {
                nfcPlc = nfcItem.getNFC_PLC();
                if (nfcItem.getJOB_NO().equals(jobNo)) {
                    //&& nfcItem.getNFC_PLC().equals(nfcPlc)) {

                    if (!tv_wt_moveTm.getText().toString().equals("")
                            && tv_wt_arriveTm.getText().toString().equals("")) {
                        if (!onPopUp) {
                            onPopUp = true;
                            timeRegistration(JOB_ACT_ARRIVE);
                        }
                    } else if (!tv_wt_moveTm.getText().toString().equals("")
                            && !tv_wt_arriveTm.getText().toString().equals("")) {

                        boolean jobStatus = true;
                        for (int i = 0; i < workStatusData.size(); i++) {
                            if (!workStatusData.get(i).getJOB_ST().equals("39")) {
                                jobStatus = false;
                                break;
                            }
                        }

                        if (jobStatus) {
                            // 모든점검항목이 완료됐으면 완료등록 팝업
                            if (!onPopUp) {
                                onPopUp = true;
                                jobComplete();
                            }
                        } else {
                            // /출발,도착시간이 등록되어있다면 각파트별 점검을 체크한다.
                            if (!onPopUp) {
                                onPopUp = true;
                                startCheckList();
                            }
                        }
                    }

                }

            }

        }
//		nfcPlc = ndef6Message.substring(ndefMessage.length() - 2);
//		Log.e("nfcPlc no", "nfcPlc = " + nfcPlc);
//		carNo = ndefMessage.substring(11, 20);
//		int indexOOf = ndefMessage.indexOf("&&&");
//		Log.w("carNo no", "carNo = " + carNo);
//		Log.e("indexOOf no", "indexOOf = " + indexOOf);
//		String moveTm = workTargetData.getMOVE_TM();
//		String arriveTm = workTargetData.getARRIVE_TM();
//
//		//if(carNo.equals(ndefMessage.substring(11, 20))){
//		////태그된 nfc의 카넘버와 현재 앱의 카넘버가 일치하면 작업을 실행한다.
//			if (moveTm.equals("")) {
//			// /출발시간이 등록안된경우
//			timeRegistration(JOB_ACT_MOVE);
//		} else if (arriveTm.equals("")) {
//			// /도착시간이 등록안된경우
//			timeRegistration(JOB_ACT_ARRIVE);
//
//		} else {
//			boolean jobStatus = true;
//			for (int i = 0; i < workStatusData.size(); i++) {
//				if (!workStatusData.get(i).getJOB_ST().equals("39")) {
//					jobStatus = false;
//					break;
//				}
//			}
//
//			if (jobStatus) {
//				// 모든점검항목이 완료됐으면 완료등록 팝업
//				jobComplete();
//			} else {
//				// /출발,도착시간이 등록되어있다면 각파트별 점검을 체크한다.
//				startCheckList();
//			}
//
//		}
//			}else{
//			Toast.makeText(context, "현재작업과 태그한 호기정보가 일치하지 않습니다.", 2000).show();
//		}

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        if (ApplicationInfo.isDebugMode()) {
            if (tv_wt_moveTm.getText().toString().equals("")) {
                alert("출발을 먼저 하십시오.", context);
                return;
            }

            if (tv_wt_arriveTm.getText().toString().equals("")) {
                alert("도착을 먼저 하십시오.", context);
                return;
            }

            Cursor cursor = wO_WT00_R01_Adapter01.getCursor();
            nfcPlc = cursor.getString(cursor.getColumnIndex("NFC_PLC"));
            if (!onPopUp) {
                onPopUp = true;
                startCheckList();
            }

        } else {
            //20160722 카주변인 경우에만 버튼과 Tag를 같이해서 할 수 있도록
            if (tv_wt_moveTm.getText().toString().equals("")) {
                alert("출발을 먼저 하십시오.", context);
                return;
            }

            if (tv_wt_arriveTm.getText().toString().equals("")) {
                alert("도착을 먼저 하십시오.", context);
                return;
            }

            Cursor cursor = wO_WT00_R01_Adapter01.getCursor();
            String plc = cursor.getString(cursor.getColumnIndex("NFC_PLC"));

            ///E/L 기계실, E/S 상부 인경우 태그 안되게 한다.
			/*if(plc.equals("11") || plc.equals(("70")))
			{
				return;
			}*/

			// 00 : 미진행, 31 : 진행중, 39 : 완료, 41 : 보류, null : 계획
            String jobStatus = workStatusData.get(position).getJOB_ST();

            //전기식 엘리베이터 11~41 (버튼가능:31, 41)
            //유압식 EL 14~44 (버튼가능: 24, 34, 44)
            //더메이터  15~45 (버튼가능: 15,25,35)
            //E/S 70~90 (버튼가능: 80 ) 20171026 yowonsm  공성윤대리 요청사항
            if (  !jobStatus.equals("31") && !jobStatus.equals("39")      // 진행중 또는 완료 상태가 아닐경우.(진행중, 완료일 경우에는 바로 확인.)
                    && (plc.equals("41") == true || plc.equals("31") == true ||    //전기식 E/L
                    plc.equals("24") == true || plc.equals("34") == true || plc.equals("44") == true ||    //유압식E/L
                    plc.equals("15") == true || plc.equals("25") == true || plc.equals("35") == true ||    //더메이터
                    plc.equals("80") == true) == false )    //에스컬레이터(중간)
            {
                return;
            }


            if (!onPopUp) //카주변인 경우에만 클릭해서 되도록
            {
                this.nfcPlc = plc;
                onPopUp = true;
                startCheckList();
            }
        }

    }

    private String nfcPlcNm = "";

    private void startCheckList() {
        nfcPosition = null;
        for (int i = 0; i < workStatusData.size(); i++) {
            if (nfcPlc.equals(workStatusData.get(i).getNFC_PLC())) {
                nfcPosition = workStatusData.get(i);

                break;
            }
        }

        if (nfcPosition != null) {
            nfcPlcNm = nfcPosition.getNFC_PLC_NM();
            if (nfcPosition.getJOB_ST().equals("39")) {
                SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
                        nfcPosition.getNFC_PLC_NM() + " 점검이 완료되었습니다. 다시 점검 하시겠습니까?",
                        new btnClickListener() {

                            @Override
                            public void onButtonClick() {
                                // TODO Auto-generated method stub
                                jobPartStart();

                                Intent intent = new Intent(
                                        WO_WT00_R01.this, WO_WT00_R02.class);
                                intent.putExtra("nfcPlc", nfcPlc);
                                intent.putExtra("jobNo", jobNo);
                                intent.putExtra("workDt", workDt);
                                intent.putExtra("nfcPlcNm", nfcPlcNm);
                                startActivity(intent);


                                //new StartCheckListAsync().execute();


                            }
                        });
                ynDialog.show();
                ynDialog.setOnDismissListener(this);
            } else {

                GPSService gs = new GPSService(context);

                if (gs.isGetLocation()) {
                    //GPS상태체크
                    //GPS가 켜져있지 않으면 작업을 시작할 수 없다.
                    CallService.startGPSService(context);
                    SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
                            nfcPosition.getNFC_PLC_NM() + " 점검을 시작하시겠습니까?",
                            new btnClickListener() {

                                @Override
                                public void onButtonClick() {
                                    // TODO Auto-generated method stub
                                    //new StartCheckListAsync().execute();
                                    jobPartStart();

                                    Intent intent = new Intent(
                                            WO_WT00_R01.this, WO_WT00_R02.class);
                                    intent.putExtra("nfcPlc", nfcPlc);
                                    intent.putExtra("jobNo", jobNo);
                                    intent.putExtra("workDt", workDt);
                                    intent.putExtra("nfcPlcNm", nfcPlcNm);
                                    startActivity(intent);
                                }
                            });
                    ynDialog.show();
                    ynDialog.setOnDismissListener(this);
                } else {

                    new AlertDialog.Builder(context)
                            .setTitle("위치서비스 동의")
                            .setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
                            .setNeutralButton("이동",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            context.startActivity(new Intent(
                                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                            .setOnDismissListener(this)
                            .show();
                }
            }
        } else {
            Toast.makeText(context, "현재 점검항목과 NFC태그가 일치하지 않습니다.", 2000).show();
            onPopUp = false;
        }


    }

    private class OfficeNfcCheckAsync extends AsyncTask<Void, String, Void> {
        EasyJsonMap ejmMsg, ejmData;

        @Override
        protected void onPostExecute(Void result) {
            WO_WT00_R01.this.ProgressDialog.dismiss();
            try {
                boolean isError = ejmMsg.getValue("errCd").equals("1") ? true : false;
                if (!isError) {

                    String rtn = ejmData.getValue("RTN");
                    if (rtn.equals("0")) {
                        //nfcTagEvent(ndefMsg);
                        alert("등록된 NFC TAG가 없습니다. 태그를 등록하시고 진행하세요.", context);
                    } else if (rtn.equals("1")) {
                        //사무실 태그
                        nfcTagEvent(ndefMsg);
                    } else if (rtn.equals("2")) {
                        //회의실 태그

                    } else if (rtn.equals("3")) {
                        //빌딩테그
                        nfcTagEvent(ndefMsg);
                    }

                } else if (isError) {
                    AlertView.showAlert(ejmMsg.getValue("errMsg"), context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WO_WT00_R01.this.ProgressDialog = android.app.ProgressDialog.show(
                    WO_WT00_R01.this, "조회중", "태그를 조회중입니다.");
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                CommonSession cs = new CommonSession(context);
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl()
                        + "sm/checkUserOfficeAttendance.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));
                arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));
                arguments.add(new BasicNameValuePair("nfcTag", tagUid));
                JSONObject returnJson = getHttp.getPost(param_url, arguments,
                        true);

                try {
                    ejmMsg = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
                    ejmData = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
            }

            return null;
        }

    }


    private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {
        ArrayList<RoutineCheckListData> routineCheckListItem;

        @Override
        protected void onPostExecute(Void result) {
            int index = 0;
            for (int i = 0; i < routineCheckListItem.size(); i++) {
                if (refControlNo.equals(routineCheckListItem.get(i)
                        .getREF_CONTR_NO())) {
                    index = i;
                    break;
                }
            }
            routineCheckListData = routineCheckListItem.get(index);
            new WorkCompleteAsync().execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            routineCheckListItem = new ArrayList<RoutineCheckListData>();
            GetHttp getHttp = new GetHttp();
            String param_url = WebServerInfo.getUrl()
                    + "ip/selectRoutineCheckList.do";

            List<NameValuePair> arguments = new ArrayList<NameValuePair>();

            arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
            arguments.add(new BasicNameValuePair("workDt", workDt));
            JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

            try {
                ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                routineCheckListItem.clear();
                int jsonSize = returnJson.getJSONArray("dataList").length();
                for (int i = 0; i < jsonSize; i++) {
                    routineCheckListItem.add(new RoutineCheckListData(ejl
                            .getValue(i, "WORK_DT"),
                            ejl.getValue(i, "BLDG_NO"), ejl.getValue(i,
                            "BLDG_NM"), ejl.getValue(i, "E_TEXT"), ejl
                            .getValue(i, "I_CNT"), ejl.getValue(i,
                            "T_CNT"), ejl.getValue(i, "REF_CONTR_NO")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private class StartCheckListAsync extends AsyncTask<Void, String, Void> {
        private String retMsg = "";

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(Void result) {
            if (retMsg.equals("1")) {
                Toast.makeText(context, "작업이 시작되었습니다", 1000).show();

                Intent intent = new Intent(
                        WO_WT00_R01.this, WO_WT00_R02.class);
                intent.putExtra("nfcPlc", nfcPlc);
                intent.putExtra("jobNo", jobNo);
                intent.putExtra("workDt", workDt);
                startActivity(intent);
            } else {
                Toast.makeText(context, "작업을 시작할 수 없습니다.", 1000).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl()
                        + "ip/updatePartCheckState.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("selTp", String.valueOf(InspectType.Start.getValue())));
                arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
                arguments.add(new BasicNameValuePair("workDt", workDt));
                arguments.add(new BasicNameValuePair("jobNo", jobNo));
                arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
                arguments.add(new BasicNameValuePair("wkTm", DateUtil.nowDateFormat("HH:mm")));
                arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
                JSONObject returnJson = getHttp.getPost(param_url, arguments,
                        true);

                try {
                    retMsg = returnJson.getString("dataString");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
            }
            return null;

        }
    }

    private void listViewHeightSet(Adapter listAdapter, ListView listView) {
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class WorkCompleteAsync extends AsyncTask<Void, String, Void> {
        private String retMsg = "";

        @Override
        protected void onPostExecute(Void result) {
            if (retMsg.equals("1")) {
                Toast.makeText(context, "작업을 완료등록하였습니다.", 2000).show();
                NextWorkDialog nwd = new NextWorkDialog(context,
                        "다음작업은 무엇입니까?", null, activity, routineCheckListData);
                nwd.show();
                nwd.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        onPopUp = false;
                    }
                });
            } else {
                Toast.makeText(context, "완료등록을 실패하였습니다.", 2000).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                workStatusData = new ArrayList<WO_WT00_R01_ITEM01>();
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl()
                        + "ip/updateWorkComplete.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();

                arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
                arguments.add(new BasicNameValuePair("workDt", workDt));
                arguments.add(new BasicNameValuePair("jobNo", jobNo));
                arguments.add(new BasicNameValuePair("carNo", carNo));
                arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
                JSONObject returnJson = getHttp.getPost(param_url, arguments,
                        true);

                try {
                    retMsg = returnJson.getString("dataString");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
            }
            return null;

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


    private String upQ030, upQ210, upQ213;

    private void upLoadDatabase() {
        // TODO Auto-generated method stub

        StringBuffer sb = updateWorkTable();
        upQ030 = sb.toString();
        StringBuffer sb2 = update210Table();
        upQ210 = sb2.toString();
        StringBuffer sb3 = update213Table();
        upQ213 = sb3.toString();

        new UploadDatabaseAsync().execute();
    }

    StringBuffer updateWorkTable() {

        StringBuffer inputParams = new StringBuffer();
        String query = "SELECT A.CS_EMP_ID, A.WORK_DT, A.JOB_NO, "
                + " A.CS_FR, '', A.JOB_ST, '', A.MOVE_TM, A.ARRIVE_TM, A.COMPLETE_TM, A.CS_EMP_ID, "
                + " B.CS_TP, B.SELCHK_USID, B.SELCHK_USID_NM, B.SUB_SELCHK_USID, B.SUB_SELCHK_USID_NM "
                + " FROM WORK_TBL A "
                + " LEFT JOIN TCSQ030 B ON A.CS_EMP_ID = B.CS_EMP_ID AND A.WORK_DT = B.WORK_DT AND A.JOB_NO = B.JOB_NO "
                + " WHERE A.WORK_DT = '" + workDt + "' AND A.JOB_NO = '" + jobNo + "'";
        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
        mCursor.moveToFirst();
        if (mCursor != null && mCursor.getCount() != 0) {

            do {
                for (int i = 0; i < mCursor.getColumnCount(); i++) {

                    if (i == mCursor.getColumnCount() - 1) {
                        inputParams.append(mCursor.getString(i)).append("|");
                    } else {
                        inputParams.append(mCursor.getString(i)).append("^");
                    }
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        dbHelper.close();
        return inputParams;


    }


    StringBuffer update210Table() {

        StringBuffer inputParams = new StringBuffer();

        String query = "SELECT CS_EMP_ID, WORK_DT, JOB_NO,NFC_PLC,CS_TM_FR, CS_TM_TO,JOB_ST,CS_EMP_ID  FROM TCSQ210 "
                + " WHERE WORK_DT = '" + workDt + "' AND JOB_NO = '" + jobNo + "'";

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query,
                null); // 쿼리 날리고
        mCursor.moveToFirst();

        if (mCursor != null && mCursor.getCount() != 0) {

            do {
                for (int i = 0; i < mCursor.getColumnCount(); i++) {

                    if (i == mCursor.getColumnCount() - 1) {
                        inputParams.append(mCursor.getString(i)).append("|");
                    } else {
                        inputParams.append(mCursor.getString(i)).append("^");
                    }
                }

            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        dbHelper.close();
        return inputParams;

    }

    StringBuffer update213Table() {
        StringBuffer inputParams = new StringBuffer();

        String query = "select CS_EMP_ID,WORK_DT,JOB_NO,NFC_PLC,CS_ITEM_CD,INPUT_TP,"
                + "INPUT_TP1,INPUT_TP3,INPUT_TP7,OVER_MONTH,MONTH_CHK,MONTH_CHK_IF,INPUT_RMK,DEF_VAL_ST,CS_EMP_ID from TCSQ213 "
                + " WHERE WORK_DT = '" + workDt + "' AND JOB_NO = '" + jobNo + "'";

        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query,
                null); // 쿼리 날리고
        mCursor.moveToFirst();

        if (mCursor != null && mCursor.getCount() != 0) {

            do {
                for (int i = 0; i < mCursor.getColumnCount(); i++) {

                    if (i == mCursor.getColumnCount() - 1) {
                        inputParams.append(mCursor.getString(i)).append("|");
                    } else {
                        inputParams.append(mCursor.getString(i)).append("^");
                    }
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        dbHelper.close();
        return inputParams;
    }


    private class UploadDatabaseAsync extends AsyncTask<Void, String, Boolean> {
        private String updateRet = "";
        private EasyJsonMap msgMap;

        private String exceptionMsg;

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            WO_WT00_R01.this.ProgressDialog.dismiss();

            if (result) {
                try {
                    boolean isError = msgMap.getValue("errCd").equals("0") ? true : false;//0이면 정상
                    if (isError) {

                        if (updateRet.equals("1")) {
                            //정상 동기화 정상동장
                            stringInit();

                            Toast.makeText(context, "점검데이터 업로드를 완료하였습니다.", 2000).show();

                            //작업완료 후 점검결과전송 버튼 활성화.... 전에.. 점검데이터 전송이 먼저;;
                            enableSendDataButton();
                        } else {
                            //실패 : 점검항목 생성 X
                            Toast.makeText(activity, "동기화를 실패하였습니다..", 2000).show();

                        }

                    } else {
                        alert(msgMap.getValue("errMsg"), activity);
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
                    alert(exceptionMsg + errMsg, activity);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WO_WT00_R01.this.ProgressDialog = android.app.ProgressDialog.show(
                    activity, "전송중", "점검결과를 전송중 입니다.");
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl()
                        + "ip/dbUploadTBL.do";    //20170922 yowonsm 테스트끝나면 바꿈
                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("upQ030", upQ030));
                arguments.add(new BasicNameValuePair("upQ210", upQ210));
                arguments.add(new BasicNameValuePair("upQ213", upQ213));


                JSONObject returnJson = getHttp.getPost(param_url, arguments,
                        true);

                try {
                    msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
                    updateRet = returnJson.getString("dataString");

                    Log.v("updateRet", "UpdateRet = " + updateRet);


                } catch (JSONException e) {
                    e.printStackTrace();
                    exceptionMsg = e.toString();
                    return false;
                }

            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
                exceptionMsg = ex.toString();
                return false;
            }
            return true;
        }
    }

    private void stringInit() {
        upQ030 = "";
        upQ210 = "";
        upQ213 = "";
    }

    private void updateTable(String workDt, String jobNo) {
        UpdateTable updateTable = new UpdateTable(context);
        updateTable.updateTable("N", workDt, jobNo);

    }


    private boolean isDataChange = false;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (isDataChange) updateTable(workDt, jobNo);
        super.onDestroy();
    }


    //----------------------내비게이션 영역--------------------------------------//
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
            Log.e("isHide", "naviHide = " + isHide);
        } else {
            testnavi.setPadding(0, 0, 0, 0);
            testnavi.setAlpha((float) 1);
            Log.e("isHide", "naviHide = " + naviPref.isHide());
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
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub
        onPopUp = false;
    }

    //----------------------내비게이션 영역--------------------------------------//

    private WO_TS00_R00_ITEM01 item01;

    private class TimeStatusChangeAsync extends AsyncTask<Void, Void, Boolean> {
        GetHttp http = new GetHttp();
        JSONObjectAndException jsonObjectAndException;
        private String exceptionMsg = null;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            WO_WT00_R01.this.ProgressDialog = android.app.ProgressDialog.show(
                    activity, "동기화", "서버와 동기화 중입니다...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // 1. bagicWorkTime

            //서버와 통신에서 데이터 받아오기 트라이캐치
            try {

                String param_url = WebServerInfo.getUrl()
                        + "ip/updateWorkStatusChanged.do";
                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
                arguments.add(new BasicNameValuePair("workDt", workDt));
                arguments.add(new BasicNameValuePair("jobNo", jobNo));
                arguments.add(new BasicNameValuePair("jobAct", jobAct));
                arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
                jsonObjectAndException = http.getPostByRI(param_url, arguments, true);

                if (jsonObjectAndException.getException() != null) {
                    exceptionMsg = jsonObjectAndException.getException().toString();
                    return false;
                }
            } catch (Exception ex) {

                exceptionMsg = ex.toString();
                return false;
            }

            //받아온 데이터가 정상적인지에 try캐치
            try {
                ejm = new EasyJsonMap(jsonObjectAndException.getjSONObj().getJSONObject("dataMap"));
                Log.v("returnJson01", jsonObjectAndException.getjSONObj().toString());
            } catch (JSONException e) {
                e.printStackTrace();
                exceptionMsg = e.toString();
                return false;
            }


            return true;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // 1. bagicWorkTime
            WO_WT00_R01.this.ProgressDialog.dismiss();
            if (result) {
                item01 = null;
                try {
                    item01 = new WO_TS00_R00_ITEM01(ejm.getValue("JOB_NO"),
                            ejm.getValue("JOB_ST_NM"),
                            ejm.getValue("AS_TP"),
                            ejm.getValue("CS_TM_TO"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    item01 = null;
                }


                if (item01 != null) {
                    String query = "";
                    if (jobAct.equals(JOB_ACT_MOVE)) {
                        query = new DatabaseRawQuery().updateMoveTime(commonSession.getEmpId(), workDt,
                                jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));

                        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL(query);

                        db.close();
                        dbHelper.close();
                        localDetailSet();
                    } else if (jobAct.equals(JOB_ACT_ARRIVE)) {
                        query = new DatabaseRawQuery().updateArriveTime
                                (commonSession.getEmpId(), workDt, jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));
                        KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                                DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL(query);
                        db.close();
                        dbHelper.close();
                        localDetailSet();

                    } else if (jobAct.equals(JOB_ACT_COMPLETE)) {
//							workTargetData.setCompleteTm(dateTime);
//							tv_fr_completionTime.setText(dateTime);
                        updateJobComplete();
                        selectRoutineCheckTable();

                        Toast.makeText(context, "작업을 완료등록하였습니다.", 2000)
                                .show();
                        NextWorkDialog nwd = new NextWorkDialog(context,
                                "다음작업은 무엇입니까?", null, activity,
                                routineCheckListData);
                        nwd.show();

                    }

                }
            } else {
                //에러일때
                //로칼 TCSQ에 인서트하고


                String query = "";

                if (jobAct.equals(JOB_ACT_MOVE)) {
                    query = new DatabaseRawQuery().updateMoveTime(commonSession.getEmpId(), workDt,
                            jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));

                    KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                            DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL(query);

                    db.close();
                    dbHelper.close();
                    localDetailSet();
                } else if (jobAct.equals(JOB_ACT_ARRIVE)) {
                    query = new DatabaseRawQuery().updateArriveTime
                            (commonSession.getEmpId(), workDt, jobNo, DateUtil.nowDateTime(), DateUtil.nowDateFormat("HH:mm"));
                    KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
                            DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL(query);
                    db.close();
                    dbHelper.close();
                    localDetailSet();

                } else if (jobAct.equals(JOB_ACT_COMPLETE)) {
//						workTargetData.setCompleteTm(dateTime);
//						tv_fr_completionTime.setText(dateTime);
                    updateJobComplete();
                    selectRoutineCheckTable();

                    Toast.makeText(context, "작업을 완료등록하였습니다.", 2000)
                            .show();
                    NextWorkDialog nwd = new NextWorkDialog(context,
                            "다음작업은 무엇입니까?", null, activity,
                            routineCheckListData);
                    nwd.show();
                }

                if (insertToLocalByTCSQ050(context, workTargetData) > 0) {
                    Toast.makeText(context, "로컬디비에 인서트되었습니다.", 2000);
                    startJobHistoryService();
                }


            }

        }
    }

    private long insertToLocalByTCSQ050(Context context, WO_WT00_R01_ITEM00 workTargetData) {
        DatabaseRawQuery databaseRawQuery = new DatabaseRawQuery();
        return databaseRawQuery.insertTCSQ050(context, workTargetData, jobAct);
    }

    private void startJobHistoryService() {
        Intent intent = new Intent(context, RealTimeJobHistoryService.class);
        startService(intent);
    }

    private List<InspectorByTeam> empList = new ArrayList<InspectorByTeam>();
    private InspectorByTeam_Adapter adapter = null;
    //POP2 :: 공통
    private SimpleDialogDetail p2;
    private boolean isLocked;

    private class InquireInspectorAsync extends AsyncTask<Integer, Void, Void> {

        SimpleDialog simpleDialog = null;
        int inspDiv = 0;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            // 1. bagicWorkTime

            inspDiv = params[0];
            //서버와 통신에서 데이터 받아오기 트라이캐치
            GetHttp getHttp = new GetHttp();
            String param_url = WebServerInfo.getUrl() + "ir/selectEmpByTeam.do";

            List<NameValuePair> arguments = new ArrayList<NameValuePair>();
            arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));

            JSONObject returnJson = getHttp.getPost(param_url, arguments, false);

            try {
                ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));

                empList.clear();
                int jsonSize = returnJson.getJSONArray("dataList").length();
                for (int i = 0; i < jsonSize; i++) {
                    empList.add(new InspectorByTeam(ejl.getValue(i, "MNG_USR_ID")
                            , ejl.getValue(i, "EMP_NO")
                            , ejl.getValue(i, "EMP_NM")
                            , ejl.getValue(i, "DEPT_CD")
                            , ejl.getValue(i, "DEPT_NM")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                isLocked = false;    //점검자 조회가 끝나면 락을푼다.
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                adapter = new InspectorByTeam_Adapter(context, R.layout.ir_es00_r01p_adapter, empList);
                simpleDialog = new SimpleDialog(context, "작업자선택", adapter, new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (inspDiv == 1) {
                            InspectorByTeam insp = empList.get(position);

                            if (insp.getMNG_USR_ID().isEmpty() == true)//승강원ID 있는지 체크
                            {
                                AlertView.showAlert("승강기정보센터 ID가 없는 점검자 입니다. 다른 점검자를 선택해 주세요.", context);
                                return;
                            }
                            if (insp.getMNG_USR_ID().equals(tv_subInspCode.getText().toString())) {
                                AlertView.showAlert("부점검자와 주점검자의 ID가 동일합니다. 다른 점검자를 선택해 주세요.", context);
                                return;
                            }


                            tv_mainInspCode.setText(insp.getMNG_USR_ID());
                            tv_mainInspName.setText(insp.getEMP_NM());

                            updateInspector();
                            simpleDialog.dismiss();
                        } else {
                            InspectorByTeam insp = empList.get(position);
                            if (insp.getMNG_USR_ID().isEmpty() == true) {
                                AlertView.showAlert("승강기정보센터 ID가 없는 점검자 입니다. 다른 점검자를 선택해 주세요.", context);
                                return;
                            }

                            if (insp.getMNG_USR_ID().equals(tv_mainInspCode.getText().toString())) {
                                AlertView.showAlert("주점검자와 부점검자의 ID가 동일합니다. 다른 점검자를 선택해 주세요.", context);
                                return;
                            }
                            tv_subInspCode.setText(insp.getMNG_USR_ID());
                            tv_subInspName.setText(insp.getEMP_NM());

                            updateInspector();
                            simpleDialog.dismiss();
                        }

                    }

                });

                simpleDialog.show();
                simpleDialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        // TODO Auto-generated method stub
                        isLocked = false;    //점검자 조회가 끝나면 락을푼다.
                    }
                });

            } catch (Exception ex) {
                isLocked = false;    //익셉션이 발생해도 푼다
            }

        }
    }

    private boolean isSendData = false;

    private void sendMngData() {
        SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, "점검결과를 승관원으로 전송 하시겠습니까?",
                new btnClickListener() {

                    @Override
                    public void onButtonClick() {
                        // TODO Auto-generated method stub
                        new CreateMngSendDataAsync().execute();
                    }
                });
        ynDialog.show();
        ynDialog.setOnDismissListener(this);
    }


    private class CreateMngSendDataAsync extends AsyncTask<Integer, Void, Void> {

        String rtn = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            WO_WT00_R01.this.ProgressDialog = android.app.ProgressDialog.show(
                    WO_WT00_R01.this, "결과전송", "점검결과 생성중 입니다.");
        }

        @Override
        protected Void doInBackground(Integer... params) {
            // 1. bagicWorkTime

            //서버와 통신에서 데이터 받아오기 트라이캐치
            GetHttp getHttp = new GetHttp();
            String param_url = WebServerInfo.getUrl() + "ip/createMngSendData.do";

            List<NameValuePair> arguments = new ArrayList<NameValuePair>();
            arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
            arguments.add(new BasicNameValuePair("workDt", workDt));
            arguments.add(new BasicNameValuePair("jobNo", jobNo));
            JSONObject returnJson = getHttp.getPost(param_url, arguments, false);

            try {
                ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));


                int jsonSize = returnJson.getJSONArray("dataList").length();
                if (jsonSize > 0) {
                    rtn = ejl.getValue(0, "RTN");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            WO_WT00_R01.this.ProgressDialog.dismiss();
            isSendData = true;
            try {
                if ("1".equals(rtn)) {
                    //결과전송 결과가 1이면 정상성공 마무리
                    //Toast.makeText(context, "점검결과 전송이 완료되었습니다.", Toast.LENGTH_LONG);

                    Intent intent = new Intent(activity, WO_WT00_R04.class);
                    intent.putExtra("empId", commonSession.getEmpId());
                    intent.putExtra("workDt", workDt);
                    intent.putExtra("jobNo", jobNo);

                    activity.startActivity(intent);
                    finish();

                } else {
                    alert(rtn, context);
                }
            } catch (Exception ex) {
                alert(rtn + ex.toString(), context);
            }
        }
    }


    @Override
    public void onBackPressed() {
        this.openDialog();
    }

    private void openDialog() {
        // TODO Auto-generated method stub
        if (jobAct.equals(JOB_ACT_COMPLETE) && !isSendData) {
            AlertView.showAlert("점검결과 전송을 하지 않았습니다. '결과전송'버튼을 눌러주세요.", context);
            return;
        } else {
            finish();
        }

    }

}


