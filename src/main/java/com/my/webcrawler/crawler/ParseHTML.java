package com.my.webcrawler.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class takes the URL string and uses the JSoup to parse the HTML.
 * It extracts href link, images and other links.
 * Creates the result object of type ParseResult which get further processed.
 * 
 * Any bad URL will be just spit out to the exception handler
 * @author Nadeem
 *
 */
public class ParseHTML implements Callable<ParseResult> {
	private Log logger = LogFactory.getLog(ParseHTML.class);
	private String urlString;

	public ParseHTML(String urlString) {
		this.urlString = urlString;
	}

	public ParseResult call() throws Exception {
		// TODO Auto-generated method stub
		return parseURL();
	}

	/**
	 * Uses the JSoup to parse the given URL and process the result of parsing.
	 * 
	 * @return ParseResult.
	 */
	private ParseResult parseURL() {
		ParseResult parseResult = null;

		try {
			Document doc = Jsoup.connect(urlString).get();
			Elements links = doc.select("a[href]");
			Elements media = doc.select("[src]");
			Elements imports = doc.select("link[href]");

			parseResult = getParseResult(urlString, links, media, links);
		} catch (IOException ex) {
			logger.error(ex.getMessage()+" URL:"+urlString);
		} catch (Exception ex) {
			logger.error(ex.getMessage()+" URL:"+urlString);
		}

		return parseResult;
	}

	private ParseResult getParseResult(String urlString, Elements links, Elements media, Elements imports) {
		ParseResult result = new ParseResult();
		
		result.setCurrentURL(urlString);
		if (links != null)
			links.forEach(link -> {
				result.getUrlList().add(link.attr("abs:href"));
			});

		if (media != null)
			media.forEach(link -> {
				result.getMediaList().add(link.attr("abs:src"));
			});

		if (imports != null)
			imports.forEach(link -> {
				result.getImportList().add(link.attr("abs:href"));
			});
		
		return result;
	}

}
