package com.jinsit.kmec.WO.WT.RI;
/**
 * WorkStatusData
 * @discription 각파트별 작업 진행현황 데이터
 * @author 원성민
 *
 */
public class WO_WT00_R01_ITEM01 {
	public String NFC_PLC;
	public String NFC_PLC_NM;
	public String JOB_ST;
	public String JOB_ST_NM;
	
	public WO_WT00_R01_ITEM01(String nFC_PLC, String nFC_PLC_NM, String jOB_ST,
			String jOB_ST_NM) {
		super();
		NFC_PLC = nFC_PLC;
		NFC_PLC_NM = nFC_PLC_NM;
		JOB_ST = jOB_ST;
		JOB_ST_NM = jOB_ST_NM;
	}
	public WO_WT00_R01_ITEM01() {
		// TODO Auto-generated constructor stub
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
	public String getJOB_ST() {
		return JOB_ST;
	}
	public void setJOB_ST(String jOB_ST) {
		JOB_ST = jOB_ST;
	}
	public String getJOB_ST_NM() {
		return JOB_ST_NM;
	}
	public void setJOB_ST_NM(String jOB_ST_NM) {
		JOB_ST_NM = jOB_ST_NM;
	}
}
