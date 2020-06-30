package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI04_R00_Item implements Serializable {
	/**
	 * 
	 */
	public IR_PI04_R00_Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param itemNo
	 * @param itemNm
	 * @param size
	 * @param applyDt
	 * @param lifeCycle
	 * @param worker
	 * @param workTm
	 * @param unitPrc
	 * @param buyPrc
	 * @param lt
	 * @param rmk
	 * @param isrtUsrId
	 * @param updtUsrId
	 * @param imgChk
	 */
	public IR_PI04_R00_Item(String itemNo, String itemNm, String size,
			String applyDt, String lifeCycle, String worker, String workTm,
			String unitPrc, String buyPrc, String lt, String rmk,
			String isrtUsrId, String updtUsrId, String imgChk) {
		super();
		this.itemNo = itemNo;
		this.itemNm = itemNm;
		this.size = size;
		this.applyDt = applyDt;
		this.lifeCycle = lifeCycle;
		this.worker = worker;
		this.workTm = workTm;
		this.unitPrc = unitPrc;
		this.buyPrc = buyPrc;
		this.lt = lt;
		this.rmk = rmk;
		this.isrtUsrId = isrtUsrId;
		this.updtUsrId = updtUsrId;
		this.imgChk = imgChk;
	}


	@Override
	public String toString() {
		return "IR_PI04_R00_Item [itemNo=" + itemNo + ", itemNm=" + itemNm
				+ ", size=" + size + ", applyDt=" + applyDt + ", lifeCycle="
				+ lifeCycle + ", worker=" + worker + ", workTm=" + workTm
				+ ", unitPrc=" + unitPrc + ", buyPrc=" + buyPrc + ", lt=" + lt
				+ ", rmk=" + rmk + ", isrtUsrId=" + isrtUsrId + ", updtUsrId="
				+ updtUsrId + ", imgChk=" + imgChk + "]";
	}


	private static final long serialVersionUID = -68615540667429516L;
	private String itemNo;
	private String itemNm;
	private String size;
	private String applyDt;
	private String lifeCycle;
	private String worker;
	private String workTm;
	private String unitPrc;
	private String buyPrc;
	private String lt;
	private String rmk;
	private String isrtUsrId;
	private String updtUsrId;
	private String imgChk;
	
	public String getImgChk() {
		return imgChk;
	}

	public void setImgChk(String imgChk) {
		this.imgChk = imgChk;
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

	public String getApplyDt() {
		return applyDt;
	}

	public void setApplyDt(String applyDt) {
		this.applyDt = applyDt;
	}

	public String getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(String lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getWorkTm() {
		return workTm;
	}

	public void setWorkTm(String workTm) {
		this.workTm = workTm;
	}

	public String getUnitPrc() {
		return unitPrc;
	}

	public void setUnitPrc(String unitPrc) {
		this.unitPrc = unitPrc;
	}

	public String getBuyPrc() {
		return buyPrc;
	}

	public void setBuyPrc(String buyPrc) {
		this.buyPrc = buyPrc;
	}

	public String getLt() {
		return lt;
	}

	public void setLt(String lt) {
		this.lt = lt;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getIsrtUsrId() {
		return isrtUsrId;
	}

	public void setIsrtUsrId(String isrtUsrId) {
		this.isrtUsrId = isrtUsrId;
	}

	public String getUpdtUsrId() {
		return updtUsrId;
	}

	public void setUpdtUsrId(String updtUsrId) {
		this.updtUsrId = updtUsrId;
	}

}
