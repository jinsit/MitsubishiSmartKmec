package com.jinsit.kmec.CM;

public class SearchElevResData {
	public String BLDG_NO;
	public String CAR_NO;
	public String DONG_CAR_NO;
	public String MODEL_NM;
	public SearchElevResData(String bLDG_NO, String cAR_NO, String dONG_CAR_NO,
			String mODEL_NM) {
		super();
		BLDG_NO = bLDG_NO;
		CAR_NO = cAR_NO;
		DONG_CAR_NO = dONG_CAR_NO;
		MODEL_NM = mODEL_NM;
	}
	public String getBLDG_NO() {
		return BLDG_NO;
	}
	public void setBLDG_NO(String bLDG_NO) {
		BLDG_NO = bLDG_NO;
	}
	public SearchElevResData() {
		super();
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
	public String getMODEL_NM() {
		return MODEL_NM;
	}
	public void setMODEL_NM(String mODEL_NM) {
		MODEL_NM = mODEL_NM;
	}
	

}
