package com.jinsit.kmec.DM.DM;

import java.io.Serializable;

public class DM_DM01_R00_ITEM04 implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String EMP_ID;
	public String EMP_NM;
	public String ATTEN_CD_NM;
	public String REP_ST;
	public String REP_ST_NM;
	public String OT_RMK;
	public String OT_WORK_DT;
	
	public DM_DM01_R00_ITEM04() {
		super();
	}
	public DM_DM01_R00_ITEM04(String eMP_ID, String eMP_NM, String aTTEN_CD_NM,
			String rEP_ST, String rEP_ST_NM, String otRmk, String otWorkDt ) {
		super();
		EMP_ID = eMP_ID;
		EMP_NM = eMP_NM;
		ATTEN_CD_NM = aTTEN_CD_NM;
		REP_ST = rEP_ST;
		REP_ST_NM = rEP_ST_NM;
		OT_RMK = otRmk;
		OT_WORK_DT = otWorkDt;
	}
	public String getEMP_ID() {
		return EMP_ID;
	}
	public void setEMP_ID(String eMP_ID) {
		EMP_ID = eMP_ID;
	}
	public String getEMP_NM() {
		return EMP_NM;
	}
	public void setEMP_NM(String eMP_NM) {
		EMP_NM = eMP_NM;
	}
	public String getATTEN_CD_NM() {
		return ATTEN_CD_NM;
	}
	public void setATTEN_CD_NM(String aTTEN_CD_NM) {
		ATTEN_CD_NM = aTTEN_CD_NM;
	}
	public String getREP_ST() {
		return REP_ST;
	}
	public void setREP_ST(String rEP_ST) {
		REP_ST = rEP_ST;
	}
	public String getREP_ST_NM() {
		return REP_ST_NM;
	}
	public void setREP_ST_NM(String rEP_ST_NM) {
		REP_ST_NM = rEP_ST_NM;
	}
	public String getOT_RMK() {
		return OT_RMK;
	}
	public void setOT_RMK(String oT_RMK) {
		OT_RMK = oT_RMK;
	}
	public String getOT_WORK_DT() {
		return OT_WORK_DT;
	}
	public void setOT_WORK_DT(String oT_WORK_DT) {
		OT_WORK_DT = oT_WORK_DT;
	}
	
	
}
