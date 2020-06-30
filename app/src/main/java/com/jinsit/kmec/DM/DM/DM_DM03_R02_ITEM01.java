package com.jinsit.kmec.DM.DM;

public class DM_DM03_R02_ITEM01 {

	public String EMP_ID;
	public String EMP_NM;
	public String ATTEND_CD;
	public String ATTEN_CD_NM;
	public String ATTEND_DT;
	public String RMK;
	public String REP_ST;
	
	public DM_DM03_R02_ITEM01(String eMP_ID, String eMP_NM, String aTTEND_CD,
			String aTTEN_CD_NM, String aTTEND_DT, String rMK, String rEP_ST) {
		super();
		EMP_ID = eMP_ID;
		EMP_NM = eMP_NM;
		ATTEND_CD = aTTEND_CD;
		ATTEN_CD_NM = aTTEN_CD_NM;
		ATTEND_DT = aTTEND_DT;
		RMK = rMK;
		REP_ST = rEP_ST;
	}
	public DM_DM03_R02_ITEM01() {
		super();
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
	public String getATTEND_CD() {
		return ATTEND_CD;
	}
	public void setATTEND_CD(String aTTEND_CD) {
		ATTEND_CD = aTTEND_CD;
	}
	public String getATTEN_CD_NM() {
		return ATTEN_CD_NM;
	}
	public void setATTEN_CD_NM(String aTTEN_CD_NM) {
		ATTEN_CD_NM = aTTEN_CD_NM;
	}
	public String getATTEND_DT() {
		return ATTEND_DT;
	}
	public void setATTEND_DT(String aTTEND_DT) {
		ATTEND_DT = aTTEND_DT;
	}
	public String getRMK() {
		return RMK;
	}
	public void setRMK(String rMK) {
		RMK = rMK;
	}
	public String getREP_ST() {
		return REP_ST;
	}
	public void setREP_ST(String rEP_ST) {
		REP_ST = rEP_ST;
	}
	
}
