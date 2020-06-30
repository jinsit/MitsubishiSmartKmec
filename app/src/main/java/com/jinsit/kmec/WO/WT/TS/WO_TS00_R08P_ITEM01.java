package com.jinsit.kmec.WO.WT.TS;

public class WO_TS00_R08P_ITEM01 {

	/**
	 * 
	 */
	public WO_TS00_R08P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param procCd
	 * @param procNm
	 */
	public WO_TS00_R08P_ITEM01(String procCd, String procNm) {
		super();
		this.procCd = procCd;
		this.procNm = procNm;
	}

	private String procCd;
	private String procNm;

	public String getProcCd() {
		return procCd;
	}

	public void setProcCd(String procCd) {
		this.procCd = procCd;
	}

	public String getProcNm() {
		return procNm;
	}

	public void setProcNm(String procNm) {
		this.procNm = procNm;
	}

}
