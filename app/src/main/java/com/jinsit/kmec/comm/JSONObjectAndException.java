package com.jinsit.kmec.comm;

import org.json.JSONObject;

public class JSONObjectAndException {

	public JSONObject jSONObj;
	public Exception exception;

	public JSONObjectAndException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JSONObjectAndException(JSONObject jSONObj, Exception exception) {
		super();
		this.jSONObj = jSONObj;
		this.exception = exception;
	}

	public JSONObject getjSONObj() {
		return jSONObj;
	}

	public void setjSONObj(JSONObject jSONObj) {
		this.jSONObj = jSONObj;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
