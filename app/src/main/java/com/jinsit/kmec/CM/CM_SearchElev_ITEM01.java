package com.jinsit.kmec.CM;

public class CM_SearchElev_ITEM01 {
	
	
	
	/**
	 * 
	 */
	public CM_SearchElev_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param bldgNo
	 * @param carNo
	 * @param dongCarNo
	 * @param modelNm
	 */
	public CM_SearchElev_ITEM01(String bldgNo, String carNo, String dongCarNo,
			String modelNm) {
		super();
		this.bldgNo = bldgNo;
		this.carNo = carNo;
		this.dongCarNo = dongCarNo;
		this.modelNm = modelNm;
	}
	
	private String bldgNo;
	private String carNo;
	private String dongCarNo;
	private String modelNm;
	public String getBldgNo() {
		return bldgNo;
	}
	public void setBldgNo(String bldgNo) {
		this.bldgNo = bldgNo;
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
	public String getModelNm() {
		return modelNm;
	}
	public void setModelNm (String modelNm) {
		this.modelNm = modelNm;
	}
	
	@Override
	public String toString() {
		return "CM_SearchElev_ITEM01 [bldgNo=" + bldgNo + ", carNo=" + carNo
				+ ", dongCarNo=" + dongCarNo + ", modelNm=" + modelNm + "]";
	}
	
	
	
	
}
