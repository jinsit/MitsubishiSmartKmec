package com.jinsit.kmec.DM.DM;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;

public class DM_DM05_R02P extends Dialog implements View.OnClickListener {
    private Context context;
    private String title;
    // /title 위젯
    private TextView tv01_popTitle;
    private TextView btn_popClose;

    private TextView tv_dm05_r02p_bldgNm;
    private TextView btn_dm05_r02p_bldgNm;
    private TextView tv_dm05_r02p_carNo;
    private TextView btn_dm05_r02p_carNo;
    private TextView btn_dm05_r02p_save;


    public DM_DM05_R02P(Context context, String bldgNm) {
        super(context);
        this.context = context;
        this.title = bldgNm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dm_dm05_r02p);

        activityInit();
    }

    protected void activityInit() {
        tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
        btn_popClose = (TextView) findViewById(R.id.btn_popClose);

        tv_dm05_r02p_bldgNm = (TextView) findViewById(R.id.tv_dm05_r02p_bldgNm);
        btn_dm05_r02p_bldgNm = (TextView) findViewById(R.id.btn_dm05_r02p_bldgNm);
        tv_dm05_r02p_carNo = (TextView) findViewById(R.id.tv_dm05_r02p_carNo);
        btn_dm05_r02p_carNo = (TextView) findViewById(R.id.btn_dm05_r02p_carNo);
        btn_dm05_r02p_save = (TextView) findViewById(R.id.btn_dm05_r02p_save);

        setEvents();
    }

    protected void setEvents() {
        tv01_popTitle.setText(title);

        btn_dm05_r02p_bldgNm.setOnClickListener(this);
        btn_dm05_r02p_carNo.setOnClickListener(this);
        btn_dm05_r02p_save.setOnClickListener(this);
        btn_popClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dm05_r02p_bldgNm:
                searchBldg();
                break;
            case R.id.btn_dm05_r02p_carNo:
                searchElev();
                break;
            case R.id.btn_dm05_r02p_save:
                if (this.bldgNo == "" || this.bldgNo == null) {
                    AlertView.showAlert("건물명을 먼저 조회 하세요.", context);
                    return;
                } else {
                    if (this.carNo == "" || this.carNo == null) {
                        AlertView.showAlert("호기명을 입력하셔야 합니다.", context);
                        return;
                    }
                }
                dismiss();
                break;
            case R.id.btn_popClose:
                bldgNo = "";
                bldgNm ="";
                carNo = "";
                dismiss();
                break;
        }
    }

    String bldgNo = "";
    String bldgNm = "";
    String carNo = "";

    private void searchBldg() {
        final CM_SearchBldg sBldg = new CM_SearchBldg(context);
        sBldg.show();
        sBldg.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

                String no = sBldg.getBldgNo();
                if (no.equals("") || no == null) {
                } else {
                    bldgNo = sBldg.getBldgNo();
                    bldgNm = sBldg.getBldgNm();
                    tv_dm05_r02p_bldgNm.setText(sBldg.getBldgNm());
                }
            }

        });
    }


    private void searchElev() {
        final CM_SearchElev elev = new CM_SearchElev(context, bldgNo);
        if (this.bldgNo == "" || this.bldgNo == null) {
            AlertView.showAlert("건물명을 먼저 조회 하세요", context);
            return;
        }
        elev.show();
        elev.elevSearch();
        elev.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                String no = elev.getReturnStr();
                if (no.equals("") || no == null) {

                } else {
                    carNo = elev.getReturnStr();
                    tv_dm05_r02p_carNo.setText(elev.getReturnStr());
                }
            }
        });
    }
}
