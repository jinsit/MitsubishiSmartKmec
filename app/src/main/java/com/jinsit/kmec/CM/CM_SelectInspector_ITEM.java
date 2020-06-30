package com.jinsit.kmec.CM;

public class CM_SelectInspector_ITEM {
	
	

	private String csEmpId;
	private String deptNm;
	private String empNm;
	private String phone1;
	
	public CM_SelectInspector_ITEM() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CM_SelectInspector_ITEM(String csEmpId, String deptNm, String empNm,
			String phone1) {
		super();
		this.csEmpId = csEmpId;
		this.deptNm = deptNm;
		this.empNm = empNm;
		this.phone1 = phone1;
	}
	public String getCsEmpId() {
		return csEmpId;
	}
	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getEmpNm() {
		return empNm;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	

	
}
