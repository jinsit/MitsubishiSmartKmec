package com.jinsit.kmec.IR.TI;

import java.io.Serializable;

public class IR_TI02_R00_ITEM01 implements Serializable {	

	

	/**
	 * 
	 */
	public IR_TI02_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	



	/**
	 * @param docuHCd
	 * @param docuMCd
	 * @param docuLCd
	 * @param docuLNm
	 * @param docuCd
	 * @param fileNm
	 */
	public IR_TI02_R00_ITEM01(String docuHCd, String docuMCd, String docuLCd,
			String docuLNm, String docuCd, String fileNm) {
		super();
		this.docuHCd = docuHCd;
		this.docuMCd = docuMCd;
		this.docuLCd = docuLCd;
		this.docuLNm = docuLNm;
		this.docuCd = docuCd;
		this.fileNm = fileNm;
	}




	private String docuHCd;
	private String docuMCd;
	private String docuLCd;
	private String docuLNm;
	private String docuCd;
	private String fileNm;
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




	public String getDocuLCd() {
		return docuLCd;
	}




	public void setDocuLCd(String docuLCd) {
		this.docuLCd = docuLCd;
	}




	public String getDocuLNm() {
		return docuLNm;
	}




	public void setDocuLNm(String docuLNm) {
		this.docuLNm = docuLNm;
	}




	public String getDocuCd() {
		return docuCd;
	}




	public void setDocuCd(String docuCd) {
		this.docuCd = docuCd;
	}




	public String getFileNm() {
		return fileNm;
	}

	public String getFileName() {

		String fileName  = "";
		try{
			fileName = this.fileNm.substring(0, this.fileNm.indexOf("."));
		}
		catch(Exception ex){
			fileName = "";
		}
		return fileName;
	}



	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}



	
	
}
