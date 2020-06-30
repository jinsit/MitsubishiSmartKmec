package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI03_R01P_Item02 implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8547343788397727539L;
	
	
	/**
	 * 
	 */
	public IR_PI03_R01P_Item02() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	/**
	 * @param stockCd
	 * @param stockNm
	 * @param itemNo
	 * @param itemNm
	 * @param drawNo
	 * @param qty
	 * @param irrQty
	 * @param location
	 */
	public IR_PI03_R01P_Item02(String stockCd, String stockNm, String itemNo,
			String itemNm, String drawNo, String qty, String irrQty, String location) {
		super();
		this.stockCd = stockCd;
		this.stockNm = stockNm;
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.drawNo = drawNo;
		this.qty = qty;
		this.irrQty = irrQty;
		this.location = location;
	}

	private String stockCd;
	private String stockNm;
	private String itemNo;
	private String itemNm;
	private String drawNo;
	private String qty;
	private String irrQty;
	private String location;
	
	public String getStockCd() {
		return stockCd;
	}

	public void setStockCd(String stockCd) {
		this.stockCd = stockCd;
	}

	public String getStockNm() {
		return stockNm;
	}

	public void setStockNm(String stockNm) {
		this.stockNm = stockNm;
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


	public String getIrrQty() {
		return irrQty;
	}

	public void setIrrQty(String irrQty) {
		this.irrQty = irrQty;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "IR_PI03_R01P_Item02 [stockCd=" + stockCd + ", stockNm="
				+ stockNm + ", itemNo=" + itemNo + ", itemNm=" + itemNm
				+ ", drawNo=" + drawNo + ", qty=" + qty + ", irrQty=" + irrQty
				+ ", location=" + location + "]";
	}
}
