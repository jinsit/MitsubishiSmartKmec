package com.jinsit.kmec.IR.NT;

import java.io.Serializable;

public class NoticeResponseData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String NOTICE_DT;
	public String NOTICE_TM;
	public String TITLE;
	public String SENDER_NM;
	public String RECIPIENT_NM;
	public String CONTENT;
	public String IMG1;
	public String IMG2;
	public String getNOTICE_DT() {
		return NOTICE_DT;
	}
	public void setNOTICE_DT(String nOTICE_DT) {
		NOTICE_DT = nOTICE_DT;
	}
	public String getNOTICE_TM() {
		return NOTICE_TM;
	}
	public void setNOTICE_TM(String nOTICE_TM) {
		NOTICE_TM = nOTICE_TM;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getSENDER_NM() {
		return SENDER_NM;
	}
	public void setSENDER_NM(String sENDER_NM) {
		SENDER_NM = sENDER_NM;
	}
	public String getRECIPIENT_NM() {
		return RECIPIENT_NM;
	}
	public void setRECIPIENT_NM(String rECIPIENT_NM) {
		RECIPIENT_NM = rECIPIENT_NM;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public String getIMG1() {
		return IMG1;
	}
	public void setIMG1(String iMG1) {
		IMG1 = iMG1;
	}
	public String getIMG2() {
		return IMG2;
	}
	public void setIMG2(String iMG2) {
		IMG2 = iMG2;
	}
	
	public NoticeResponseData() {
		super();
	}
	public NoticeResponseData(String iMG1, String iMG2) {
		super();
		IMG1 = iMG1;
		IMG2 = iMG2;
	}
	public NoticeResponseData(String nOTICE_DT, String nOTICE_TM, String tITLE,
			String sENDER_NM, String rECIPIENT_NM, String cONTENT) {
		super();
		NOTICE_DT = nOTICE_DT;
		NOTICE_TM = nOTICE_TM;
		TITLE = tITLE;
		SENDER_NM = sENDER_NM;
		RECIPIENT_NM = rECIPIENT_NM;
		CONTENT = cONTENT;
	}

}