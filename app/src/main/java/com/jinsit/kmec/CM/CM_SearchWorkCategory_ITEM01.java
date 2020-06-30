package com.jinsit.kmec.CM;

public class CM_SearchWorkCategory_ITEM01 {


	//,고장수리
	/**
	 *
	 */
	public CM_SearchWorkCategory_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * @param workCd
	 * @param workNm
	 * @param check1
	 * @param check2
	 * @param check3
	 * @param check4
	 */
	public CM_SearchWorkCategory_ITEM01(String workCd, String workNm,
										String check1, String check2, String check3, String check4) {
		super();
		this.workCd = workCd;
		this.workNm = workNm;
		this.check1 = check1;
		this.check2 = check2;
		this.check3 = check3;
		this.check4 = check4;
	}



	private String workCd;
	private String workNm;
	private String check1;
	private String check2;
	private String check3;
	private String check4;

	private boolean supportDept;
	private boolean searchBldg;
	private boolean searchElev;
	private boolean searchOrderNo;
	private boolean createWork;


	public boolean isSupportDept() {
		if(this.check1.equals("1")){
			return true;
		}
		return false;
	}

	public boolean isSearchBldg() {
		if(this.check2.equals("1")){
			return true;
		}
		return false;
	}
	public boolean isSearchElev() {
		if(this.check3.equals("1")){
			return true;
		}
		return false;
	}
	public boolean isSearchOrderNo() {
		if(this.check4.equals("1")){
			return true;
		}
		return false;
	}
	public boolean isCreateWork() {
		return true;
	}
	/*
	 * yowonsm 클레임작업생성시 클레임번호를 호기보다 먼저넣어야 해서 따로뺌
	 */
	public boolean isClaimNo(){
		if(this.check4.equals("1") && this.workCd.equals("CA11")){
			return true;
		}
		return false;
	}



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
	public String getCheck1() {
		return check1;
	}
	public void setCheck1(String check1) {
		this.check1 = check1;
	}
	public String getCheck2() {
		return check2;
	}
	public void setCheck2(String check2) {
		this.check2 = check2;
	}
	public String getCheck3() {
		return check3;
	}
	public void setCheck3(String check3) {
		this.check3 = check3;
	}
	public String getCheck4() {
		return check4;
	}
	public void setCheck4(String check4) {
		this.check4 = check4;
	}



}
