package com.jinsit.kmec.IP.IS;

import java.io.Serializable;

public class IP_IS02_R01P_Item01 implements Serializable {

	private static final long serialVersionUID = 2133080562395316000L;
	private String workDt;
	private String bldgNm;
	private String carNo;
	private String workNm;
	private String jobStNm;
	private String csTmFr;
	private String yCnt;
	private String tCnt;
	
	public IP_IS02_R01P_Item01(){};
	public IP_IS02_R01P_Item01(  String workDt
							   , String bldgNm
							   , String carNo
							   , String workNm
							   , String jobStNm
							   , String csTmFr
							   , String yCnt
							   , String tCnt){
		super();
		this.workDt    	= workDt;
		this.bldgNm  	= bldgNm;
		this.carNo 		= carNo;
		this.workNm 	= workNm;
		this.jobStNm 	= jobStNm;
		this.csTmFr 	= csTmFr;
		this.yCnt 		= yCnt;
		this.tCnt		= tCnt;
	}
	
	public String getWorkDt() {
		return workDt;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public String getCarNo() {
		return carNo;
	}
	public String getWorkNm() {
		return workNm;
	}
	public String getJobStNm() {
		return jobStNm;
	}
	public String getCsTmFr() {
		return csTmFr;
	}
	public String getyCnt() {
		return yCnt;
	}
	public String getTCnt() {
		return tCnt;
	}
	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	public void setJobStNm(String jobStNm) {
		this.jobStNm = jobStNm;
	}
	public void setCsTmFr(String csTmFr) {
		this.csTmFr = csTmFr;
	}
	public void setyCnt(String yCnt) {
		this.yCnt = yCnt;
	}
	public void setdCnt(String tCnt) {
		this.tCnt = tCnt;
	}
	
	@Override
	public String toString() {
		return "IP_IS02_R01P_Item01 [workDt=" + workDt + ", bldgNm=" + bldgNm
				+ ", carNo=" + carNo + ", workNm=" + workNm + ", jobStNm="
				+ jobStNm + ", csTmFr=" + csTmFr + ", yCnt=" + yCnt + ", dCnt="
				+ tCnt + "]";
	}
	
};
