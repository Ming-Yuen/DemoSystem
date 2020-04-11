package com.api.std;

public class Response {
	private Integer status;
	private Integer successCount;
	private Integer failedCount;
	private String errMessage;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

}
