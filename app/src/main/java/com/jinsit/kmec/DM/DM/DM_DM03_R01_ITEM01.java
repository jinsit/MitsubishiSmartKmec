package com.jinsit.kmec.DM.DM;

public class DM_DM03_R01_ITEM01 {
	public DM_DM03_R01_ITEM01() {

	}

	public String EMP_ID;
	public String EMP_NM;
	public String ATTEN_CD_NM;
	public String REP_ST_NM;

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

	public String getATTEND_CD_NM() {
		return ATTEN_CD_NM;
	}

	public void setATTEND_CD_NM(String aTTEND_CD_NM) {
		ATTEN_CD_NM = aTTEND_CD_NM;
	}

	public String getREP_ST_NM() {
		return REP_ST_NM;
	}

	public void setREP_ST_NM(String rEP_ST_NM) {
		REP_ST_NM = rEP_ST_NM;
	}

	public DM_DM03_R01_ITEM01(String eMP_ID, String eMP_NM,
			String aTTEND_CD_NM, String rEP_ST_NM) {
		super();
		EMP_ID = eMP_ID;
		EMP_NM = eMP_NM;
		ATTEN_CD_NM = aTTEND_CD_NM;
		REP_ST_NM = rEP_ST_NM;
	}

}
