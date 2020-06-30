package com.jinsit.kmec.WO.WT.RI;

import java.io.Serializable;

public class RoutineCheckListData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String WORK_DT;
	public String BLDG_NO;
	public String BLDG_NM;
	public String E_TEXT;
	public String I_CNT;
	public String T_CNT;
	public String REF_CONTR_NO;
	
	public RoutineCheckListData() {
		super();
	}
	public RoutineCheckListData(String wORK_DT, String bLDG_NO, String bLDG_NM,
			String e_TEXT, String i_CNT, String t_CNT,String rEF_CONTR_NO) {
		super();
		WORK_DT = wORK_DT;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		E_TEXT = e_TEXT;
		I_CNT = i_CNT;
		T_CNT = t_CNT;
		REF_CONTR_NO = rEF_CONTR_NO;
	}
	public String getWORK_DT() {
		return WORK_DT;
	}
	public void setWORK_DT(String wORK_DT) {
		WORK_DT = wORK_DT;
	}
	public String getBLDG_NO() {
		return BLDG_NO;
	}
	public void setBLDG_NO(String bLDG_NO) {
		BLDG_NO = bLDG_NO;
	}
	public String getBLDG_NM() {
		return BLDG_NM;
	}
	public void setBLDG_NM(String bLDG_NM) {
		BLDG_NM = bLDG_NM;
	}
	public String getE_TEXT() {
		return E_TEXT;
	}
	public void setE_TEXT(String e_TEXT) {
		E_TEXT = e_TEXT;
	}
	public String getI_CNT() {
		return I_CNT;
	}
	public void setI_CNT(String i_CNT) {
		I_CNT = i_CNT;
	}
	public String getT_CNT() {
		return T_CNT;
	}
	public void setT_CNT(String t_CNT) {
		T_CNT = t_CNT;
	}
	public String getREF_CONTR_NO() {
		return REF_CONTR_NO;
	}
	public void setREF_CONTR_NO(String rEF_CONTR_NO) {
		REF_CONTR_NO = rEF_CONTR_NO;
	}
	
	
}
