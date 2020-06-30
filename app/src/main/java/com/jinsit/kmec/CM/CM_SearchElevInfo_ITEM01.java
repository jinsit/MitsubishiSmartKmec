package com.jinsit.kmec.CM;

public class CM_SearchElevInfo_ITEM01 {
	
	
	/**
	 * 
	 */
	public CM_SearchElevInfo_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param carNo
	 * @param dongCarNo
	 * @param carCd
	 * @param modelNm
	 * @param makerCd
	 * @param fstInspRsDt
	 * @param expireDt
	 * @param custPassDt
	 * @param csPassDt
	 */
	public CM_SearchElevInfo_ITEM01(String carNo, String dongCarNo,
			String carCd, String modelNm, String makerCd, String fstInspRsDt,
			String expireDt, String custPassDt, String csPassDt) {
		super();
		this.carNo = carNo;
		this.dongCarNo = dongCarNo;
		this.carCd = carCd;
		this.modelNm = modelNm;
		this.makerCd = makerCd;
		this.fstInspRsDt = fstInspRsDt;
		this.expireDt = expireDt;
		this.custPassDt = custPassDt;
		this.csPassDt = csPassDt;
	}
	private String carNo;
	private String dongCarNo;
	private String carCd;
	private String modelNm;
	private String makerCd;
	private String fstInspRsDt;
	private String expireDt;
	private String custPassDt;
	private String csPassDt;
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getDongCarNo() {
		return dongCarNo;
	}
	public void setDongCarNo(String dongCarNo) {
		this.dongCarNo = dongCarNo;
	}
	public String getCarCd() {
		return carCd;
	}
	public void setCarCd(String carCd) {
		this.carCd = carCd;
	}
	public String getModelNm() {
		return modelNm;
	}
	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}
	public String getMakerCd() {
		return makerCd;
	}
	public void setMakerCd(String makerCd) {
		this.makerCd = makerCd;
	}
	public String getFstInspRsDt() {
		return fstInspRsDt;
	}
	public void setFstInspRsDt(String fstInspRsDt) {
		this.fstInspRsDt = fstInspRsDt;
	}
	public String getExpireDt() {
		return expireDt;
	}
	public void setExpireDt(String expireDt) {
		this.expireDt = expireDt;
	}
	public String getCustPassDt() {
		return custPassDt;
	}
	public void setCustPassDt(String custPassDt) {
		this.custPassDt = custPassDt;
	}
	public String getCsPassDt() {
		return csPassDt;
	}
	public void setCsPassDt(String csPassDt) {
		this.csPassDt = csPassDt;
	}
	
	@Override
	public String toString() {
		return "CM_SearchElevInfo_ITEM01 [carNo=" + carNo + ", dongCarNo="
				+ dongCarNo + ", carCd=" + carCd + ", modelNm=" + modelNm
				+ ", makerCd=" + makerCd + ", fstInspRsDt=" + fstInspRsDt
				+ ", expireDt=" + expireDt + ", custPassDt=" + custPassDt
				+ ", csPassDt=" + csPassDt + "]";
	}
	
	
	
	
	
}
