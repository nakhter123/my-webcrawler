package com.my.webcrawler.crawler;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class handle two set of queues one listen to the result of parsing the URL which
 * has the Future reference holding ParseResult object.  Second queue is process the ParseResult 
 * which has all the new URL in a given web page.
 * @author Nadeem
 *
 */
public class ProcessURL {
	private static final Log logger = LogFactory.getLog(ProcessURL.class);

	private String rootURL;
	private Crawler crawler;
	private LinkedBlockingQueue<Future<ParseResult>> futureQueue;
	private LinkedBlockingQueue<ParseResult> newURLListQueue; 
	
	public void init() {
		logger.info("ProcessURL init() started");
		futureQueue = new LinkedBlockingQueue<>();
		newURLListQueue = new LinkedBlockingQueue<>();

		handleProcessURL();
		processNewURL();
		ParseResult resultObj = new ParseResult();
		resultObj.setCurrentURL(getRootURL());
		resultObj.setUrlList(Arrays.asList(getRootURL()));
		newURLListQueue.add(resultObj);
	}
	
	private void handleProcessURL() {
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Future<ParseResult> future = futureQueue.take();
						ParseResult parseResult = future.get();
						if (parseResult != null)
							newURLListQueue.add(parseResult);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		logger.info("Started handleProcessURL() thread");
	}
	
	private void processNewURL() {
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						ParseResult parseResult = newURLListQueue.take();
						
						parseResult.getUrlList().forEach(urlString -> {
							crawler.processURL(urlString);
						});
						//crawler.urlProcessed(parseResult.getCurrentURL());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		logger.info("Started processNewURL() thread");
	}

	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}

	public void setCrawler(Crawler crawler) {
		this.crawler = crawler;
	}

	public LinkedBlockingQueue<Future<ParseResult>> getFutureQueue() {
		return futureQueue;
	}
}
