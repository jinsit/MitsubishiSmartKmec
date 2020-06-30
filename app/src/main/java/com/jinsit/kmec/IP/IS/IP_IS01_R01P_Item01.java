package com.jinsit.kmec.IP.IS;

import java.io.Serializable;

public class IP_IS01_R01P_Item01 implements Serializable {

	
	private static final long serialVersionUID = 7291133922076425288L;
	private String carNo;
	private String jobStNm;
	private String planWorkDt;
	private String workDt;
	private String empNm;
	
	public IP_IS01_R01P_Item01(){};
	public IP_IS01_R01P_Item01(  String carNo
							   , String jobStNm
							   , String planWorkDt
							   , String workDt
							   , String empNm){
		super();
		this.carNo    	= carNo;
		this.jobStNm  	= jobStNm;
		this.planWorkDt = planWorkDt;
		this.workDt 	= workDt;
		this.empNm		= empNm;
	}
	
	public String getCarNo() {
		return carNo;
	}
	public String getJobStNm() {
		return jobStNm;
	}
	public String getPlanWorkDt() {
		return planWorkDt;
	}
	public String getWorkDt() {
		return workDt;
	}
	public String getEmpNm() {
		return empNm;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public void setJobStNm(String jobStNm) {
		this.jobStNm = jobStNm;
	}
	public void setPlanWorkDt(String planWorkDt) {
		this.planWorkDt = planWorkDt;
	}
	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	
	@Override
	public String toString() {
		return "IP_IS01_R01P_Item01 [carNo=" + carNo + ", jobStNm=" + jobStNm
				+ ", planWorkDt=" + planWorkDt + ", workDt=" + workDt
				+ ", empNm=" + empNm + "]";
	}
	
	
};
