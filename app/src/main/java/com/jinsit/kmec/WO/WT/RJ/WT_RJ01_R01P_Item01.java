package com.jinsit.kmec.WO.WT.RJ;

import java.io.Serializable;

public class WT_RJ01_R01P_Item01 implements Serializable {

	//Field
	private String carNo;
	private String itemNo;
	private String itemNm;
	private String unitPrc;
	private String qty;
	private String unit;
	private String amt;
	private static final long serialVersionUID = 2707845529114663681L;
	
	//Constructor
	public WT_RJ01_R01P_Item01(String carNo, String itemNo, String itemNm,
			String unitPrc, String qty, String unit, String amt) {
		super();
		this.carNo = carNo;
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.unitPrc = unitPrc;
		this.qty = qty;
		this.unit = unit;
		this.amt = amt;
	}

	//Getter & Setter
	public String getCarNo() {
		return carNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public String getItemNm() {
		return itemNm;
	}

	public String getUnitPrc() {
		return unitPrc;
	}

	public String getQty() {
		return qty;
	}

	public String getUnit() {
		return unit;
	}

	public String getAmt() {
		return amt;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}

	public void setUnitPrc(String unitPrc) {
		this.unitPrc = unitPrc;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}
	
	
	@Override
	public String toString() {
		return "WT_RJ01_R01P_Item01 [carNo=" + carNo + ", itemNo=" + itemNo
				+ ", itemNm=" + itemNm + ", unitPrc=" + unitPrc + ", qty="
				+ qty + ", unit=" + unit + ", amt=" + amt + "]";
	}
	
};