package com.jinsit.kmec.CM;

public class SearchBldgResData {
	public String BLDG_NO;
	public String BLDG_NM;
	public String ADDR;
	public String CS_DEPT_NM;
	
	public SearchBldgResData() {
		super();
	}
	public SearchBldgResData(String bLDG_NO, String bLDG_NM, String aDDR,
			String cS_DEPT_NM) {
		super();
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		ADDR = aDDR;
		CS_DEPT_NM = cS_DEPT_NM;
	}
	public String getCS_DEPT_NM() {
		return CS_DEPT_NM;
	}
	public void setCS_DEPT_NM(String cS_DEPT_NM) {
		CS_DEPT_NM = cS_DEPT_NM;
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
	public String getADDR() {
		return ADDR;
	}
	public void setADDR(String aDDR) {
		ADDR = aDDR;
	}
}
