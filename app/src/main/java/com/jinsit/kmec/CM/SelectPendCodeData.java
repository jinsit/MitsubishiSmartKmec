package com.jinsit.kmec.CM;

public class SelectPendCodeData {
	public String YET_REASON_CD;
	public String YET_REASON_NM;
	public String getYET_REASON_CD() {
		return YET_REASON_CD;
	}
	public void setYET_REASON_CD(String yET_REASON_CD) {
		YET_REASON_CD = yET_REASON_CD;
	}
	public String getYET_REASON_NM() {
		return YET_REASON_NM;
	}
	public void setYET_REASON_NM(String yET_REASON_NM) {
		YET_REASON_NM = yET_REASON_NM;
	}
	public SelectPendCodeData(String yET_REASON_CD, String yET_REASON_NM) {
		super();
		YET_REASON_CD = yET_REASON_CD;
		YET_REASON_NM = yET_REASON_NM;
	}
	
}