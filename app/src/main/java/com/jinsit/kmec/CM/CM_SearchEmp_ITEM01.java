package com.jinsit.kmec.CM;

import java.io.Serializable;

public class CM_SearchEmp_ITEM01 implements Serializable {

	//Field
	private String deptNm;
	private String csEmpId;
	private String empNm;
	private String phone;
	private String isrtDt;
	private String lastJobTm;
	private String loginDay;
	private String deviceSt;
	private String workNm;
	private String engSt;
	private String nowBldg;
	private String addr;
	private static final long serialVersionUID = -3122252102300677212L;
	
	//Constructor
	public CM_SearchEmp_ITEM01(  String deptNm
							   , String csEmpId
							   , String empNm
							   , String phone
							   , String isrtDt
							   , String lastJobTm
							   , String loginDay
							   , String deviceSt
							   , String workNm
							   , String engSt
							   , String nowBldg
							   , String addr) {
		super();
		this.deptNm    = deptNm;
		this.csEmpId   = csEmpId;
		this.empNm 	   = empNm;
		this.phone 	   = phone;
		this.isrtDt    = isrtDt;
		this.lastJobTm = lastJobTm;
		this.loginDay  = loginDay;
		this.deviceSt  = deviceSt;
		this.workNm    = workNm;
		this.engSt 	   = engSt;
		this.nowBldg   = nowBldg;
		this.addr 	   = addr;
	}	
	

	//Getter & Setter
	public String getDeptNm() {
		return deptNm;
	}
	public String getCsEmpId() {
		return csEmpId;
	}
	public String getEmpNm() {
		return empNm;
	}
	public String getPhone() {
		return phone;
	}
	public String getIsrtDt() {
		return isrtDt;
	}
	public String getLastJobTm() {
		return lastJobTm;
	}
	public String getLoginDay() {
		return loginDay;
	}
	public String getDeviceSt() {
		return deviceSt;
	}
	public String getWorkNm() {
		return workNm;
	}
	public String getEngSt() {
		return engSt;
	}
	public String getNowBldg() {
		return nowBldg;
	}
	public String getAddr() {
		return addr;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setIsrtDt(String isrtDt) {
		this.isrtDt = isrtDt;
	}
	public void setLastJobTm(String lastJobTm) {
		this.lastJobTm = lastJobTm;
	}
	public void setLoginDay(String loginDay) {
		this.loginDay = loginDay;
	}
	public void setDeviceSt(String deviceSt) {
		this.deviceSt = deviceSt;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	public void setEngSt(String engSt) {
		this.engSt = engSt;
	}
	public void setNowBldg(String nowBldg) {
		this.nowBldg = nowBldg;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Override
	public String toString() {
		return "CM_SearchEmp_ITEM01 [deptNm=" + deptNm + ", csEmpId=" + csEmpId
				+ ", empNm=" + empNm + ", phone=" + phone + ", isrtDt="
				+ isrtDt + ", lastJobTm=" + lastJobTm + ", loginDay="
				+ loginDay + ", deviceSt=" + deviceSt + ", workNm=" + workNm
				+ ", engSt=" + engSt + ", nowBldg=" + nowBldg + ", addr="
				+ addr + "]";
	}
	
};