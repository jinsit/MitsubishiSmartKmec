package com.jinsit.kmec.IR.EDS;

public class IR_EDS00_R00P_ITEM00 {

	public String LICENSE_NO;
	public String LICENSE_NM;
	public String APPROVE_MON;
	public String EDU_TO_DT;
	public String NUM;

	public IR_EDS00_R00P_ITEM00(String lICENSE_NO, String lICENSE_NM,
			String aPPROVE_MON, String eDU_TO_DT, String nUM) {
		super();
		LICENSE_NO = lICENSE_NO;
		LICENSE_NM = lICENSE_NM;
		APPROVE_MON = aPPROVE_MON;
		EDU_TO_DT = eDU_TO_DT;
		NUM = nUM;
	}

	public String getLICENSE_NO() {
		return LICENSE_NO;
	}

	public void setLICENSE_NO(String lICENSE_NO) {
		LICENSE_NO = lICENSE_NO;
	}

	public String getLICENSE_NM() {
		return LICENSE_NM;
	}

	public void setLICENSE_NM(String lICENSE_NM) {
		LICENSE_NM = lICENSE_NM;
	}

	public String getAPPROVE_MON() {
		return APPROVE_MON;
	}

	public void setAPPROVE_MON(String aPPROVE_MON) {
		APPROVE_MON = aPPROVE_MON;
	}

	public String getEDU_TO_DT() {
		return EDU_TO_DT;
	}

	public void setEDU_TO_DT(String eDU_TO_DT) {
		EDU_TO_DT = eDU_TO_DT;
	}

	public String getNUM() {
		return NUM;
	}

	public void setNUM(String nUM) {
		NUM = nUM;
	}

}
