package com.my.webcrawler.crawler;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class starts the crawler, this class is instantiated from the spring context.
 * The init() method will initialize the thread pool and the other variables.
 * urlQueue : All new URL will be put in this blocking queue.
 * urlProcessedSet : Set of URL which have been processed
 * executor : Thread pool.
 * 
 * @author Nadeem
 *
 */
public class CrawlerImpel implements Crawler {
	private static final Log logger = LogFactory.getLog(CrawlerImpel.class);

	private ExecutorService executor;
	private LinkedBlockingQueue<String> urlQueue;
	private Set<String> urlProcessedSet;
	private int maximumPoolSize = 2;
	private ProcessURL processURL;

	public CrawlerImpel() {

	}

	public CrawlerImpel(int maximumPoolSize) {
		setMaximumPoolSize(maximumPoolSize);
	}

	public void init() {
		logger.info("Initialzing Crawler Impl");
		urlProcessedSet = ConcurrentHashMap.newKeySet();
		urlQueue = new LinkedBlockingQueue<>();
		executor = Executors.newFixedThreadPool(maximumPoolSize);
		printProcessedURL();
		process();
	}

	public boolean isURLProcessed(String url) {
		// TODO Auto-generated method stub
		return urlProcessedSet.contains(url);
	}

	public Queue<String> getUrlQueue() {
		return urlQueue;
	}

	public void processURL(String url) {
		urlQueue.add(url);
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	/**
	 * Starts the new thread which takes the new URL from the urlQueue which is
	 * then executed on the thread from the threadpool.  The future result is handed over to 
	 * another queue to further process the result in ProcessURL. ProcessURL will take the
	 * parse result and feed back to the urlQueue in this class. 
	 */
	private void process() {

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String urlString = urlQueue.take();
						if (urlProcessedSet.contains(urlString))
							continue;

						if ((urlString.contains("google")) || urlString.contains("twitter"))
							continue;
						
						ParseHTML processHTML = new ParseHTML(urlString);
						Future<ParseResult> futureTask = executor.submit(processHTML);
						urlProcessedSet.add(urlString);

						processURL.getFutureQueue().add(futureTask);
					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}).start();
		
		logger.info("Started CrawlerImpl process() ...");
	}

	public ProcessURL getProcessURL() {
		return processURL;
	}

	public void setProcessURL(ProcessURL processURL) {
		this.processURL = processURL;
	}

	/**
	 * Prints the total count of the URL which have be processed (crawled) every 30 secs.
	 */
	public void printProcessedURL() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(30000);
						logger.info("Total Processed URL:"+urlProcessedSet.size());
						urlProcessedSet.stream().sorted().forEach(System.out::println);
					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}).start();
		logger.info("Print Thread started");
	}
}
