package com.jinsit.kmec.IR.TI;

import java.io.Serializable;

public class IR_TI01_R00_ITEM01 implements Serializable {	

	

	/**
	 * 
	 */
	public IR_TI01_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	/**
	 * @param docuHCd
	 * @param docuMCd
	 * @param docuMNm
	 */
	public IR_TI01_R00_ITEM01(String docuHCd, String docuMCd, String docuMNm) {
		super();
		this.docuHCd = docuHCd;
		this.docuMCd = docuMCd;
		this.docuMNm = docuMNm;
	}



	private String docuHCd;
	private String docuMCd;
	private String docuMNm;
	public String getDocuHCd() {
		return docuHCd;
	}
	public void setDocuHCd(String docuHCd) {
		this.docuHCd = docuHCd;
	}
	public String getDocuMCd() {
		return docuMCd;
	}
	public void setDocuMCd(String docuMCd) {
		this.docuMCd = docuMCd;
	}
	public String getDocuMNm() {
		return docuMNm;
	}
	public void setDocuMNm(String docuMNm) {
		this.docuMNm = docuMNm;
	}
	



	
	
}
