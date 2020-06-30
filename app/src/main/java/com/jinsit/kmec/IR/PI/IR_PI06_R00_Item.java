package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI06_R00_Item implements Serializable {
	/**
	 *
	 */
	public IR_PI06_R00_Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IR_PI06_R00_Item(String CD, String NM, String TP) {
		this.CD = CD;
		this.NM = NM;
		this.TP = TP;
	}

	private String CD;
	private String NM;
	private String TP;

	public String getCD() {
		return CD;
	}

	public void setCD(String CD) {
		this.CD = CD;
	}

	public String getNM() {
		return NM;
	}

	public void setNM(String NM) {
		this.NM = NM;
	}

	public String getTP() {
		return TP;
	}

	public void setTP(String TP) {
		this.TP = TP;
	}


	@Override
	public String toString() {
		return getNM();
	}
}
