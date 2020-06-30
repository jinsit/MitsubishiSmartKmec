package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI03_R01P_Item01 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3947838729452233241L;
	/**
	 * 
	 */
	public IR_PI03_R01P_Item01() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param itemNo
	 * @param itemNm
	 * @param drawNo
	 * @param qty
	 */
	public IR_PI03_R01P_Item01(String itemNo, String itemNm, String drawNo,
			String qty) {
		super();
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.drawNo = drawNo;
		this.qty = qty;
	}
	
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
	private String itemNo;
	private String itemNm;
	private String drawNo;
	private String qty;
	
	@Override
	public String toString() {
		return "IR_PI03_R01P_Item01 [itemNo=" + itemNo + ", itemNm=" + itemNm
				+ ", drawNo=" + drawNo + ", qty=" + qty + "]";
	}

}
