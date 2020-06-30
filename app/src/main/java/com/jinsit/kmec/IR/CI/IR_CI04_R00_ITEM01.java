package com.jinsit.kmec.IR.CI;


public class IR_CI04_R00_ITEM01 {	

	
	
	/**
	 * 
	 */
	public IR_CI04_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	



	/**
	 * @param rsContrNo
	 * @param contrDt
	 * @param rsNm
	 */
	public IR_CI04_R00_ITEM01(String rsContrNo, String contrDt, String rsNm, String carNo, String carNm) {
		super();
		this.rsContrNo = rsContrNo;
		this.contrDt = contrDt;
		this.rsNm = rsNm;
		this.carNo = carNo;
		this.carNm = carNm;
	}




	private String rsContrNo;
	private String contrDt;
	private String rsNm;
	//호기/기종 정보 추가 20181130 yowonsm
	private String carNo;
	private String carNm;

	public String getRsContrNo() {
		return rsContrNo;
	}




	public void setRsContrNo(String rsContrNo) {
		this.rsContrNo = rsContrNo;
	}




	public String getContrDt() {
		return contrDt;
	}




	public void setContrDt(String contrDt) {
		this.contrDt = contrDt;
	}




	public String getRsNm() {
		return rsNm;
	}




	public void setRsNm(String rsNm) {
		this.rsNm = rsNm;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCarNm() {
		return carNm;
	}

	public void setCarNm(String carNm) {
		this.carNm = carNm;
	}
}
