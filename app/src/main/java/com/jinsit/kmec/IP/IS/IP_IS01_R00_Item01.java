package com.jinsit.kmec.IP.IS;

import java.io.Serializable;

public class IP_IS01_R00_Item01 implements Serializable{

	private static final long serialVersionUID = 239136531648649201L;
	private String ymd;
	private String eleNo;
	private String bldgNm;
	private String jobNo;
	private String empId;

	public IP_IS01_R00_Item01(){};
	public IP_IS01_R00_Item01(  String ymd
							  , String eleNo
							  , String bldgNm
							  , String jobNo
							  , String empId) {
		super();
		this.ymd    = ymd;
		this.eleNo  = eleNo;
		this.bldgNm = bldgNm;
		this.jobNo = jobNo;
		this.empId = empId;
	}
	
	public String getYmd() {
		return ymd;
	}
	public String getEleNo() {
		return eleNo;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public String getJobNo() {
		return jobNo;
	}
	public String getEmpId() { return empId;}
	public void setYmd(String ymd) {
		this.ymd = ymd;
	}
	public void setEleNo(String eleNo) {
		this.eleNo = eleNo;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public  void setJobNo(String jobNo) {this.jobNo = jobNo;}
	public void setEmpId(String empId) {this.empId = empId;}
	@Override
	public String toString() {
		return "IP_IS01_R00_Item01 [ymd=" + ymd + ", eleNo=" + eleNo
				+ ", bldgNm=" + bldgNm +  ", jobNo=" + jobNo + ", empId=" + empId + "]";
	}
	
};
