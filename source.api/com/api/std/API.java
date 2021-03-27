package com.api.std;

import java.sql.Connection;
import java.sql.SQLException;

import com.database.jdbc.DatabaseHelper;
import com.database.jdbc.DbException;
import com.global.Global;

public class API {

	protected Connection dbConn = null;

	protected static final String isSuccess = "Success";
	protected static final String isFail = "Fail";
	
	protected String procName;
	
	protected ErrRecord errRec = null;
	
	public void init(String procName) throws DbException {
		this.procName = procName;
		getConnection();
	}

	public void error(Throwable t) {
		errRec = new ErrRecord();
		errRec.setErrCode(isFail);
		errRec.setErrMessage(t.getMessage());
		Global.getLogger.error(procName, t.getMessage(), t);
	}

	public void getConnection() throws DbException {
		if (dbConn == null) {
			dbConn = DatabaseHelper.getConnection();
		}
	}

	public void finish(Response resp) {
		if(errRec != null && isFail.equals(errRec.getErrCode())) {
			resp.setErrMessage(errRec.getErrMessage());
			resp.setStatus(errRec.getErrCode());
		}else {
			resp.setStatus(isSuccess);
		}
		DatabaseHelper.commit(dbConn);
		Global.getLogger.info(this.getClass().getName(), procName, "processing complete");
	}

}

class ErrRecord {
	private String errCode = null;
	private String errMessage = null;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
}