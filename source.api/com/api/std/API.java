package com.api.std;

import java.sql.Connection;
import java.sql.SQLException;

import com.database.DatabaseHelper;
import com.database.DatabaseHelperException;
import com.global.Global;

public class API {

	protected Connection dbConn = null;

	protected static final Integer isSuccess = 1;
	protected static final Integer isFail = -1;
	
	protected String procName;
	
	protected ErrRecord errRec = null;
	
	public void init(String procName) {
		this.procName = procName;
	}

	public void error(Throwable t) {
		errRec = new ErrRecord();
		errRec.setErrCode(isFail);
		errRec.setErrMessage(t.getMessage());
		Global.getLogger.error(procName, t.getMessage(), t);
	}

	public void getConnection() throws DatabaseHelperException {
		if (dbConn != null) {
			dbConn = DatabaseHelper.getConnection();
		}
	}

	public void finish(Response resp) {
		if(errRec != null && errRec.getErrCode() == isFail) {
			resp.setErrMessage(errRec.getErrMessage());
			resp.setStatus(errRec.getErrCode());
		}else {
			resp.setStatus(isSuccess);
		}
		try {
			if (dbConn != null && !dbConn.isClosed()) {
				dbConn.close();
			}
			Global.getLogger.info(this.getClass().getName(), procName, "end of process");
		} catch (SQLException e) {
			resp.setErrMessage(e.getMessage());
			Global.getLogger.error(procName, e.getMessage(), e);
		}
	}

}

class ErrRecord {
	private Integer errCode = null;
	private String errMessage = null;

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
}