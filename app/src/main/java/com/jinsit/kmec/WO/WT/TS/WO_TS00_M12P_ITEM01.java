package com.jinsit.kmec.WO.WT.TS;


public class WO_TS00_M12P_ITEM01 {

	/**
	 * 
	 */
	public WO_TS00_M12P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param empId
	 * @param workDt
	 * @param jobNo
	 * @param contactCd
	 * @param ascCd1
	 * @param ascCd2
	 * @param customer
	 * @param asRmk
	 * @param custSign
	 * @param usrId
	 */
	public WO_TS00_M12P_ITEM01(String empId, String workDt, String jobNo, String ascCd1, String ascCd2, String customer,
			String asRmk, String custSign, String usrId,String custSignData) {
		super();
		this.empId = empId;
		this.workDt = workDt;
		this.jobNo = jobNo;
		this.ascCd1 = ascCd1;
		this.ascCd2 = ascCd2;
		this.customer = customer;
		this.asRmk = asRmk;
		this.custSign = custSign;
		this.usrId = usrId;
		this.custSignData = custSignData;
	}


	private String empId;
	private String workDt;
	private String jobNo;
	private String ascCd1;
	private String ascCd2;
	private String customer;
	private String asRmk;
	private String custSign;
	private String usrId;
	private String custSignData;
	
	
	public String getEmpId() {
		return empId;
	}


	public void setEmpId(String empId) {
		this.empId = empId;
	}


	public String getWorkDt() {
		return workDt;
	}


	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}


	public String getJobNo() {
		return jobNo;
	}


	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}


	public String getAscCd1() {
		return ascCd1;
	}


	public void setAscCd1(String ascCd1) {
		this.ascCd1 = ascCd1;
	}


	public String getAscCd2() {
		return ascCd2;
	}


	public void setAscCd2(String ascCd2) {
		this.ascCd2 = ascCd2;
	}


	public String getCustomer() {
		return customer;
	}


	public void setCustomer(String customer) {
		this.customer = customer;
	}


	public String getAsRmk() {
		return asRmk;
	}


	public void setAsRmk(String asRmk) {
		this.asRmk = asRmk;
	}


	public String getCustSign() {
		return custSign;
	}


	public void setCustSign(String custSign) {
		this.custSign = custSign;
	}
	
	public String getCustSignData() {
		return custSignData;
	}


	public void setCustSignData(String custSignData) {
		this.custSignData = custSignData;
	}
	

	public String getUsrId() {
		return usrId;
	}


	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	
	
	

}
