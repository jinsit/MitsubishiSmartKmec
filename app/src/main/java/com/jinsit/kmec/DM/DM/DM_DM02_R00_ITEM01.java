package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM02_R00_ITEM01 implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 999437021490866719L;
	
	
	/**
	 * 
	 */
	public DM_DM02_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param workCd
	 * @param workNm
	 */
	public DM_DM02_R00_ITEM01(String subCd, String title, String ordSq) {
		super();
		this.subCd = subCd;
		this.title = title;
		this.ordSq = ordSq;
	}
	
	private String subCd;
	private String title;
	private String ordSq;


	public String getSubCd() {
		return subCd;
	}
	public void setSubCd(String subCd) {
		this.subCd = subCd;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getOrdSq() {
		return ordSq;
	}
	public void setOrdSq(String ordSq) {
		this.ordSq = ordSq;
	}
	@Override
	public String toString() {
		return "DM_DM02_R00_ITEM01 [subCd=" + subCd + ", title=" + title
				+ ", ordSq=" + ordSq + "]";
	}
	
	

}
