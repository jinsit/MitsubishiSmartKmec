package com.jinsit.kmec.CM;

public class SearchDeptResData {
	public String DEPT_CD;
	public String DEPT_NM;
	public String getDEPT_CD() {
		return DEPT_CD;
	}
	public void setDEPT_CD(String dEPT_CD) {
		DEPT_CD = dEPT_CD;
	}
	public String getDEPT_NM() {
		return DEPT_NM;
	}
	public void setDEPT_NM(String dEPT_NM) {
		DEPT_NM = dEPT_NM;
	}
	public SearchDeptResData(String dEPT_CD, String dEPT_NM) {
		super();
		DEPT_CD = dEPT_CD;
		DEPT_NM = dEPT_NM;
	}
	public SearchDeptResData() {
		super();
	}
	
	
}
