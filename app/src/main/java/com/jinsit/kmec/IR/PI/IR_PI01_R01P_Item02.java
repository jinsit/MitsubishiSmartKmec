package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI01_R01P_Item02 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9072260562176105699L;
	/**
	 * 
	 */
	public IR_PI01_R01P_Item02() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param sNo
	 * @param carNo
	 * @param itemNm
	 * @param plQty
	 */
	public IR_PI01_R01P_Item02(String sNo, String carNo, String itemNm,
			String plQty) {
		super();
		this.sNo = sNo;
		this.carNo = carNo;
		this.itemNm = itemNm;
		this.plQty = plQty;
	}
	private String sNo;
	private String carNo;
	private String itemNm;
	private String plQty;public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getItemNm() {
		return itemNm;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public String getPlQty() {
		return plQty;
	}
	public void setPlQty(String plQty) {
		this.plQty = plQty;
	}

}
