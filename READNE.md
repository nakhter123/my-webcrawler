To Run: Execute the main class
Main Class : com.wipro.interview.webcrawler.MainWebCrawler

Webcrawler Root URL: In the web_crawler.xml specify the rootUrl value its currently set to 
http://wiprodigital.com

MainWebCrawler : Main class defined here.

CrawlerImpl : This class starts the crawler, it is instantiated from the spring context.
The init() method will initialize the thread pool and the other variables.
urlQueue : All new URL will be put in this queue.
urlProcessedSet : Set of URL which have been processed
executor : Thread pool.

ProcessURL:This class handle two set of queues one listen to the result of parsing the URL which
has the Future reference holding ParseResult object.  Second queue is process the ParseResult 
which has all the new URL in a given web page.

ParseHTML: Given the URL string it parses the URL using the JSoup tool, it will extract all the 
href, images and other links and creates a result which holds a collection URLs for the given 
page.

ParseResult: Holds the URL string to parse and collection of links for href on the given page,
links for media and other type of links.
