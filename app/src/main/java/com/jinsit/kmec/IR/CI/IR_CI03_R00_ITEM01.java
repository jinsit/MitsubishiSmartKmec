package com.jinsit.kmec.IR.CI;

public class IR_CI03_R00_ITEM01 {

	/**
	 * 
	 */
	public IR_CI03_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param workDt
	 * @param st
	 * @param csEmpId
	 * @param jobNo
	 * @param jobST
	 */
	public IR_CI03_R00_ITEM01(String workDt, String st, String csEmpId,
			String jobNo, String jobST) {
		super();
		this.workDt = workDt;
		this.st = st;
		this.csEmpId = csEmpId;
		this.jobNo = jobNo;
		this.jobST = jobST;
	}

	private String workDt;
	private String st;
	private String csEmpId;
	private String jobNo;
	private String jobST;

	public String getWorkDt() {
		return workDt;
	}

	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
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

	public String getJobST() {return jobST;}

	public void setJobST(String jobST) {this.jobST = jobST;}
}
