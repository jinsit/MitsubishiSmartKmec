package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI03_R00_Item implements Serializable {


	private static final long serialVersionUID = 6699989808821886054L;
	public IR_PI03_R00_Item() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param itemNo
	 * @param itemNm
	 * @param drawNo
	 */
	public IR_PI03_R00_Item(String itemNo, String itemNm, String drawNo) {
		super();
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.drawNo = drawNo;
	}

	public IR_PI03_R00_Item(String itemNo, String itemNm, String drawNo, String qty, String stockCd) {
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.drawNo = drawNo;
		this.qty = qty;
		this.stockCd = stockCd;
	}

	/**
	 * 
	 */

	private String itemNo;
	private String itemNm;
	private String drawNo;
	private String qty;
	private String stockCd;
	
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemNm() {
		return itemNm;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public String getDrawNo() {
		return drawNo;
	}
	public void setDrawNo(String drawNo) {
		this.drawNo = drawNo;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getStockCd() {
		return stockCd;
	}

	public void setStockCd(String stockCd) {
		this.stockCd = stockCd;
	}

	@Override
	public String toString() {
		return "IR_PI03_R00_Item [itemNo=" + itemNo + ", itemNm=" + itemNm
				+ ", drawNo=" + drawNo + "]";
	}
}
