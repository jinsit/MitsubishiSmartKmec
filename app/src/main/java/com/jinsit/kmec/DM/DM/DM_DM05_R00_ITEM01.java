package com.jinsit.kmec.DM.DM;

public class DM_DM05_R00_ITEM01 {
    public String WORK_DT;
    public String WORK_DAY;
    public String WEEKDAY;
    public String DAY_NM1;
    public String DAY_NM2;
    public String DAY_NM3;
    public String WORK_HR;
    public String REP_NM;
    public DM_DM05_R00_ITEM01(String WORK_DT, String WORK_DAY, String WEEKDAY, String DAY_NM1, String DAY_NM2, String DAY_NM3, String WORK_HR, String REP_NM) {
        super();
        this.WORK_DT = WORK_DT;
        this.WORK_DAY = WORK_DAY;
        this.WEEKDAY = WEEKDAY;
        this.DAY_NM1 = DAY_NM1;
        this.DAY_NM2 = DAY_NM2;
        this.DAY_NM3 = DAY_NM3;
        this.WORK_HR = WORK_HR;
        this.REP_NM = REP_NM;
    }



    public String getWORK_DT() {
        return WORK_DT;
    }

    public void setWORK_DT(String WORK_DT) {
        this.WORK_DT = WORK_DT;
    }

    public String getWORK_DAY() {
        return WORK_DAY;
    }

    public void setWORK_DAY(String WORK_DAY) {
        this.WORK_DAY = WORK_DAY;
    }

    public String getWEEKDAY() {
        return WEEKDAY;
    }

    public void setWEEKDAY(String WEEKDAY) {
        this.WEEKDAY = WEEKDAY;
    }

    public String getDAY_NM1() {
        return DAY_NM1;
    }

    public void setDAY_NM1(String DAY_NM1) {
        this.DAY_NM1 = DAY_NM1;
    }

    public String getDAY_NM2() {
        return DAY_NM2;
    }

    public void setDAY_NM2(String DAY_NM2) {
        this.DAY_NM2 = DAY_NM2;
    }

    public String getDAY_NM3() {
        return DAY_NM3;
    }

    public void setDAY_NM3(String DAY_NM3) {
        this.DAY_NM3 = DAY_NM3;
    }

    public String getWORK_HR() {
        return WORK_HR;
    }

    public void setWORK_HR(String WORK_HR) {
        this.WORK_HR = WORK_HR;
    }

    public String getREP_NM() {
        return REP_NM;
    }

    public void setREP_NM(String REP_NM) {
        this.REP_NM = REP_NM;
    }
}
