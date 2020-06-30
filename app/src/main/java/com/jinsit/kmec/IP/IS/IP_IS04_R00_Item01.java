package com.jinsit.kmec.IP.IS;

import java.io.Serializable;

public class IP_IS04_R00_Item01 extends IP_IS04_R00 implements Serializable {

    private static final long serialVersionUID = 5384038949122173896L;

    /**
     * @param WORK_DT
     * @param BLDG_NM
     * @param CAR_NO
     * @param ELEVATOR_NO
     * @param SEND_DATE
     * @param SEND_YN_NM
     * @param JOB_NO
     */

    public IP_IS04_R00_Item01(String WORK_DT, String BLDG_NO, String BLDG_NM, String CAR_NO,
                              String ELEVATOR_NO, String SEND_DATE, String SEND_YN, String SEND_YN_NM, String CS_EMP_ID, String JOB_NO) {
        super();
        this.WORK_DT = WORK_DT;
        this.BLDG_NO = BLDG_NO;
        this.BLDG_NM = BLDG_NM;
        this.CAR_NO = CAR_NO;
        this.ELEVATOR_NO = ELEVATOR_NO;
        this.SEND_DATE = SEND_DATE;
        this.SEND_YN_NM = SEND_YN_NM;
        this.SEND_YN = SEND_YN;
        this.CS_EMP_ID = CS_EMP_ID;
        this.JOB_NO = JOB_NO;
    }

    @Override
    public String toString() {
        return "IP_IS04_R00_Item [WORK_DT=" + WORK_DT + ", BLDG_NM=" + BLDG_NM
                + ", CAR_NO=" + CAR_NO + ", ELEVATOR_NO=" + ELEVATOR_NO + ", SEND_DATE=" + SEND_DATE + ", SEND_YN_NM=" + SEND_YN_NM + ", JOB_NO=" + JOB_NO + "]";
    }

    private String WORK_DT;
    private String BLDG_NO;
    private String BLDG_NM;
    private String CAR_NO;
    private String ELEVATOR_NO;
    private String SEND_DATE;
    private String SEND_YN;
    private String SEND_YN_NM;
    private String CS_EMP_ID;
    private String JOB_NO;


    public String getWORK_DT() {
        return WORK_DT;
    }

    public void setWORK_DT(String WORK_DT) {
        this.WORK_DT = WORK_DT;
    }

    public String getBLDG_NO() {
        return BLDG_NO;
    }

    public void setBLDG_NO(String BLDG_NO) {
        this.BLDG_NO = BLDG_NO;
    }

    public String getBLDG_NM() {
        return BLDG_NM;
    }

    public void setBLDG_NM(String BLDG_NM) {
        this.BLDG_NM = BLDG_NM;
    }

    public String getCAR_NO() {
        return CAR_NO;
    }

    public void setCAR_NO(String CAR_NO) {
        this.CAR_NO = CAR_NO;
    }

    public String getELEVATOR_NO() {
        return ELEVATOR_NO;
    }

    public void setELEVATOR_NO(String ELEVATOR_NO) {
        this.ELEVATOR_NO = ELEVATOR_NO;
    }

    public String getSEND_DATE() {
        return SEND_DATE;
    }

    public void setSEND_DATE(String SEND_DATE) {
        this.SEND_DATE = SEND_DATE;
    }

    public String getSEND_YN() {
        return SEND_YN;
    }

    public void setSEND_YN(String SEND_YN) {
        this.SEND_YN = SEND_YN;
    }

    public String getSEND_YN_NM() {
        return SEND_YN_NM;
    }

    public void setSEND_YN_NM(String SEND_YN_NM) {
        this.SEND_YN_NM = SEND_YN_NM;
    }

    public String getCS_EMP_ID() {
        return CS_EMP_ID;
    }

    public void setCS_EMP_ID(String CS_EMP_ID) {
        this.CS_EMP_ID = CS_EMP_ID;
    }

    public String getJOB_NO() {
        return JOB_NO;
    }

    public void setJOB_NO(String JOB_NO) {
        this.JOB_NO = JOB_NO;
    }
}
