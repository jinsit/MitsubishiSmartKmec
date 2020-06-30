package com.jinsit.kmec.comm.CV;

public class InspectionPlanHistoryData {

	public String EMP_ID;
	public String WORK_DT;
	public String WORK_DAY;
	public String WEEKDAY;
	public String DAY_NM1;
	public String DAY_NM2;
	public String DAY_NM3;
	public String D_CNT;
	public String N_CNT;
	
	
	public InspectionPlanHistoryData(String eMP_ID, String wORK_DT,
			String wORK_DAY, String wEEKDAY, String dAY_NM1, String dAY_NM2,
			String dAY_NM3, String d_CNT, String n_CNT) {
		super();
		EMP_ID = eMP_ID;
		WORK_DT = wORK_DT;
		WORK_DAY = wORK_DAY;
		WEEKDAY = wEEKDAY;
		DAY_NM1 = dAY_NM1;
		DAY_NM2 = dAY_NM2;
		DAY_NM3 = dAY_NM3;
		D_CNT = d_CNT;
		N_CNT = n_CNT;
	}
	public String getEMP_ID() {
		return EMP_ID;
	}
	public void setEMP_ID(String eMP_ID) {
		EMP_ID = eMP_ID;
	}
	public String getWORK_DT() {
		return WORK_DT;
	}
	public void setWORK_DT(String wORK_DT) {
		WORK_DT = wORK_DT;
	}
	public String getWORK_DAY() {
		return WORK_DAY;
	}
	public void setWORK_DAY(String wORK_DAY) {
		WORK_DAY = wORK_DAY;
	}
	public String getWEEKDAY() {
		return WEEKDAY;
	}
	public void setWEEKDAY(String wEEKDAY) {
		WEEKDAY = wEEKDAY;
	}
	public String getDAY_NM1() {
		return DAY_NM1;
	}
	public void setDAY_NM1(String dAY_NM1) {
		DAY_NM1 = dAY_NM1;
	}
	public String getDAY_NM2() {
		return DAY_NM2;
	}
	public void setDAY_NM2(String dAY_NM2) {
		DAY_NM2 = dAY_NM2;
	}
	public String getDAY_NM3() {
		return DAY_NM3;
	}
	public void setDAY_NM3(String dAY_NM3) {
		DAY_NM3 = dAY_NM3;
	}
	public String getD_CNT() {
		return D_CNT;
	}
	public void setD_CNT(String d_CNT) {
		D_CNT = d_CNT;
	}
	public String getN_CNT() {
		return N_CNT;
	}
	public void setN_CNT(String n_CNT) {
		N_CNT = n_CNT;
	}
}
