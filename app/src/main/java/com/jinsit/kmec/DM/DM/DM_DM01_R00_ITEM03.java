package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM01_R00_ITEM03 implements Serializable {
	
	
	/**
	 * 
	 */
	public DM_DM01_R00_ITEM03() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param csEmpId
	 * @param otWorkDt
	 * @param otNo
	 * @param otWorkCd
	 * @param workNm
	 * @param bldgNo
	 * @param bldgNm
	 * @param carNo
	 * @param otTmFr
	 * @param otTmTo
	 * @param otTm
	  * @param repSt
	  * @param repStNm 
	 * @param otRemark
	 */
	public DM_DM01_R00_ITEM03(String csEmpId, String otWorkDt, String otNo,
			String otWorkCd, String workNm, String bldgNo, String bldgNm,
			String carNo, String otTmFr, String otTmTo, String otTm,String repSt,String repStNm,
			String otRemark) {
		super();
		this.csEmpId = csEmpId;
		this.otWorkDt = otWorkDt;
		this.otNo = otNo;
		this.otWorkCd = otWorkCd;
		this.workNm = workNm;
		this.bldgNo = bldgNo;
		this.bldgNm = bldgNm;
		this.carNo = carNo;
		this.otTmFr = otTmFr;
		this.otTmTo = otTmTo;
		this.otTm = otTm;
		this.repSt = repSt;
		this.repStNm = repStNm;
		this.otRemark = otRemark;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3382717576659073386L;
	private String csEmpId;
	private String otWorkDt;
	private String otNo;
	private String otWorkCd;
	private String workNm;
	private String bldgNo;
	private String bldgNm;
	private String carNo;
	private String otTmFr;
	private String otTmTo;
	private String otTm;
	private String repSt;
	private String repStNm;
	private String otRemark;
	
	
	public String getRepSt() {
		return repSt;
	}
	public void setRepSt(String repSt) {
		this.repSt = repSt;
	}
	public String getRepStNm() {
		return repStNm;
	}
	public void setRepStNm(String repStNm) {
		this.repStNm = repStNm;
	}
	public String getCsEmpId() {
		return csEmpId;
	}
	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}
	public String getOtWorkDt() {
		return otWorkDt;
	}
	public void setOtWorkDt(String otWorkDt) {
		this.otWorkDt = otWorkDt;
	}
	public String getOtNo() {
		return otNo;
	}
	public void setOtNo(String otNo) {
		this.otNo = otNo;
	}
	public String getOtWorkCd() {
		return otWorkCd;
	}
	public void setOtWorkCd(String otWorkCd) {
		this.otWorkCd = otWorkCd;
	}
	public String getWorkNm() {
		return workNm;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	public String getBldgNo() {
		return bldgNo;
	}
	public void setBldgNo(String bldgNo) {
		this.bldgNo = bldgNo;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getOtTmFr() {
		return otTmFr;
	}
	public void setOtTmFr(String otTmFr) {
		this.otTmFr = otTmFr;
	}
	public String getOtTmTo() {
		return otTmTo;
	}
	public void setOtTmTo(String otTmTo) {
		this.otTmTo = otTmTo;
	}
	public String getOtTm() {
		return otTm;
	}
	public void setOtTm(String otTm) {
		this.otTm = otTm;
	}
	public String getOtRemark() {
		return otRemark;
	}
	public void setOtRemark(String otRemark) {
		this.otRemark = otRemark;
	}
	@Override
	public String toString() {
		return "DM_DM01_R00_ITEM03 [csEmpId=" + csEmpId + ", otWorkDt="
				+ otWorkDt + ", otNo=" + otNo + ", otWorkCd=" + otWorkCd
				+ ", workNm=" + workNm + ", bldgNo=" + bldgNo + ", bldgNm="
				+ bldgNm + ", carNo=" + carNo + ", otTmFr=" + otTmFr
				+ ", otTmTo=" + otTmTo + ", otTm=" + otTm + ", otRemark="
				+ otRemark + "]";
	}
	
	
	
}
