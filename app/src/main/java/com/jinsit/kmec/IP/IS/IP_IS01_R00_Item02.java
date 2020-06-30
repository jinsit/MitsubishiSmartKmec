package com.jinsit.kmec.IP.IS;

import java.io.Serializable;

public class IP_IS01_R00_Item02 implements Serializable {

	private static final long serialVersionUID = -8962851987975349586L;
	private String bldgNm;
	private String endVal;
	private String dCnt;
	private String tCnt;
	
	public IP_IS01_R00_Item02(){};
	public IP_IS01_R00_Item02(  String bldgNm
							  , String endVal
							  , String dCnt
							  , String tCnt) {
		super();
		this.bldgNm  = bldgNm;
		this.endVal  = endVal;
		this.dCnt 	 = dCnt;
		this.tCnt 	 = tCnt;
	}

	public String getBldgNm() {
		return bldgNm;
	}
	public String getEndVal() {
		return endVal;
	}
	public String getdCnt() {
		return dCnt;
	}
	public String gettCnt() {
		return tCnt;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public void setEndVal(String endVal) {
		this.endVal = endVal;
	}
	public void setdCnt(String dCnt) {
		this.dCnt = dCnt;
	}
	public void settCnt(String tCnt) {
		this.tCnt = tCnt;
	}
	
	@Override
	public String toString() {
		return "IP_IS01_R00_Item02 [bldgNm=" + bldgNm + ", endVal=" + endVal
				+ ", dCnt=" + dCnt + ", tCnt=" + tCnt + "]";
	}
};
