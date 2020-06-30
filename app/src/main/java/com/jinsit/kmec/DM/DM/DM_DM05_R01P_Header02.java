package com.jinsit.kmec.DM.DM;

public class DM_DM05_R01P_Header02 {
    public String EMP_ID;
    public String ATTEND_DT;
    public String ATTEND_CD;
    public String REP_ST;
    public String BASE_LIMIT_MI;
    public String BASE_LIMIT_MI_10;
    public String BASE_LIMIT_MI_20;

    public DM_DM05_R01P_Header02(String EMP_ID, String ATTEND_DT, String ATTEND_CD, String REP_ST, String BASE_LIMIT_MI, String BASE_LIMIT_MI_10, String BASE_LIMIT_MI_20) {
        this.EMP_ID = EMP_ID;
        this.ATTEND_DT = ATTEND_DT;
        this.ATTEND_CD = ATTEND_CD;
        this.REP_ST = REP_ST;
        this.BASE_LIMIT_MI = BASE_LIMIT_MI;
        this.BASE_LIMIT_MI_10 = BASE_LIMIT_MI_10;
        this.BASE_LIMIT_MI_20 = BASE_LIMIT_MI_20;
    }

    public String getEMP_ID() {
        return EMP_ID;
    }

    public void setEMP_ID(String EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public String getATTEND_DT() {
        return ATTEND_DT;
    }

    public void setATTEND_DT(String ATTEND_DT) {
        this.ATTEND_DT = ATTEND_DT;
    }

    public String getATTEND_CD() {
        return ATTEND_CD;
    }

    public void setATTEND_CD(String ATTEND_CD) {
        this.ATTEND_CD = ATTEND_CD;
    }

    public String getREP_ST() {
        return REP_ST;
    }

    public void setREP_ST(String REP_ST) {
        this.REP_ST = REP_ST;
    }

    public String getBASE_LIMIT_MI() {
        return BASE_LIMIT_MI;
    }

    public void setBASE_LIMIT_MI(String BASE_LIMIT_MI) {
        this.BASE_LIMIT_MI = BASE_LIMIT_MI;
    }

    public String getBASE_LIMIT_MI_10() {
        return BASE_LIMIT_MI_10;
    }

    public void setBASE_LIMIT_MI_10(String BASE_LIMIT_MI_10) {
        this.BASE_LIMIT_MI_10 = BASE_LIMIT_MI_10;
    }

    public String getBASE_LIMIT_MI_20() {
        return BASE_LIMIT_MI_20;
    }

    public void setBASE_LIMIT_MI_20(String BASE_LIMIT_MI_20) {
        this.BASE_LIMIT_MI_20 = BASE_LIMIT_MI_20;
    }
}
