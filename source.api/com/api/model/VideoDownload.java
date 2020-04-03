package com.api.model;

public class VideoDownload {
	private String downloadURL;
	private String videoType;
	private String saveURI;
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public String getSaveURI() {
		return saveURI;
	}
	public void setSaveURI(String saveURI) {
		this.saveURI = saveURI;
	}
	
}
