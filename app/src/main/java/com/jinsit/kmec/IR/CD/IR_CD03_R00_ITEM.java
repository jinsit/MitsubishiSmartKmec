package com.jinsit.kmec.IR.CD;

import java.io.Serializable;

public class IR_CD03_R00_ITEM implements Serializable {
	private static final long serialVersionUID = 1L;
	

	public IR_CD03_R00_ITEM() {
		super();
	}

	
	public IR_CD03_R00_ITEM(String rECEV_TM, String oRDER_NM, String bLDG_NO,
			String bLDG_NM, String cAR_NO, String dONG_CAR_NO, String cAR_CD,
			String mODEL_NM, String rECEV_DESC, String mOVE_TM,
			String aRRIVE_TM, String rESERV_TM, String cS_EMP_NM, String pHONE1) {
		super();
		RECEV_TM = rECEV_TM;
		ORDER_NM = oRDER_NM;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		DONG_CAR_NO = dONG_CAR_NO;
		CAR_CD = cAR_CD;
		MODEL_NM = mODEL_NM;
		RECEV_DESC = rECEV_DESC;
		MOVE_TM = mOVE_TM;
		ARRIVE_TM = aRRIVE_TM;
		RESERV_TM = rESERV_TM;
		CS_EMP_NM = cS_EMP_NM;
		PHONE1 = pHONE1;
	}


	public String getRECEV_TM() {
		return RECEV_TM;
	}
	public void setRECEV_TM(String rECEV_TM) {
		RECEV_TM = rECEV_TM;
	}
	public String getORDER_NM() {
		return ORDER_NM;
	}
	public void setORDER_NM(String oRDER_NM) {
		ORDER_NM = oRDER_NM;
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
	public String getRECEV_DESC() {
		return RECEV_DESC;
	}
	public void setRECEV_DESC(String rECEV_DESC) {
		RECEV_DESC = rECEV_DESC;
	}
	public String getMOVE_TM() {
		return MOVE_TM;
	}
	public void setMOVE_TM(String mOVE_TM) {
		MOVE_TM = mOVE_TM;
	}
	public String getARRIVE_TM() {
		return ARRIVE_TM;
	}
	public void setARRIVE_TM(String aRRIVE_TM) {
		ARRIVE_TM = aRRIVE_TM;
	}
	public String getRESERV_TM() {
		return RESERV_TM;
	}
	public void setRESERV_TM(String rESERV_TM) {
		RESERV_TM = rESERV_TM;
	}
	public String getCS_EMP_NM() {
		return CS_EMP_NM;
	}
	public void setCS_EMP_NM(String cS_EMP_NM) {
		CS_EMP_NM = cS_EMP_NM;
	}
	public String getPHONE1() {
		return PHONE1;
	}
	public void setPHONE1(String pHONE1) {
		PHONE1 = pHONE1;
	}


	public String RECEV_TM;
	public String ORDER_NM;
	public String BLDG_NO;
	public String BLDG_NM;
	public String CAR_NO;
	public String DONG_CAR_NO;
	public String CAR_CD;
	//public String DONG_CAR_NO; field equal
	public String MODEL_NM;
	public String RECEV_DESC;
	public String MOVE_TM;
	public String ARRIVE_TM;
	public String RESERV_TM;
	public String CS_EMP_NM;
	public String PHONE1;
	
	

}