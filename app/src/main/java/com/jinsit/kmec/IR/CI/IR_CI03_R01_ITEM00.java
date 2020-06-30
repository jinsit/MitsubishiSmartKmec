package com.jinsit.kmec.IR.CI;

public class IR_CI03_R01_ITEM00 {

	/**
	 *
	 */
	public IR_CI03_R01_ITEM00() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_CI03_R01_ITEM00(String workDt, String carNo, String csEmpNm, String csEmpId, String jobNo, String tp) {
		this.workDt = workDt;
		this.carNo = carNo;
		this.csEmpNm = csEmpNm;
		this.csEmpId = csEmpId;
		this.jobNo = jobNo;
		this.tp = tp;
	}

	/**
	 * @param workDt
	 * @param st
	 * @param csEmpId
	 * @param jobNo
	 * @param jobST
	 */


	private String workDt;
	private String carNo;
	private String csEmpNm;
	private String csEmpId;
	private String jobNo;
	private String tp;

	public String getWorkDt() {
		return workDt;
	}

	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCsEmpNm() {
		return csEmpNm;
	}

	public void setCsEmpNm(String csEmpNm) {
		this.csEmpNm = csEmpNm;
	}

	public String getCsEmpId() {
		return csEmpId;
	}

	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getTp() {
		return tp;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}
}
