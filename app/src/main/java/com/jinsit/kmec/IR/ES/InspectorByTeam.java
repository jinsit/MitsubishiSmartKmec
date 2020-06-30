package com.jinsit.kmec.IR.ES;

import java.io.Serializable;

public class InspectorByTeam implements Serializable {

	private static final long serialVersionUID = -389717341082779544L;
	private String MNG_USR_ID;
	private String EMP_NO;
	private String EMP_NM;
	private String DEPT_CD;
	private String DEPT_NM;
	
	public InspectorByTeam(){
		
	};
	
	public InspectorByTeam(String mngUsrId, String empNo, String empNm, String deptCd, String deptNm) {
		super();
		this.MNG_USR_ID = mngUsrId;
		this.EMP_NO  = empNo;
		this.EMP_NM = empNm;
		this.DEPT_CD = deptCd;
		this.DEPT_NM = deptNm;
	}
	
	
	public String getMNG_USR_ID() {
		return MNG_USR_ID;
	}
	public void setMNG_USR_ID(String mNG_USR_ID) {
		MNG_USR_ID = mNG_USR_ID;
	}
	public String getEMP_NO() {
		return EMP_NO;
	}
	public void setEMP_NO(String eMP_NO) {
		EMP_NO = eMP_NO;
	}
	public String getEMP_NM() {
		return EMP_NM;
	}
	public void setEMP_NM(String eMP_NM) {
		EMP_NM = eMP_NM;
	}
	public String getDEPT_CD() {
		return DEPT_CD;
	}
	public void setDEPT_CD(String dEPT_CD) {
		DEPT_CD = dEPT_CD;
	}
	public String getDEPT_NM() {
		return DEPT_NM;
	}
	public void setDEPT_NM(String dEPT_NM) {
		DEPT_NM = dEPT_NM;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}