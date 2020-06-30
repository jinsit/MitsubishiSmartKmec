package com.jinsit.kmec.WO.WT.TS;

public class WO_TS00_R07P_ITEM01 {

	/**
	 * 
	 */
	public WO_TS00_R07P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param faultCd
	 * @param faultNm
	 */
	public WO_TS00_R07P_ITEM01(String faultCd, String faultNm) {
		super();
		this.faultCd = faultCd;
		this.faultNm = faultNm;
	}

	private String faultCd;
	private String faultNm;

	public String getFaultCd() {
		return faultCd;
	}

	public void setFaultCd(String faultCd) {
		this.faultCd = faultCd;
	}

	public String getFaultNm() {
		return faultNm;
	}

	public void setFaultNm(String faultNm) {
		this.faultNm = faultNm;
	}

}
