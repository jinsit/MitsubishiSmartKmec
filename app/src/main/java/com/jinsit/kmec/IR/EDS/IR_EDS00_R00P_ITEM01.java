package com.jinsit.kmec.IR.EDS;

public class IR_EDS00_R00P_ITEM01 {
	public String EDU_NM;
	public String APPLY_CAR_CD;
	public String EDU_TO_DT;
	public String SCORE;
	public String NUM;
	public IR_EDS00_R00P_ITEM01(String eDU_NM, String aPPLY_CAR_CD,
			String eDU_TO_DT, String sCORE, String nUM) {
		super();
		EDU_NM = eDU_NM;
		APPLY_CAR_CD = aPPLY_CAR_CD;
		EDU_TO_DT = eDU_TO_DT;
		SCORE = sCORE;
		NUM = nUM;
	}
	public String getEDU_NM() {
		return EDU_NM;
	}
	public void setEDU_NM(String eDU_NM) {
		EDU_NM = eDU_NM;
	}
	public String getAPPLY_CAR_CD() {
		return APPLY_CAR_CD;
	}
	public void setAPPLY_CAR_CD(String aPPLY_CAR_CD) {
		APPLY_CAR_CD = aPPLY_CAR_CD;
	}
	public String getEDU_TO_DT() {
		return EDU_TO_DT;
	}
	public void setEDU_TO_DT(String eDU_TO_DT) {
		EDU_TO_DT = eDU_TO_DT;
	}
	public String getSCORE() {
		return SCORE;
	}
	public void setSCORE(String sCORE) {
		SCORE = sCORE;
	}
	public String getNUM() {
		return NUM;
	}
	public void setNUM(String nUM) {
		NUM = nUM;
	}
	
}
