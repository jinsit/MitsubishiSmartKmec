package com.jinsit.kmec.IR.CD;

import java.io.Serializable;
/**
 * 승객갇힘 조회 PassengerLock PL
 * @author Won
 *
 */
public class IR_CD01_R00_ITEM implements Serializable {
	private static final long serialVersionUID = 1L;
	public String RECEV_TM;
	public String TEN;

	public IR_CD01_R00_ITEM() {
		super();
	}

	public IR_CD01_R00_ITEM(String rECEV_TM, String bLDG_NO, String bLDG_NM,
							String cAR_NO, String dONG_CAR_NO, String cAR_CD, String mODEL_NM,
							String cS_DEPT_NM, String eMP_NM_1, String eMP_1_HP,
							String eMP_NM_2, String eMP_2_HP, String aRRIVE_TM,
							String cOMPLETE_TM, String rESCUE_TM, String eX_ORDER_CD,
							String sTATUS_CD, String cBS_CD_1, String cBS_CD_2,
							String cBS_CD_3, String fAULT_CD, String pROC_CD, String dUTY_CD,
							String oRDER_DESC, String tEN) {
		super();
		RECEV_TM = rECEV_TM;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		DONG_CAR_NO = dONG_CAR_NO;
		CAR_CD = cAR_CD;
		MODEL_NM = mODEL_NM;
		CS_DEPT_NM = cS_DEPT_NM;
		EMP_NM_1 = eMP_NM_1;
		EMP_NM_2 = eMP_NM_2;
		EMP_1_HP = eMP_1_HP;
		EMP_2_HP = eMP_2_HP;
		ARRIVE_TM = aRRIVE_TM;
		COMPLETE_TM = cOMPLETE_TM;
		RESCUE_TM = rESCUE_TM;
		EX_ORDER_CD = eX_ORDER_CD;
		STATUS_CD = sTATUS_CD;
		CBS_CD_1 = cBS_CD_1;
		CBS_CD_2 = cBS_CD_2;
		CBS_CD_3 = cBS_CD_3;
		FAULT_CD = fAULT_CD;
		PROC_CD = pROC_CD;
		DUTY_CD = dUTY_CD;
		ORDER_DESC = oRDER_DESC;
		TEN = tEN;
	}
	public String getRECEV_TM() {
		return RECEV_TM;
	}
	public void setRECEV_TM(String rECEV_TM) {
		RECEV_TM = rECEV_TM;
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

	public String getCS_DEPT_NM() {
		return CS_DEPT_NM;
	}

	public void setCS_DEPT_NM(String cS_DEPT_NM) {
		CS_DEPT_NM = cS_DEPT_NM;
	}

	public String getEMP_NM_1() {
		return EMP_NM_1;
	}

	public void setEMP_NM_1(String eMP_NM_1) {
		EMP_NM_1 = eMP_NM_1;
	}

	public String getEMP_2_HP() {
		return EMP_2_HP;
	}

	public void setEMP_2_HP(String eMP_2_HP) {
		EMP_2_HP = eMP_2_HP;
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

	public String getRESCUE_TM() {
		return RESCUE_TM;
	}

	public void setRESCUE_TM(String rESCUE_TM) {
		RESCUE_TM = rESCUE_TM;
	}

	public String getEX_ORDER_CD() {
		return EX_ORDER_CD;
	}

	public void setEX_ORDER_CD(String eX_ORDER_CD) {
		EX_ORDER_CD = eX_ORDER_CD;
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

	public String getORDER_DESC() {
		return ORDER_DESC;
	}

	public void setORDER_DESC(String oRDER_DESC) {
		ORDER_DESC = oRDER_DESC;
	}

	public String BLDG_NO;
	public String getEMP_NM_2() {
		return EMP_NM_2;
	}

	public void setEMP_NM_2(String eMP_NM_2) {
		EMP_NM_2 = eMP_NM_2;
	}

	public String getEMP_1_HP() {
		return EMP_1_HP;
	}

	public void setEMP_1_HP(String eMP_1_HP) {
		EMP_1_HP = eMP_1_HP;
	}

	public String getDONG_CAR_NO() {
		return DONG_CAR_NO;
	}
	public void setDONG_CAR_NO(String dONG_CAR_NO) {
		DONG_CAR_NO = dONG_CAR_NO;
	}


	public String getTEN() {
		return TEN;
	}

	public void setTEN(String tEN) {
		TEN = tEN;
	}

	public String BLDG_NM;
	public String CAR_NO;
	public String DONG_CAR_NO;
	public String CAR_CD;

	public String MODEL_NM;
	public String CS_DEPT_NM;
	public String EMP_NM_1;
	public String EMP_NM_2;
	public String EMP_1_HP;
	public String EMP_2_HP;
	public String ARRIVE_TM;
	public String COMPLETE_TM;
	public String RESCUE_TM;

	// public String 10; <<????
	public String EX_ORDER_CD;
	public String STATUS_CD;
	public String CBS_CD_1;
	public String CBS_CD_2;
	public String CBS_CD_3;
	public String FAULT_CD;
	public String PROC_CD;
	public String DUTY_CD;
	public String ORDER_DESC;

}