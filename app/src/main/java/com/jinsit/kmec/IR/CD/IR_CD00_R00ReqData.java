package com.jinsit.kmec.IR.CD;

import java.io.Serializable;


public class IR_CD00_R00ReqData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String selTp="";
	public String selCd="";
	public String recevDtFr="";
	public String recevDtTo="";
	public String countSt="";
	public String rCount="";
	public String csBldgNo="";
	public String csCarNo="";
	

	public String getCsBldgNo() {
		return csBldgNo;
	}
	public void setCsBldgNo(String csBldgNo) {
		this.csBldgNo = csBldgNo;
	}
	public String getCsCarNo() {
		return csCarNo;
	}
	public void setCsCarNo(String csCarNo) {
		this.csCarNo = csCarNo;
	}
	
	
	public IR_CD00_R00ReqData(String selTp, String selCd, String recevDtFr,
			String recevDtTo, String countSt, String rCount, String csBldgNo,
			String csCarNo) {
		super();
		this.selTp = selTp;
		this.selCd = selCd;
		this.recevDtFr = recevDtFr;
		this.recevDtTo = recevDtTo;
		this.countSt = countSt;
		this.rCount = rCount;
		this.csBldgNo = csBldgNo;
		this.csCarNo = csCarNo;
	}
	public IR_CD00_R00ReqData(String selTp, String selCd) {
		super();
		this.selTp = selTp;
		this.selCd = selCd;
	}
	public IR_CD00_R00ReqData(String selTp, String selCd, String recevDtFr,
			String recevDtTo, String countSt, String rCount) {
		super();
		this.selTp = selTp;
		this.selCd = selCd;
		this.recevDtFr = recevDtFr;
		this.recevDtTo = recevDtTo;
		this.countSt = countSt;
		this.rCount = rCount;
	}
	public String getSelTp() {
		return selTp;
	}
	public void setSelTp(String selTp) {
		this.selTp = selTp;
	}
	public String getSelCd() {
		return selCd;
	}
	public void setSelCd(String selCd) {
		this.selCd = selCd;
	}
	public String getRecevDtFr() {
		return recevDtFr;
	}
	public void setRecevDtFr(String recevDtFr) {
		this.recevDtFr = recevDtFr;
	}
	public String getRecevDtTo() {
		return recevDtTo;
	}
	public void setRecevDtTo(String recevDtTo) {
		this.recevDtTo = recevDtTo;
	}
	public String getCountSt() {
		return countSt;
	}
	public void setCountSt(String countSt) {
		this.countSt = countSt;
	}
	public String getrCount() {
		return rCount;
	}
	public void setrCount(String rCount) {
		this.rCount = rCount;
	}
	
}



