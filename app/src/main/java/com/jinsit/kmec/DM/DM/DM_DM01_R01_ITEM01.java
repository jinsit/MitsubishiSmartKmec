package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM01_R01_ITEM01 implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 999437021490866719L;
	
	
	/**
	 * 
	 */
	public DM_DM01_R01_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param workCd
	 * @param workNm
	 */
	public DM_DM01_R01_ITEM01(String workCd, String workNm) {
		super();
		this.workCd = workCd;
		this.workNm = workNm;
	}
	private String workCd;
	private String workNm;


	public String getWorkCd() {
		return workCd;
	}
	public void setWorkCd(String workCd) {
		this.workCd = workCd;
	}
	public String getWorkNm() {
		return workNm;
	}
	public void setWorkNm(String workNm) {
		this.workNm = workNm;
	}
	
	@Override
	public String toString() {
		return "DM_DM01_R01_ITEM01 [workCd=" + workCd + ", workNm=" + workNm
				+ "]";
	}
	
	

}
