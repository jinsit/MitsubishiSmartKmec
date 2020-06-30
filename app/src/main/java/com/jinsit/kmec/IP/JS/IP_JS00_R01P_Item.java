package com.jinsit.kmec.IP.JS;

import java.io.Serializable;

public class IP_JS00_R01P_Item implements Serializable {

	private static final long serialVersionUID = -2700385328832823323L;
	private String workNm;
	private String fromToTotalTime;
	private String deptNm;
	private String BldgNm;
	private String carNo;
	
	public IP_JS00_R01P_Item(){};
	public IP_JS00_R01P_Item(  String workNm
							 , String fromToTotalTime
							 , String deptNm
							 , String BldgNm
							 , String carNo) {
		super();
		this.workNm = workNm;
		this.fromToTotalTime  = fromToTotalTime;
		this.deptNm = deptNm;
		this.BldgNm = BldgNm;
		this.carNo = carNo;
	}
	
	public String getWorkNm() {
		return workNm;
	}
	public String getFromToTotalTime() {
		return fromToTotalTime;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public String getBldgNm() {
		return BldgNm;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	public void setFromToTotalTime(String fromToTotalTime) {
		this.fromToTotalTime = fromToTotalTime;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public void setBldgNm(String bldgNm) {
		BldgNm = bldgNm;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	@Override
	public String toString() {
		return "IP_JS00_R01P_Item [workNm=" + workNm + ", fromToTotalTime="
				+ fromToTotalTime + ", deptNm=" + deptNm + ", BldgNm=" + BldgNm
				+ ", carNo=" + carNo + "]";
	}	
};
