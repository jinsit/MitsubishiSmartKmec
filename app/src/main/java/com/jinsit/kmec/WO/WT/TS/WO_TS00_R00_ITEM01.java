package com.jinsit.kmec.WO.WT.TS;

public class WO_TS00_R00_ITEM01 {

	/**
	 * 
	 */
	public WO_TS00_R00_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * @param jobNo
	 * @param jobStNm
	 * @param asTp
	 * @param csTmFr
	 */
	public WO_TS00_R00_ITEM01(String jobNo, String jobStNm, String asTp,
			String csTmFr) {
		super();
		this.jobNo = jobNo;
		this.jobStNm = jobStNm;
		this.asTp = asTp;
		this.csTmFr = csTmFr;
	}



	private String jobNo;
	private String jobStNm;
	private String asTp;
	private String csTmFr;
	
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getJobStNm() {
		return jobStNm;
	}

	public void setJobStNm(String jobStNm) {
		this.jobStNm = jobStNm;
	}

	public String getAsTp() {
		return asTp;
	}

	public void setAsTp(String asTp) {
		this.asTp = asTp;
	}

	public String getCsTmFr() {
		return csTmFr;
	}

	public void setCsTmFr(String csTmFr) {
		this.csTmFr = csTmFr;
	}

}
