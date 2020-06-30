package com.jinsit.kmec.IP.IS;

import java.io.Serializable;

public class IP_IS03_R01_Item01 implements Serializable {

	private static final long serialVersionUID = 5384038949122173896L;

	private String workDt;
	private String bldgNm;
	private String jobStNm;
	private String csTmFr;
	private String workNm;
	private String yCnt;
	private String tCnt;
	private String carNo;
	
	public IP_IS03_R01_Item01(String workDt, String bldgNm, String jobStNm,
			String csTmFr, String workNm, String yCnt, String tCnt, String carNo) {
		super();
		this.workDt = workDt;
		this.bldgNm = bldgNm;
		this.jobStNm = jobStNm;
		this.csTmFr = csTmFr;
		this.workNm = workNm;
		this.yCnt = yCnt;
		this.tCnt = tCnt;
		this.carNo = carNo;
	}
	
	public String getWorkDt() {
		return workDt;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public String getJobStNm() {
		return jobStNm;
	}
	public String getCsTmFr() {
		return csTmFr;
	}
	public String getWorkNm() {
		return workNm;
	}
	public String getyCnt() {
		return yCnt;
	}
	public String gettCnt() {
		return tCnt;
	}
	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public void setJobStNm(String jobStNm) {
		this.jobStNm = jobStNm;
	}
	public void setCsTmFr(String csTmFr) {
		this.csTmFr = csTmFr;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	public void setyCnt(String yCnt) {
		this.yCnt = yCnt;
	}
	public void settCnt(String tCnt) {
		this.tCnt = tCnt;
	}
	
	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	@Override
	public String toString() {
		return "IP_IS03_R01_Item01 [workDt=" + workDt + ", bldgNm=" + bldgNm
				+ ", jobStNm=" + jobStNm + ", csTmFr=" + csTmFr + ", workNm="
				+ workNm + ", yCnt=" + yCnt + ", tCnt=" + tCnt + "]";
	}
	
	
}
