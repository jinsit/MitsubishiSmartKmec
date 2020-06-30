package com.jinsit.kmec.CM;

import java.io.Serializable;
import java.util.ArrayList;

public class CM_SearchBldgInfo_ITEM01 implements Serializable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4382097750283494636L;
	
	
	
	
	/**
	 * 
	 */
	public CM_SearchBldgInfo_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
		this.child = new ArrayList<CM_SearchBldgInfo_ITEM01>();
	}
	
	
	
	/**
	 * @param bldgNo
	 * @param bldgNm
	 * @param addr
	 * @param csCd
	 * @param runSt
	 * @param clientDept
	 * @param clientNm
	 * @param clientHp
	 * @param clientTel
	 * @param contrDt
	 * @param contrDtFrTo
	 * @param faultDtFrTo
	 * @param insDtFrTo
	 * @param empNm1
	 * @param empNm2
	 * @param emp1hP
	 * @param emp2hP
	 * @param csDetpNm
	 */
	public CM_SearchBldgInfo_ITEM01(String bldgNo, String bldgNm, String addr,
			String csCd, String runSt, String clientDept, String clientNm,
			String clientHp, String clientTel, String contrDt,
			String contrDtFrTo, String faultDtFrTo, String insDtFrTo,
			String empNm1, String empNm2, String emp1hP, String emp2hP,
			String csDetpNm) {
		super();
		this.bldgNo = bldgNo;
		this.bldgNm = bldgNm;
		this.addr = addr;
		this.csCd = csCd;
		this.runSt = runSt;
		this.clientDept = clientDept;
		this.clientNm = clientNm;
		this.clientHp = clientHp;
		this.clientTel = clientTel;
		this.contrDt = contrDt;
		this.contrDtFrTo = contrDtFrTo;
		this.faultDtFrTo = faultDtFrTo;
		this.insDtFrTo = insDtFrTo;
		this.empNm1 = empNm1;
		this.empNm2 = empNm2;
		this.emp1hP = emp1hP;
		this.emp2hP = emp2hP;
		this.csDetpNm = csDetpNm;
		this.child = new ArrayList<CM_SearchBldgInfo_ITEM01>();
	}



	private String bldgNo;
	private String bldgNm;
	private String addr;
	private String csCd;
	private String runSt;
	private String clientDept;
	private String clientNm;
	private String clientHp;
	private String clientTel;
	private String contrDt;
	private String contrDtFrTo;
	private String faultDtFrTo;
	private String insDtFrTo;
	private String empNm1;
	private String empNm2;
	private String emp1hP;
	private String emp2hP;
	private String csDetpNm;




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
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCsCd() {
		return csCd;
	}
	public void setCsCd(String csCd) {
		this.csCd = csCd;
	}
	public String getRunSt() {
		return runSt;
	}
	public void setRunSt(String runSt) {
		this.runSt = runSt;
	}
	public String getClientDept() {
		return clientDept;
	}
	public void setClientDept(String clientDept) {
		this.clientDept = clientDept;
	}
	public String getClientNm() {
		return clientNm;
	}
	public void setClientNm(String clientNm) {
		this.clientNm = clientNm;
	}
	public String getClientHp() {
		return this.clientHp;
	}
	public void setClientHp(String clientHp) {
		this.clientHp = clientHp;
	}
	public String getClientTel() {
		return this.clientTel;
	}
	public void setClientTel(String clientTel) {
		this.clientTel = clientTel;
	}
	public String getContrDt() {
		return this.contrDt;
	}
	public void setContrDt(String contrDt) {
		this.contrDt = contrDt;
	}
	public String getContrDtFrTo() {
		return this.contrDtFrTo;
	}
	public void setContrDtFrTo(String contrDtFrTo) {
		this.contrDtFrTo = contrDtFrTo;
	}
	public String getFaultDtFrTo() {
		return this.faultDtFrTo;
	}
	public void setFaultDtFrTo(String faultDtFrTo) {
		this.faultDtFrTo = faultDtFrTo;
	}
	public String getInsDtFrTo() {
		return this.insDtFrTo;
	}
	public void setInsDtFrTo(String insDtFrTo) {
		this.insDtFrTo = insDtFrTo;
	}
	public String getEmpNm1() {
		return this.empNm1;
	}
	public void setEmpNm1(String empNm1) {
		this.empNm1 = empNm1;
	}
	public String getEmp2hP() {
		return this.emp2hP;
	}
	public void setEmp2hP(String emp2hP) {
		this.emp2hP = emp2hP;
	}
	public String getCsDetpNm() {
		return this.csDetpNm;
	}
	public void setCsDetpNm(String csDetpNm) {
		this.csDetpNm = csDetpNm;
	}
	
	public String getEmpNm2() {
		return empNm2;
	}
	public void setEmpNm2(String empNm2) {
		this.empNm2 = empNm2;
	}
	public String getEmp1hP() {
		return emp1hP;
	}
	public void setEmp1hP(String emp1hP) {
		this.emp1hP = emp1hP;
	}
	private ArrayList<CM_SearchBldgInfo_ITEM01> child;
	
	public ArrayList<CM_SearchBldgInfo_ITEM01> getChild() {
		return child;
	}



	public void setChild(ArrayList<CM_SearchBldgInfo_ITEM01> child) {
		this.child = child;
	}



	@Override
	public String toString() {
		return "IR_CI00_R00_ITEM01 [bldgNo=" + bldgNo + ", bldgNm=" + bldgNm
				+ ", addr=" + addr + ", csCd=" + csCd + ", runSt=" + runSt
				+ ", clientDept=" + clientDept + ", clientNm=" + clientNm
				+ ", clientHp=" + clientHp + ", clientTel=" + clientTel
				+ ", contrDt=" + contrDt + ", contrDtFrTo=" + contrDtFrTo
				+ ", faultDtFrTo=" + faultDtFrTo + ", insDtFrTo=" + insDtFrTo
				+ ", empNm1=" + empNm1 + ", empNm2=" + empNm2 + ", emp1hP="
				+ emp1hP + ", emp2hP=" + emp2hP + ", csDetpNm=" + csDetpNm
				+ "]";
	}
	

	
	
	
}
