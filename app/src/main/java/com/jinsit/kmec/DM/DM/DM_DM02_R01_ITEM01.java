package com.jinsit.kmec.DM.DM;


public class DM_DM02_R01_ITEM01 {

	public String ATTEN_DT;
	public String ATTEN_CD_NM;
	public String REP_ST_NM;

	public String getATTEND_DT() {
		return ATTEN_DT;
	}

	public void setATTEND_DT(String aTTEND_DT) {
		ATTEN_DT = aTTEND_DT;
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

	public DM_DM02_R01_ITEM01(String aTTEND_DT, String aTTEND_CD_NM,
			String rEP_ST_NM) {
		super();
		ATTEN_DT = aTTEND_DT;
		ATTEN_CD_NM = aTTEND_CD_NM;
		REP_ST_NM = rEP_ST_NM;
	}

	public DM_DM02_R01_ITEM01(String aTTEND_CD_NM, String rEP_ST_NM) {
		super();
		ATTEN_CD_NM = aTTEND_CD_NM;
		REP_ST_NM = rEP_ST_NM;
	}

	public DM_DM02_R01_ITEM01() {
		super();
	}

}
