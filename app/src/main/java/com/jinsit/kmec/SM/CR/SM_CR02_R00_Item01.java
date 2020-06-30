package com.jinsit.kmec.SM.CR;

import java.io.Serializable;

public class SM_CR02_R00_Item01 implements Serializable {

	//Field
	private String ngNo;
	private String projectNo;
	private String projectNm;
	private String empId;
	private String empNm;
	private static final long serialVersionUID = 3863247534869459285L;
	
	//Constructor
	public SM_CR02_R00_Item01(String ngNo, String projectNo, String projectNm,
			String empId, String empNm) {
		super();
		this.ngNo = ngNo;
		this.projectNo = projectNo;
		this.projectNm = projectNm;
		this.empId = empId;
		this.empNm = empNm;
	}

	//Getter & Setter
	public String getNgNo() {
		return ngNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public String getProjectNm() {
		return projectNm;
	}

	public String getEmpId() {
		return empId;
	}

	public String getEmpNm() {
		return empNm;
	}

	public void setNgNo(String ngNo) {
		this.ngNo = ngNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public void setProjectNm(String projectNm) {
		this.projectNm = projectNm;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}

	@Override
	public String toString() {
		return "SM_CR02_R00_Item01 [ngNo=" + ngNo + ", projectNo=" + projectNo
				+ ", projectNm=" + projectNm + ", empId=" + empId + ", empNm="
				+ empNm + "]";
	}
	
};
