package com.jinsit.kmec.WO;

public class WO_WC00_R01P_ITEM01 {
	
	/**
	 * 
	 */
	public WO_WC00_R01P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param deptCd
	 * @param deptNm
	 */
	public WO_WC00_R01P_ITEM01(String deptCd, String deptNm) {
		super();
		this.deptCd = deptCd;
		this.deptNm = deptNm;
	}


	private String deptCd;
	private String deptNm;
	
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	
	
}
