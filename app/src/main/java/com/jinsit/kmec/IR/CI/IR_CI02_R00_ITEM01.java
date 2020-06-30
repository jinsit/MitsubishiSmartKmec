package com.jinsit.kmec.IR.CI;


public class IR_CI02_R00_ITEM01 {	

	
	
	/**
	 * 
	 */
	public IR_CI02_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	/**
	 * @param st
	 * @param recevDt
	 * @param orderCd
	 * @param carNo
	 * @param dongCarNo
	 * @param recevNo
	 * @param exOrderCd
	 * @param statusCd
	 */
	public IR_CI02_R00_ITEM01(String st, String recevDt, String orderCd,
			String carNo, String dongCarNo, String recevNo, String exOrderCd,
			String statusCd) {
		super();
		this.st = st;
		this.recevDt = recevDt;
		this.orderCd = orderCd;
		this.carNo = carNo;
		this.dongCarNo = dongCarNo;
		this.recevNo = recevNo;
		this.exOrderCd = exOrderCd;
		this.statusCd = statusCd;
	}




	private String st;
	private String recevDt;
	private String orderCd;
	private String carNo;
	private String dongCarNo;
	private String recevNo;
	private String exOrderCd;
	private String statusCd;
	public String getSt() {
		return st;
	}




	public void setSt(String st) {
		this.st = st;
	}




	public String getRecevDt() {
		return recevDt;
	}




	public void setRecevDt(String recevDt) {
		this.recevDt = recevDt;
	}




	public String getOrderCd() {
		return orderCd;
	}




	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}




	public String getCarNo() {
		return carNo;
	}




	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}




	public String getDongCarNo() {
		return dongCarNo;
	}




	public void setDongCarNo(String dongCarNo) {
		this.dongCarNo = dongCarNo;
	}




	public String getRecevNo() {
		return recevNo;
	}




	public void setRecevNo(String recevNo) {
		this.recevNo = recevNo;
	}




	public String getExOrderCd() {
		return exOrderCd;
	}




	public void setExOrderCd(String exOrderCd) {
		this.exOrderCd = exOrderCd;
	}




	public String getStatusCd() {
		return statusCd;
	}




	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}




	@Override
	public String toString() {
		return "IR_CI02_R00_ITEM01 [st=" + st + ", recevDt=" + recevDt
				+ ", orderCd=" + orderCd + ", carNo=" + carNo + ", dongCarNo="
				+ dongCarNo + ", recevNo=" + recevNo + ", exOrderCd="
				+ exOrderCd + ", statusCd=" + statusCd + "]";
	}



	

	
	
}
