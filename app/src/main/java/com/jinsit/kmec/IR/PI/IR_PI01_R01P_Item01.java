package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI01_R01P_Item01 implements Serializable {

	
	

	public IR_PI01_R01P_Item01() {
		super();
		// TODO Auto-generated constructor stub
	}
	public IR_PI01_R01P_Item01(String partsDt, String bldgNo, String bldgNm, String refContrNo, String repSt,
			String partsCd, String matDueDt, String apprDt, String matDt,
			String matApprDt) {
		super();
		this.partsDt = partsDt;
		this.bldgNo = bldgNo;
		this.bldgNm = bldgNm;
		this.refContrNo = refContrNo;
		this.repSt = repSt;
		this.partsCd = partsCd;
		this.matDueDt = matDueDt;
		this.apprDt = apprDt;
		this.matDt = matDt;
		this.matApprDt = matApprDt;
	}
	private static final long serialVersionUID = -5863523625540915652L;
	
	
	private String partsDt;
	private String bldgNo;
	private String bldgNm;
	private String refContrNo;
	private String repSt;
	private String partsCd;
	private String matDueDt;
	private String apprDt;
	private String matDt;
	private String matApprDt;
	
	public String getPartsDt() {
		return partsDt;
	}
	public void setPartsDt(String partsDt) {
		this.partsDt = partsDt;
	}
	public String getBldgNo() {
		return bldgNo;
	}
	public void setBldgNo(String bldgNo) {
		this.bldgNo = bldgNo;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public String getRefContrNo() {
		return refContrNo;
	}
	public void setRefContrNo(String refContrNo) {
		this.refContrNo = refContrNo;
	}
	public String getRepSt() {
		return repSt;
	}
	public void setRepSt(String repSt) {
		this.repSt = repSt;
	}
	public String getPartsCd() {
		return partsCd;
	}
	public void setPartsCd(String partsCd) {
		this.partsCd = partsCd;
	}
	public String getMatDueDt() {
		return matDueDt;
	}
	public void setMatDueDt(String matDueDt) {
		this.matDueDt = matDueDt;
	}
	public String getApprDt() {
		return apprDt;
	}
	public void setApprDt(String apprDt) {
		this.apprDt = apprDt;
	}
	public String getMatDt() {
		return matDt;
	}
	public void setMatDt(String matDt) {
		this.matDt = matDt;
	}
	public String getMatApprDt() {
		return matApprDt;
	}
	public void setMatApprDt(String matApprDt) {
		this.matApprDt = matApprDt;
	}
	
	@Override
	public String toString() {
		return "IR_PI01_R02P_Item [partsDt=" + partsDt + ", bldgNo=" + bldgNo
				+ ", bldgNm=" + bldgNm + ", refContrNo=" + refContrNo
				+ ", repSt=" + repSt + ", partsCd=" + partsCd + ", matDueDt="
				+ matDueDt + ", apprDt=" + apprDt + ", matDt=" + matDt
				+ ", matApprDt=" + matApprDt + "]";
	}

}
