package com.jinsit.kmec.CM;

public class CM_SaveReadPicture_ITEM01 {

	/**
	 * 
	 */
	public CM_SaveReadPicture_ITEM01() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param picTp
	 * @param picSeq
	 * @param fileNm
	 */
	public CM_SaveReadPicture_ITEM01(String picTp, String picSeq, String fileNm) {
		super();
		this.picTp = picTp;
		this.picSeq = picSeq;
		this.fileNm = fileNm;
	}
	private String picTp;
	private String picSeq;
	private String fileNm;

	public String getPicTp() {
		return picTp;
	}
	public void setPicTp(String picTp) {
		this.picTp = picTp;
	}
	public String getPicSeq() {
		return picSeq;
	}
	public void setPicSeq(String picSeq) {
		this.picSeq = picSeq;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
}
