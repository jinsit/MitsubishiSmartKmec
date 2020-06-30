package com.jinsit.kmec.CM;

public class CM_SelectPermissionCheck_ReqestData {
	public String usrId = "";
	public String menuCd = "";

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getMenuCd() {
		return menuCd;
	}

	public void setMenuCd(String menuCd) {
		this.menuCd = menuCd;
	}

	public CM_SelectPermissionCheck_ReqestData(String usrId, String menuCd) {
		super();
		this.usrId = usrId;
		this.menuCd = menuCd;
	}

	public CM_SelectPermissionCheck_ReqestData() {
		super();
	}

}
