package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM01_R00_ITEM01 implements Serializable {	

	
	/**
	 * 
	 */
	public DM_DM01_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param csEmpId
	 * @param workDt
	 * @param approvalYn
	 * @param approvalDt
	 * @param basicOt
	 * @param onDutyOt
	 * @param specialOt
	 */
	public DM_DM01_R00_ITEM01(String csEmpId, String workDt, String approvalYn, String basicOt, String onDutyOt, String specialOt) {
		super();
		this.csEmpId = csEmpId;
		this.workDt = workDt;
		this.approvalYn = approvalYn;
		this.basicOt = basicOt;
		this.onDutyOt = onDutyOt;
		this.specialOt = specialOt;
	}
	
	
	public DM_DM01_R00_ITEM01(String csEmpId, String workDt, String basicOt, String onDutyOt, String specialOt) {
		super();
		this.csEmpId = csEmpId;
		this.workDt = workDt;
		this.basicOt = basicOt;
		this.onDutyOt = onDutyOt;
		this.specialOt = specialOt;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7088054275838816410L;
	private String csEmpId;
	private String workDt;
	private String approvalYn;
	private String basicOt;
	private String onDutyOt;
	private String specialOt;
	
	public String getCsEmpId() {
		return csEmpId;
	}
	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}
	public String getWorkDt() {
		return workDt;
	}
	public void setWorkDt(String workDt) {
		this.workDt = workDt;
	}
	public String getApprovalYn() {
		return approvalYn;
	}
	public void setApprovalYn(String approvalYn) {
		this.approvalYn = approvalYn;
	}
	public String getBasicOt() {
		return basicOt;
	}
	public void setBasicOt(String basicOt) {
		this.basicOt = basicOt;
	}
	public String getOnDutyOt() {
		return onDutyOt;
	}
	public void setOnDutyOt(String onDutyOt) {
		this.onDutyOt = onDutyOt;
	}
	public String getSpecialOt() {
		return specialOt;
	}
	public void setSpecialOt(String specialOt) {
		this.specialOt = specialOt;
	}
	@Override
	public String toString() {
		return "DM_DM01_R00_ITEM01 [csEmpId=" + csEmpId + ", workDt=" + workDt
				+ ", approvalYn=" + approvalYn 
				+ ", basicOt=" + basicOt + ", onDutyOt=" + onDutyOt
				+ ", specialOt=" + specialOt + "]";
	}
	
}