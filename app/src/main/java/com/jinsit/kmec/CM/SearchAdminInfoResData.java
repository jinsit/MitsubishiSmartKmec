package com.jinsit.kmec.CM;

public class SearchAdminInfoResData {
	public String CLIENT_NM;
	public String MOBILE;
	public String MAIL_ADDR;
	public String PHONE;
	public SearchAdminInfoResData(String cLIENT_NM, String mOBILE,
			String mAIL_ADDR, String pHONE) {
		super();
		CLIENT_NM = cLIENT_NM;
		MOBILE = mOBILE;
		MAIL_ADDR = mAIL_ADDR;
		PHONE = pHONE;
	}
	public SearchAdminInfoResData() {
		super();
	}
	public String getCLIENT_NM() {
		return CLIENT_NM;
	}
	public void setCLIENT_NM(String cLIENT_NM) {
		CLIENT_NM = cLIENT_NM;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getMAIL_ADDR() {
		return MAIL_ADDR;
	}
	public void setMAIL_ADDR(String mAIL_ADDR) {
		MAIL_ADDR = mAIL_ADDR;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
}