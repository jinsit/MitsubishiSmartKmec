package com.jinsit.kmec.IR.ES;

import java.io.Serializable;

public class IR_ES00_R01P_Item implements Serializable {

	private static final long serialVersionUID = -389717341082779544L;
	private String deptNm;
	private String empNm;
	private String workNm;
	private String nowBldg;
	
	public IR_ES00_R01P_Item(){};
	public IR_ES00_R01P_Item(String deptNm, String empNm, String workNm, String nowBldg) {
		super();
		this.deptNm = deptNm;
		this.empNm  = empNm;
		this.workNm = workNm;
		this.nowBldg = nowBldg;
		
	}
	
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getEmpNm() {
		return empNm;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	public String getWorkNm() {
		return workNm;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	
	public String getNowBldg() {
		return nowBldg;
	}
	public void setNowBldg(String nowBldg) {
		this.nowBldg = nowBldg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	@Override
	public String toString() {
		return "IR_ES00_R01P_Item [deptNm=" + deptNm + ", empNm=" + empNm
				+ ", workNm=" + workNm + "]";
	}
	
};
