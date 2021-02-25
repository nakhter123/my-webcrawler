package com.my.webcrawler.crawler;

public interface Crawler {
	public boolean isURLProcessed(String urlStr);
	public void processURL(String urlStr);
}
