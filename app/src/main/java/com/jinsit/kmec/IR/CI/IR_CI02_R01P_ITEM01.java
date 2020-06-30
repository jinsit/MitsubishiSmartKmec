package com.jinsit.kmec.IR.CI;


public class IR_CI02_R01P_ITEM01 {	


	/**
	 * 
	 */
	public IR_CI02_R01P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param carNo
	 * @param modelNm
	 * @param recevDt
	 * @param arriveDt
	 * @param completeDt
	 * @param rescueDt
	 * @param contactCd
	 * @param recevDesc
	 * @param exOrder
	 * @param statusCd
	 * @param cbsCd1
	 * @param cbsCd2
	 * @param cbsCd3
	 * @param faultCd
	 * @param procCd
	 * @param dutyCd
	 * @param orderDesc
	 */
	public IR_CI02_R01P_ITEM01(String carNo, String modelNm, String recevDt,
			String arriveDt, String completeDt, String rescueDt,
			String contactCd, String recevDesc, String exOrder,
			String statusCd, String cbsCd1, String cbsCd2, String cbsCd3,
			String faultCd, String procCd, String dutyCd, String orderDesc,
							   String csEmpNm, String csEmpMobile, String notify, String notifyPhone) {
		super();
		this.carNo = carNo;
		this.modelNm = modelNm;
		this.recevDt = recevDt;
		this.arriveDt = arriveDt;
		this.completeDt = completeDt;
		this.rescueDt = rescueDt;
		this.contactCd = contactCd;
		this.recevDesc = recevDesc;
		this.exOrder = exOrder;
		this.statusCd = statusCd;
		this.cbsCd1 = cbsCd1;
		this.cbsCd2 = cbsCd2;
		this.cbsCd3 = cbsCd3;
		this.faultCd = faultCd;
		this.procCd = procCd;
		this.dutyCd = dutyCd;
		this.orderDesc = orderDesc;
		this.csEmpNm = csEmpNm;
		this.csEmpMobile = csEmpMobile;
		this.notify = notify;
		this.notifyPhone = notifyPhone;
	}

	private String carNo;
	private String modelNm;
	private String recevDt;
	private String arriveDt;
	private String completeDt;
	private String rescueDt;
	private String contactCd;
	private String recevDesc;
	private String exOrder;
	private String statusCd;
	private String cbsCd1;
	private String cbsCd2;
	private String cbsCd3;
	private String faultCd;
	private String procCd;
	private String dutyCd;
	private String orderDesc;
	//고장처리자/연락처 추가 20181130 yowonsm
	private String csEmpNm;
	private String csEmpMobile;
	private String notify;
	private String notifyPhone;

	
	public String getRecevDt() {
		return recevDt;
	}
	public void setRecevDt(String recevDt) {
		this.recevDt = recevDt;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getModelNm() {
		return modelNm;
	}
	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}
	public String getArriveDt() {
		return arriveDt;
	}
	public void setArriveDt(String arriveDt) {
		this.arriveDt = arriveDt;
	}
	public String getCompleteDt() {
		return completeDt;
	}
	public void setCompleteDt(String completeDt) {
		this.completeDt = completeDt;
	}
	public String getRescueDt() {
		return rescueDt;
	}
	public void setRescueDt(String rescueDt) {
		this.rescueDt = rescueDt;
	}
	public String getContactCd() {
		return contactCd;
	}
	public void setContactCd(String contactCd) {
		this.contactCd = contactCd;
	}
	public String getRecevDesc() {
		return recevDesc;
	}
	public void setRecevDesc(String recevDesc) {
		this.recevDesc = recevDesc;
	}
	public String getExOrder() {
		return exOrder;
	}
	public void setExOrder(String exOrder) {
		this.exOrder = exOrder;
	}
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	public String getCbsCd1() {
		return cbsCd1;
	}
	public void setCbsCd1(String cbsCd1) {
		this.cbsCd1 = cbsCd1;
	}
	public String getCbsCd2() {
		return cbsCd2;
	}
	public void setCbsCd2(String cbsCd2) {
		this.cbsCd2 = cbsCd2;
	}
	public String getCbsCd3() {
		return cbsCd3;
	}
	public void setCbsCd3(String cbsCd3) {
		this.cbsCd3 = cbsCd3;
	}
	public String getFaultCd() {
		return faultCd;
	}
	public void setFaultCd(String faultCd) {
		this.faultCd = faultCd;
	}
	public String getProcCd() {
		return procCd;
	}
	public void setProcCd(String procCd) {
		this.procCd = procCd;
	}
	public String getDutyCd() {
		return dutyCd;
	}
	public void setDutyCd(String dutyCd) {
		this.dutyCd = dutyCd;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getCsEmpNm() {
		return csEmpNm;
	}

	public void setCsEmpNm(String csEmpNm) {
		this.csEmpNm = csEmpNm;
	}

	public String getCsEmpMobile() {
		return csEmpMobile;
	}

	public void setCsEmpMobile(String csEmpMobile) {
		this.csEmpMobile = csEmpMobile;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public String getNotifyPhone() {
		return notifyPhone;
	}

	public void setNotifyPhone(String notifyPhone) {
		this.notifyPhone = notifyPhone;
	}
}
