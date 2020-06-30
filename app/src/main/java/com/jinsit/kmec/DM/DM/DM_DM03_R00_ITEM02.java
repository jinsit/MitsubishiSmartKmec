package com.jinsit.kmec.DM.DM;

import java.io.Serializable;
import java.text.NumberFormat;

public class DM_DM03_R00_ITEM02 implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = -6168516955544636647L;



	/**
	 *
	 */
	public DM_DM03_R00_ITEM02() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param csEmpId
	 * @param empNm
	 * @param otWorkDt
	 * @param approvalYn
	 * @param approvalYnNm
	 * @param approvalDt
	 * @param basicOt
	 * @param onDutyOt
	 * @param specialOt
	 * @param rmk
	 * @param deptCustId
	 */
	public DM_DM03_R00_ITEM02(String csEmpId, String empNm, String otWorkDt,
							  String approvalYn,String approvalYnNm, String approvalDt, String basicOt,
							  String onDutyOt, String specialOt, String rmk, String deptCustId) {
		super();
		this.csEmpId = csEmpId;
		this.empNm = empNm;
		this.otWorkDt = otWorkDt;
		this.approvalYn = approvalYn;
		this.approvalYnNm = approvalYnNm;
		this.approvalDt = approvalDt;
		this.basicOt = basicOt;
		this.onDutyOt = onDutyOt;
		this.specialOt = specialOt;
		this.rmk = rmk;
		this.deptCustId = deptCustId;
	}


	private String csEmpId;
	private String empNm;
	private String otWorkDt;
	private String approvalYn;
	private String approvalYnNm;
	private String approvalDt;
	private String basicOt;
	private String onDutyOt;
	private String specialOt;
	private String rmk;
	private String deptCustId;



	public String getApprovalYnNm() {
		return approvalYnNm;
	}
	public void setApprovalYnNm(String approvalYnNm) {
		this.approvalYnNm = approvalYnNm;
	}
	public String getCsEmpId() {
		return csEmpId;
	}

	public void setCsEmpId(String csEmpId) {
		this.csEmpId = csEmpId;
	}
	public String getEmpNm() {
		return empNm;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	public String getOtWorkDt() {
		return otWorkDt;
	}
	public void setOtWorkDt(String otWorkDt) {
		this.otWorkDt = otWorkDt;
	}
	public String getApprovalYn() {
		return approvalYn;
	}
	public void setApprovalYn(String approvalYn) {
		this.approvalYn = approvalYn;
	}
	public String getApprovalDt() {
		return approvalDt;
	}
	public void setApprovalDt(String approvalDt) {
		this.approvalDt = approvalDt;
	}

	public String getBasicOt() {
		if (basicOt == "" || basicOt == null) {
			basicOt = "0";
		}
		return basicOt;
	}

	public void setBasicOt(String basicOt) {

		this.basicOt = basicOt;
	}
	public String getOnDutyOt() {
		if(onDutyOt == "" || onDutyOt == null)
		{
			onDutyOt = "0";
		}
		return onDutyOt;
	}
	public void setOnDutyOt(String onDutyOt) {

		this.onDutyOt = onDutyOt;
	}
	public String getSpecialOt() {
		if (specialOt == "" || specialOt == null) {
			specialOt = "0";
		}
		return specialOt;
	}
	public void setSpecialOt(String specialOt) {
		this.specialOt = specialOt;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getDeptCustId() {
		return deptCustId;
	}
	public void setDeptCustId(String deptCustId) {
		this.deptCustId = deptCustId;
	}

	public String getTotalOt()
	{
		double basicOt;
		double onDutyOt;
		double specialOt;
		try {
			basicOt = Double.valueOf(this.getBasicOt());
		} catch (Exception ex) {
			basicOt =0;
		}
		try {
			onDutyOt = Double.valueOf(this.getOnDutyOt());
		} catch (Exception ex) {
			onDutyOt =0;
		}
		try {
			specialOt = Double.valueOf(this.getSpecialOt());
		} catch (Exception ex) {
			specialOt =0;
		}

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0); // 소수점 아래의 자릿수를 정하는 부분
		nf.setGroupingUsed(false); // 그룹핑이라는 것은 1,000 이런 식으로 0을 세자리씩 묶어서 쉼표로 나누는
		// 것을 의미.

		return nf.format(basicOt + onDutyOt + specialOt);
	}


	@Override
	public String toString() {
		return "DM_DM03_R00_ITEM02 [csEmpId=" + csEmpId + ", empNm=" + empNm
				+ ", otWorkDt=" + otWorkDt + ", approvalYn=" + approvalYn
				+ ", approvalYnNm=" + approvalYnNm + ", approvalDt="
				+ approvalDt + ", basicOt=" + basicOt + ", onDutyOt="
				+ onDutyOt + ", specialOt=" + specialOt + ", rmk=" + rmk
				+ ", deptCustId=" + deptCustId + "]";
	}


}