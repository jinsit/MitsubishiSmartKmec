package com.jinsit.kmec.IR.CD;

import java.io.Serializable;


/**
 * 다발고장 조회 RepeatFailure RF 1번 ITEM
 * @author Won
 *
 */
public class IR_CD02_R00_ITEM00 implements Serializable {
	private static final long serialVersionUID = 1L;


	public String BLDG_NO;
	public String BLDG_NM;
	public String CAR_NO;
	public String DONG_CAR_NO; //아파드 몇동
	public String CAR_CD;
	public String MODEL_NM;
	public String RECEV_TM;
	public String ARRIVE_TM;
	public String COMPLETE_TM;
	public String RESERV_TM;
	public String CONTACT_CD;
	public String RECEV_DESC;
	public String STATUS_CD;
	public String CBS_CD_1;
	public String CBS_CD_2;
	public String CBS_CD_3;
	public String FAULT_CD;
	public String PROC_CD;
	public String DUTY_CD;
	public String RECEV_NO;

	public IR_CD02_R00_ITEM00()
	{super();
	}
	public IR_CD02_R00_ITEM00(String bLDG_NO, String bLDG_NM, String cAR_NO,
							  String dONG_CAR_NO, String cAR_CD, String mODEL_NM,
							  String rECEV_TM, String aRRIVE_TM, String cOMPLETE_TM,
							  String rESERV_TM, String cONTACT_CD, String rECEV_DESC,
							  String sTATUS_CD, String cBS_CD_1, String cBS_CD_2,
							  String cBS_CD_3, String fAULT_CD, String pROC_CD, String dUTY_CD,
							  String rECEV_NO) {
		super();
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		DONG_CAR_NO = dONG_CAR_NO;
		CAR_CD = cAR_CD;
		MODEL_NM = mODEL_NM;
		RECEV_TM = rECEV_TM;
		ARRIVE_TM = aRRIVE_TM;
		COMPLETE_TM = cOMPLETE_TM;
		RESERV_TM = rESERV_TM;
		CONTACT_CD = cONTACT_CD;
		RECEV_DESC = rECEV_DESC;
		STATUS_CD = sTATUS_CD;
		CBS_CD_1 = cBS_CD_1;
		CBS_CD_2 = cBS_CD_2;
		CBS_CD_3 = cBS_CD_3;
		FAULT_CD = fAULT_CD;
		PROC_CD = pROC_CD;
		DUTY_CD = dUTY_CD;
		RECEV_NO = rECEV_NO;
	}

	public String getBLDG_NO() {
		return BLDG_NO;
	}

	public void setBLDG_NO(String bLDG_NO) {
		BLDG_NO = bLDG_NO;
	}

	public String getBLDG_NM() {
		return BLDG_NM;
	}

	public void setBLDG_NM(String bLDG_NM) {
		BLDG_NM = bLDG_NM;
	}

	public String getCAR_NO() {
		return CAR_NO;
	}

	public void setCAR_NO(String cAR_NO) {
		CAR_NO = cAR_NO;
	}

	public String getDONG_CAR_NO() {
		return DONG_CAR_NO;
	}

	public void setDONG_CAR_NO(String dONG_CAR_NO) {
		DONG_CAR_NO = dONG_CAR_NO;
	}

	public String getCAR_CD() {
		return CAR_CD;
	}

	public void setCAR_CD(String cAR_CD) {
		CAR_CD = cAR_CD;
	}

	public String getMODEL_NM() {
		return MODEL_NM;
	}

	public void setMODEL_NM(String mODEL_NM) {
		MODEL_NM = mODEL_NM;
	}

	public String getRECEV_TM() {
		return RECEV_TM;
	}

	public void setRECEV_TM(String rECEV_TM) {
		RECEV_TM = rECEV_TM;
	}

	public String getARRIVE_TM() {
		return ARRIVE_TM;
	}

	public void setARRIVE_TM(String aRRIVE_TM) {
		ARRIVE_TM = aRRIVE_TM;
	}

	public String getCOMPLETE_TM() {
		return COMPLETE_TM;
	}

	public void setCOMPLETE_TM(String cOMPLETE_TM) {
		COMPLETE_TM = cOMPLETE_TM;
	}

	public String getRESERV_TM() {
		return RESERV_TM;
	}

	public void setRESERV_TM(String rESERV_TM) {
		RESERV_TM = rESERV_TM;
	}

	public String getCONTACT_CD() {
		return CONTACT_CD;
	}

	public void setCONTACT_CD(String cONTACT_CD) {
		CONTACT_CD = cONTACT_CD;
	}

	public String getRECEV_DESC() {
		return RECEV_DESC;
	}

	public void setRECEV_DESC(String rECEV_DESC) {
		RECEV_DESC = rECEV_DESC;
	}

	public String getSTATUS_CD() {
		return STATUS_CD;
	}

	public void setSTATUS_CD(String sTATUS_CD) {
		STATUS_CD = sTATUS_CD;
	}

	public String getCBS_CD_1() {
		return CBS_CD_1;
	}

	public void setCBS_CD_1(String cBS_CD_1) {
		CBS_CD_1 = cBS_CD_1;
	}

	public String getCBS_CD_2() {
		return CBS_CD_2;
	}

	public void setCBS_CD_2(String cBS_CD_2) {
		CBS_CD_2 = cBS_CD_2;
	}

	public String getCBS_CD_3() {
		return CBS_CD_3;
	}

	public void setCBS_CD_3(String cBS_CD_3) {
		CBS_CD_3 = cBS_CD_3;
	}

	public String getFAULT_CD() {
		return FAULT_CD;
	}

	public void setFAULT_CD(String fAULT_CD) {
		FAULT_CD = fAULT_CD;
	}

	public String getPROC_CD() {
		return PROC_CD;
	}

	public void setPROC_CD(String pROC_CD) {
		PROC_CD = pROC_CD;
	}

	public String getDUTY_CD() {
		return DUTY_CD;
	}

	public void setDUTY_CD(String dUTY_CD) {
		DUTY_CD = dUTY_CD;
	}

	public String getRECEV_NO() {
		return RECEV_NO;
	}

	public void setRECEV_NO(String rECEV_NO) {
		RECEV_NO = rECEV_NO;
	}




}