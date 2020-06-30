package com.jinsit.kmec.DM.DM;

public class DM_DM05_R01P_Header01 {
    public String CS_EMP_ID;
    public String WORK_DT;
    public String ATTEND_CD;
    public String DAY_DUTY_CODE;
    public String DAY_DUTY_IF;
    public String NIGHT_DUTY_CODE;
    public String NIGHT_DUTY_IF;

    public DM_DM05_R01P_Header01(String CS_EMP_ID, String WORK_DT, String ATTEND_CD, String DAY_DUTY_CODE, String DAY_DUTY_IF, String NIGHT_DUTY_CODE, String NIGHT_DUTY_IF) {
        this.CS_EMP_ID = CS_EMP_ID;
        this.WORK_DT = WORK_DT;
        this.ATTEND_CD = ATTEND_CD;
        this.DAY_DUTY_CODE = DAY_DUTY_CODE;
        this.DAY_DUTY_IF = DAY_DUTY_IF;
        this.NIGHT_DUTY_CODE = NIGHT_DUTY_CODE;
        this.NIGHT_DUTY_IF = NIGHT_DUTY_IF;
    }

    public String getCS_EMP_ID() {
        return CS_EMP_ID;
    }

    public void setCS_EMP_ID(String CS_EMP_ID) {
        this.CS_EMP_ID = CS_EMP_ID;
    }

    public String getWORK_DT() {
        return WORK_DT;
    }

    public void setWORK_DT(String WORK_DT) {
        this.WORK_DT = WORK_DT;
    }

    public String getATTEND_CD() {
        return ATTEND_CD;
    }

    public void setATTEND_CD(String ATTEND_CD) {
        this.ATTEND_CD = ATTEND_CD;
    }

    public String getDAY_DUTY_CODE() {
        return DAY_DUTY_CODE;
    }

    public void setDAY_DUTY_CODE(String DAY_DUTY_CODE) {
        this.DAY_DUTY_CODE = DAY_DUTY_CODE;
    }

    public String getDAY_DUTY_IF() {
        return DAY_DUTY_IF;
    }

    public void setDAY_DUTY_IF(String DAY_DUTY_IF) {
        this.DAY_DUTY_IF = DAY_DUTY_IF;
    }

    public String getNIGHT_DUTY_CODE() {
        return NIGHT_DUTY_CODE;
    }

    public void setNIGHT_DUTY_CODE(String NIGHT_DUTY_CODE) {
        this.NIGHT_DUTY_CODE = NIGHT_DUTY_CODE;
    }

    public String getNIGHT_DUTY_IF() {
        return NIGHT_DUTY_IF;
    }

    public void setNIGHT_DUTY_IF(String NIGHT_DUTY_IF) {
        this.NIGHT_DUTY_IF = NIGHT_DUTY_IF;
    }
}
