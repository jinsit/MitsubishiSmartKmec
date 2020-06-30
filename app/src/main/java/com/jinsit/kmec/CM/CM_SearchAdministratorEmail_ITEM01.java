package com.jinsit.kmec.CM;

public class CM_SearchAdministratorEmail_ITEM01 {


	//,고장수리
	/**
	 *
	 */
	public CM_SearchAdministratorEmail_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * @param gradeNm
	 * @param clientNm
	 * @param phone
	 * @param mailAddr
	 */
	public CM_SearchAdministratorEmail_ITEM01(String gradeNm, String clientNm,
											  String phone, String mailAddr) {
		super();
		this.gradeNm = gradeNm;
		this.clientNm = clientNm;
		this.phone = phone;
		this.mailAddr = mailAddr;
	}



	private String gradeNm;
	private String clientNm;
	private String phone;
	private String mailAddr;

	public String getGradeNm() {
		return gradeNm;
	}



	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
	}



	public String getClientNm() {
		return clientNm;
	}



	public void setClientNm(String clientNm) {
		this.clientNm = clientNm;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getMailAddr() {
		return mailAddr;
	}



	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}




}
