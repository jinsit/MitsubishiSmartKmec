package com.jinsit.kmec.WO.WT.TS;

public class WO_TS00_R04P_ITEM01 {

	/**
	 * 
	 */
	public WO_TS00_R04P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param assyCd
	 * @param assyNm
	 * @param rmk
	 */
	public WO_TS00_R04P_ITEM01(String assyCd, String assyNm, String rmk) {
		super();
		this.assyCd = assyCd;
		this.assyNm = assyNm;
		this.rmk = rmk;
	}

	private String assyCd;
	private String assyNm;
	private String rmk;

	public String getAssyCd() {
		return assyCd;
	}

	public void setAssyCd(String assyCd) {
		this.assyCd = assyCd;
	}

	public String getAssyNm() {
		return assyNm;
	}

	public void setAssyNm(String assyNm) {
		this.assyNm = assyNm;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

}
