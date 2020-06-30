package com.jinsit.kmec.WO;

public class WO_WC00_R02P_ITEM01 {

	/**
	 * 
	 */
	public WO_WC00_R02P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param deptCd
	 * @param deptNm
	 */
	public WO_WC00_R02P_ITEM01(String refContrNo, String rmk) {
		super();
		this.refContrNo = refContrNo;
		this.rmk = rmk;
	}

	private String refContrNo;
	private String rmk;

	public String getRefContrNo() {
		return refContrNo;
	}

	public void setRefContrNo(String refContrNo) {
		this.refContrNo = refContrNo;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

}
