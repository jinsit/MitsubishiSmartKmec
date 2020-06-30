package com.jinsit.kmec.IR.NM;

public class PartData {
	public String CODE_CD;
	public String CODE_NM;

	public PartData(String cODE_CD, String cODE_NM) {
		super();
		CODE_CD = cODE_CD;
		CODE_NM = cODE_NM;
	}

	public String getCODE_CD() {
		return CODE_CD;
	}

	public void setCODE_CD(String cODE_CD) {
		CODE_CD = cODE_CD;
	}

	public String getCODE_NM() {
		return CODE_NM;
	}

	public void setCODE_NM(String cODE_NM) {
		CODE_NM = cODE_NM;
	}

}
