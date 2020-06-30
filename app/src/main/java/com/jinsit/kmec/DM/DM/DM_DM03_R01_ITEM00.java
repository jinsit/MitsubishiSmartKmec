package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM03_R01_ITEM00 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 509742669794476834L;
	
	private String title;
	private String subCd;
	
	
	/**
	 * 
	 */
	public DM_DM03_R01_ITEM00() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param title
	 * @param cd
	 */
	public DM_DM03_R01_ITEM00(String title, String cd) {
		super();
		this.title = title;
		this.subCd = cd;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCd() {
		return subCd;
	}


	public void setCd(String cd) {
		this.subCd = cd;
	}


	@Override
	public String toString() {
		return "DM_DM03_R00_ITEM01 [title=" + title + ", cd=" + subCd + "]";
	}
	
	
	
}
