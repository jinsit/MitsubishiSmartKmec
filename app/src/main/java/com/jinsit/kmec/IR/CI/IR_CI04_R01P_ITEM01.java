package com.jinsit.kmec.IR.CI;


public class IR_CI04_R01P_ITEM01 {


	/**
	 *
	 */
	public IR_CI04_R01P_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param carNo
	 * @param dongCarNo
	 * @param modelNm
	 * @param itemNm
	 * @param qty
	 */
	public IR_CI04_R01P_ITEM01(String carNo, String dongCarNo, String modelNm,String itemNo,
							   String itemNm, String qty, String compDt, String mngNo) {
		super();
		this.carNo = carNo;
		this.dongCarNo = dongCarNo;
		this.modelNm = modelNm;
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.qty = qty;
		this.compDt = compDt;
		this.mngNo = mngNo;
	}








	private String carNo;
	private String dongCarNo;
	private String modelNm;
	private String itemNo; //itemNo추가 itemNo로 조회해서 부품판가현황같은 팝업을 띄워줘야 해서
	private String itemNm;
	private String qty;
	private String compDt;
	private String mngNo;
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








	public String getModelNm() {
		return modelNm;
	}








	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}








	public String getItemNm() {
		return itemNm;
	}








	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}








	public String getQty() {
		return qty;
	}








	public void setQty(String qty) {
		this.qty = qty;
	}


	public String getItemNo() {
		return itemNo;
	}


	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}


	public String getCompDt() {
		return compDt;
	}

	public void setCompDt(String compDt) {
		this.compDt = compDt;
	}

	public String getMngNo() {
		return mngNo;
	}

	public void setMngNo(String mngNo) {
		this.mngNo = mngNo;
	}
}
