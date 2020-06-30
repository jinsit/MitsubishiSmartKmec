package com.jinsit.kmec.IR.TI;

import java.io.Serializable;

public class IR_TI00_R00_ITEM01 implements Serializable {	

	

	/**
	 * 
	 */
	public IR_TI00_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param docuHCd
	 * @param docuHNm
	 */
	public IR_TI00_R00_ITEM01(String docuHCd, String docuHNm) {
		super();
		this.docuHCd = docuHCd;
		this.docuHNm = docuHNm;
	}


	private String docuHCd;
	private String docuHNm;
	
	public String getDocuHCd() {
		return docuHCd;
	}


	public void setDocuHCd(String docuHCd) {
		this.docuHCd = docuHCd;
	}


	public String getDocuHNm() {
		return docuHNm;
	}


	public void setDocuHNm(String docuHNm) {
		this.docuHNm = docuHNm;
	}




	
	
}
