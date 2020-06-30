package com.jinsit.kmec.DB;

/**
 * -- 2 : LOCAL DB DATA DOWN  => 점검계획 점검파트 TCSQ210
 * PROCEDURE SPDC104M;2
 * @author 원성민
 *
 */
public class TableTCSQ210 {
	public String CS_EMP_ID;
	public String WORK_DT;
	public String JOB_NO;
	public String NFC_PLC;
	public String NFC_PLC_NM;
	public String CS_TM_FR;
	public String CS_TM_TO;
	public String CS_TM;
	public String JOB_ST;
	public String ENG_ST;
	public String REASON_RMK;
	public String RMK;
	public String ISRT_USR_ID;
	public String ISRT_DT;
	public String UPDT_USR_ID;
	public String UPDT_DT;
	public String NFC_UDID;

	public TableTCSQ210() {
		super();
	}

	public TableTCSQ210(String cS_EMP_ID, String wORK_DT, String jOB_NO,
						String nFC_PLC,String nFC_PLC_NM, String cS_TM_FR, String cS_TM_TO, String cS_TM,
						String jOB_ST, String eNG_ST, String rEASON_RMK, String rMK,
						String iSRT_USR_ID, String iSRT_DT, String uPDT_USR_ID,
						String uPDT_DT,String nFC_UDID) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		NFC_PLC = nFC_PLC;
		NFC_PLC_NM = nFC_PLC_NM;
		CS_TM_FR = cS_TM_FR;
		CS_TM_TO = cS_TM_TO;
		CS_TM = cS_TM;
		JOB_ST = jOB_ST;
		ENG_ST = eNG_ST;
		REASON_RMK = rEASON_RMK;
		RMK = rMK;
		ISRT_USR_ID = iSRT_USR_ID;
		ISRT_DT = iSRT_DT;
		UPDT_USR_ID = uPDT_USR_ID;
		UPDT_DT = uPDT_DT;
		NFC_UDID =nFC_UDID;
	}

	public String getCS_EMP_ID() {
		return CS_EMP_ID;
	}

	public void setCS_EMP_ID(String cS_EMP_ID) {
		CS_EMP_ID = cS_EMP_ID;
	}

	public String getWORK_DT() {
		return WORK_DT;
	}

	public void setWORK_DT(String wORK_DT) {
		WORK_DT = wORK_DT;
	}

	public String getJOB_NO() {
		return JOB_NO;
	}

	public void setJOB_NO(String jOB_NO) {
		JOB_NO = jOB_NO;
	}

	public String getNFC_PLC() {
		return NFC_PLC;
	}

	public void setNFC_PLC(String nFC_PLC) {
		NFC_PLC = nFC_PLC;
	}


	public String getNFC_PLC_NM() {
		return NFC_PLC_NM;
	}

	public void setNFC_PLC_NM(String nFC_PLC_NM) {
		NFC_PLC_NM = nFC_PLC_NM;
	}

	public String getCS_TM_FR() {
		return CS_TM_FR;
	}

	public void setCS_TM_FR(String cS_TM_FR) {
		CS_TM_FR = cS_TM_FR;
	}

	public String getCS_TM_TO() {
		return CS_TM_TO;
	}

	public void setCS_TM_TO(String cS_TM_TO) {
		CS_TM_TO = cS_TM_TO;
	}

	public String getCS_TM() {
		return CS_TM;
	}

	public void setCS_TM(String cS_TM) {
		CS_TM = cS_TM;
	}

	public String getJOB_ST() {
		return JOB_ST;
	}

	public void setJOB_ST(String jOB_ST) {
		JOB_ST = jOB_ST;
	}

	public String getENG_ST() {
		return ENG_ST;
	}

	public void setENG_ST(String eNG_ST) {
		ENG_ST = eNG_ST;
	}

	public String getREASON_RMK() {
		return REASON_RMK;
	}

	public void setREASON_RMK(String rEASON_RMK) {
		REASON_RMK = rEASON_RMK;
	}

	public String getRMK() {
		return RMK;
	}

	public void setRMK(String rMK) {
		RMK = rMK;
	}

	public String getISRT_USR_ID() {
		return ISRT_USR_ID;
	}

	public void setISRT_USR_ID(String iSRT_USR_ID) {
		ISRT_USR_ID = iSRT_USR_ID;
	}

	public String getISRT_DT() {
		return ISRT_DT;
	}

	public void setISRT_DT(String iSRT_DT) {
		ISRT_DT = iSRT_DT;
	}

	public String getUPDT_USR_ID() {
		return UPDT_USR_ID;
	}

	public void setUPDT_USR_ID(String uPDT_USR_ID) {
		UPDT_USR_ID = uPDT_USR_ID;
	}

	public String getUPDT_DT() {
		return UPDT_DT;
	}

	public void setUPDT_DT(String uPDT_DT) {
		UPDT_DT = uPDT_DT;
	}

	public String getNFC_UDID() {
		return NFC_UDID;
	}

	public void setNFC_UDID(String nFC_UDID) {
		NFC_UDID = nFC_UDID;
	}

}
