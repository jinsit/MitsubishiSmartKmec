package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI05_R00_Item implements Serializable {
	/**
	 *
	 */
	public IR_PI05_R00_Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param PITEM_NO
	 * @param DS_DRAW_NO
	 * @param GL_NO
	 * @param DS_DRAW_NM
//	 * @param QTY
	 * @param ITEM_NO
	 * @param REMARK
	 */
	public IR_PI05_R00_Item(String PITEM_NO, String DS_DRAW_NO, String GL_NO,
							String DS_DRAW_NM, String QTY, String ITEM_NO, String REMARK) {
		super();
		this.PITEM_NO = PITEM_NO;
		this.DS_DRAW_NO = DS_DRAW_NO;
		this.GL_NO = GL_NO;
		this.DS_DRAW_NM = DS_DRAW_NM;
		this.QTY = QTY;
		this.ITEM_NO = ITEM_NO;
		this.REMARK = REMARK;
	}


	@Override
	public String toString() {
		return "IR_PI05_R00_Item [PITEM_NO=" + PITEM_NO + ", DS_DRAW_NO=" + DS_DRAW_NO
				+ ", GL_NO=" + GL_NO + ", DS_DRAW_NM=" + DS_DRAW_NM +  ", QTY=" + QTY + ", ITEM_NO=" + ITEM_NO + ", REMARK=" + REMARK + "]";
	}


	private static final long serialVersionUID = -68615540667429516L;
	private String PITEM_NO;
	private String DS_DRAW_NO;
	private String GL_NO;
	private String DS_DRAW_NM;
	private String QTY;
	private String ITEM_NO;
	private String REMARK;

	public String getPITEM_NO() {
		return PITEM_NO;
	}

	public void setPITEM_NO(String PITEM_NO) {
		this.PITEM_NO = PITEM_NO;
	}

	public String getDS_DRAW_NO() {
		return DS_DRAW_NO;
	}

	public void setDS_DRAW_NO(String DS_DRAW_NO) {
		this.PITEM_NO = DS_DRAW_NO;
	}

	public String getGL_NO() {
		return GL_NO;
	}

	public void setGL_NO(String GL_NO) {
		this.GL_NO = GL_NO;
	}

	public String getDS_DRAW_NM() {
		return DS_DRAW_NM;
	}

	public void setDS_DRAW_NM(String DS_DRAW_NM) {
		this.DS_DRAW_NM = DS_DRAW_NM;
	}

	public String getQTY() {
		return QTY;
	}

	public void setQTY(String QTY) {
		this.QTY = QTY;
	}

	public String getITEM_NO() {
		return ITEM_NO;
	}

	public void setITEM_NO(String ITEM_NO) {
		this.ITEM_NO = ITEM_NO;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

}
