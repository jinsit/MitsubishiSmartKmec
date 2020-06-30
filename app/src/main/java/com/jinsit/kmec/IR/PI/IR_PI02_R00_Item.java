package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI02_R00_Item implements Serializable {

	public IR_PI02_R00_Item() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param outNo
	 * @param outDt
	 * @param bldgNm
	 * @param bldgNo
	 * @param partsNo
	 * @param apprDt
	 * @param carNo
	 * @param itemNo
	 * @param itemNm
	 * @param size
	 * @param outQty
	 * @param plQty 출고수량 추가 2015-02-06
	 */
	public IR_PI02_R00_Item(String outNo, String outDt, String bldgNm,
							String bldgNo, String partsNo, String apprDt, String carNo,
							String itemNo, String itemNm, String size, String outQty, String plQty) {
		super();
		this.outNo = outNo;
		this.outDt = outDt;
		this.bldgNm = bldgNm;
		this.bldgNo = bldgNo;
		this.partsNo = partsNo;
		this.apprDt = apprDt;
		this.carNo = carNo;
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.size = size;
		this.outQty = outQty;
		this.plQty = plQty;
	}
	/**
	 *
	 */
	private static final long serialVersionUID = -2379365733195370981L;
	private String outNo;
	private String outDt;
	private String bldgNm;
	private String bldgNo;
	private String partsNo;
	private String apprDt;
	private String carNo;
	private String itemNo;
	private String itemNm;
	private String size;
	private String outQty;
	private String plQty;

	public String getOutNo() {
		return outNo;
	}
	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}
	public String getOutDt() {
		return outDt;
	}
	public void setOutDt(String outDt) {
		this.outDt = outDt;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public String getBldgNo() {
		return bldgNo;
	}
	public void setBldgNo(String bldgNo) {
		this.bldgNo = bldgNo;
	}
	public String getPartsNo() {
		return partsNo;
	}
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	public String getApprDt() {
		return apprDt;
	}
	public void setApprDt(String apprDt) {
		this.apprDt = apprDt;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getOutQty() {
		return outQty;
	}
	public void setOutQty(String outQty) {
		this.outQty = outQty;
	}

	public String getPlQty() {
		return plQty;
	}
	public void setPlQty(String plQty) {
		this.plQty = plQty;
	}
	@Override
	public String toString() {
		return "IR_PI02_R00_Item [outNo=" + outNo + ", outDt=" + outDt
				+ ", bldgNm=" + bldgNm + ", bldgNo=" + bldgNo + ", partsNo="
				+ partsNo + ", apprDt=" + apprDt + ", carNo=" + carNo
				+ ", itemNo=" + itemNo + ", itemNm=" + itemNm + ", size="
				+ size + ", outQty=" + outQty +", plQty=" + plQty + "]";
	}
}
