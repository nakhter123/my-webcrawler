package com.my.webcrawler.crawler;

import java.util.ArrayList;
import java.util.List;

public class ParseResult {
	private String currentURL;
	private List<String> urlList = new ArrayList<>();
	private List<String> mediaList = new ArrayList<>();;
	private List<String> importList = new ArrayList<>();;
	
	public String getCurrentURL() {
		return currentURL;
	}
	public void setCurrentURL(String currentURL) {
		this.currentURL = currentURL;
	}
	public List<String> getUrlList() {
		return urlList;
	}
	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
	public List<String> getMediaList() {
		return mediaList;
	}
	public void setMediaList(List<String> mediaList) {
		this.mediaList = mediaList;
	}
	public List<String> getImportList() {
		return importList;
	}
	public void setImportList(List<String> importList) {
		this.importList = importList;
	}
	

}
