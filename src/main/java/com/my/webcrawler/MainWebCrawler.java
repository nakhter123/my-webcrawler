/**
 * 
 */
package com.my.webcrawler;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ImportResource;
 

/**
 * @author Nadeem
 *
 */
@ImportResource("classpath:web_crawler.xml")
public class MainWebCrawler {
	private static final Log logger = LogFactory.getLog(MainWebCrawler.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try  {
			Class<?> aClass = Class.forName("com.wipro.interview.webcrawler.MainWebCrawler");
			SpringApplication app = new SpringApplication(aClass);
			app.setWebApplicationType(WebApplicationType.NONE);
			app.setLogStartupInfo(true);
			app.run(args);
		}catch(Throwable ex) {
			logger.error(ex);
			Runtime.getRuntime().halt(-1);
		}
	}

}
