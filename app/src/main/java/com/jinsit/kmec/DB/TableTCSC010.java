package com.jinsit.kmec.DB;

/**
 * 5 : LOCAL DB DATA DOWN => 관리자 정보 TCSC010 5 : PROCEDURE [dbo].[SPDC104M];5
 *
 * @author 원성민
 *
 */
public class TableTCSC010 {


	public String BLDG_NO;
	public String CLIENT_ID;
	public String CLIENT_CD;
	public String CLIENT_NM;
	public String MAIL_ADDR;
	public String DEPT_NM;
	public String GRAD_NM;
	public String PHONE;
	public String MOBILE;
	public String FAX;


	public TableTCSC010(String bLDG_NO, String cLIENT_ID, String cLIENT_CD,
						String cLIENT_NM, String mAIL_ADDR, String dEPT_NM, String gRAD_NM,
						String pHONE, String mOBILE, String fAX) {
		super();
		BLDG_NO = bLDG_NO;
		CLIENT_ID = cLIENT_ID;
		CLIENT_CD = cLIENT_CD;
		CLIENT_NM = cLIENT_NM;
		MAIL_ADDR = mAIL_ADDR;
		DEPT_NM = dEPT_NM;
		GRAD_NM = gRAD_NM;
		PHONE = pHONE;
		MOBILE = mOBILE;
		FAX = fAX;
	}


	public String getBLDG_NO() {
		return BLDG_NO;
	}


	public void setBLDG_NO(String bLDG_NO) {
		BLDG_NO = bLDG_NO;
	}


	public String getCLIENT_ID() {
		return CLIENT_ID;
	}


	public void setCLIENT_ID(String cLIENT_ID) {
		CLIENT_ID = cLIENT_ID;
	}


	public String getCLIENT_CD() {
		return CLIENT_CD;
	}


	public void setCLIENT_CD(String cLIENT_CD) {
		CLIENT_CD = cLIENT_CD;
	}


	public String getCLIENT_NM() {
		return CLIENT_NM;
	}


	public void setCLIENT_NM(String cLIENT_NM) {
		CLIENT_NM = cLIENT_NM;
	}


	public String getMAIL_ADDR() {
		return MAIL_ADDR;
	}


	public void setMAIL_ADDR(String mAIL_ADDR) {
		MAIL_ADDR = mAIL_ADDR;
	}


	public String getDEPT_NM() {
		return DEPT_NM;
	}


	public void setDEPT_NM(String dEPT_NM) {
		DEPT_NM = dEPT_NM;
	}


	public String getGRAD_NM() {
		return GRAD_NM;
	}


	public void setGRAD_NM(String gRAD_NM) {
		GRAD_NM = gRAD_NM;
	}


	public String getPHONE() {
		return PHONE;
	}


	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}


	public String getMOBILE() {
		return MOBILE;
	}


	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}


	public String getFAX() {
		return FAX;
	}


	public void setFAX(String fAX) {
		FAX = fAX;
	}


	public TableTCSC010() {
		super();
	}


}