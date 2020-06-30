package com.jinsit.kmec.IR.CD;

import java.io.Serializable;

public class IR_CD02_R00_ITEM01 implements Serializable {
	private static final long serialVersionUID = 1L;


	public String BLDG_NO;
	public String BLDG_NM;
	public String CAR_NO;
	public String DONG_CAR_NO; //아파드 몇동
	public String CAR_CD;
	public String MODEL_NM;
	public String RST_CNT;
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
	public String getRST_CNT() {
		return RST_CNT;
	}
	public void setRST_CNT(String rST_CNT) {
		RST_CNT = rST_CNT;
	}
	public IR_CD02_R00_ITEM01(String bLDG_NO, String bLDG_NM, String cAR_NO,
							  String dONG_CAR_NO, String cAR_CD, String mODEL_NM, String rST_CNT) {
		super();
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		DONG_CAR_NO = dONG_CAR_NO;
		CAR_CD = cAR_CD;
		MODEL_NM = mODEL_NM;
		RST_CNT = rST_CNT;
	}





}