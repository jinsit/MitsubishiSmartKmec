package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM01_R00_ITEM02 implements Serializable {

	
	/**
	 * 
	 */
	public DM_DM01_R00_ITEM02() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**  selTp 1 
	 * @param csEmpId
	 * @param workDt
	 * @param jobNo
	 * @param workCd
	 * @param bldgNo
	 * @param bldgNm
	 * @param carNo
	 * @param dongCarNo
	 * @param csDtFr
	 * @param csDtTo
	 * @param csTmFr
	 * @param csTmTo
	 * @param csTm
	 */
	public DM_DM01_R00_ITEM02(String csEmpId, String workDt, String jobNo,
			String workCd, String bldgNo, String bldgNm, String carNo,
			String dongCarNo, String csDtFr, String csDtTo, String csTmFr,
			String csTmTo, String csTm) {
		super();
		this.csEmpId = csEmpId;
		this.workDt = workDt;
		this.jobNo = jobNo;
		this.workCd = workCd;
		this.bldgNo = bldgNo;
		this.bldgNm = bldgNm;
		this.carNo = carNo;
		this.dongCarNo = dongCarNo;
		this.csDtFr = csDtFr;
		this.csDtTo = csDtTo;
		this.csTmFr = csTmFr;
		this.csTmTo = csTmTo;
		this.csTm = csTm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -339971214541902030L;	

	private String csEmpId;
	private String workDt;
	private String jobNo;
	private String workCd;
	private String bldgNo;
	private String bldgNm;
	private String carNo;
	private String dongCarNo;
	private String csDtFr;
	private String csDtTo;
	private String csTmFr;
	private String csTmTo;
	private String csTm;
	

	public String getCsDtTo() {
		return csDtTo;
	}
	public void setCsDtTo(String csDtTo) {
		this.csDtTo = csDtTo;
	}
	public String getCsEmpId() {
		return csEmpId;
	}
	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}
	public String getWorkDt() {
		return workDt;
	}
	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getWorkCd() {
		return workCd;
	}
	public void setWorkCd(String workCd) {
		this.workCd = workCd;
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
	public String getDongCarNo() {
		return dongCarNo;
	}
	public void setDongCarNo(String dongCarNo) {
		this.dongCarNo = dongCarNo;
	}
	public String getCsTmFr() {
		return csTmFr;
	}
	public void setCsTmFr(String csTmFr) {
		this.csTmFr = csTmFr;
	}
	public String getCsTmTo() {
		return csTmTo;
	}
	public void setCsTmTo(String csTmTo) {
		this.csTmTo = csTmTo;
	}
	public String getCsTm() {
		return csTm;
	}
	public void setCsTm(String csTm) {
		this.csTm = csTm;
	}
	
	private String getCsDtFr() {
		return csDtFr;
	}
	private void setCsDtFr(String csDtFr) {
		this.csDtFr = csDtFr;
	}
	
	
}
