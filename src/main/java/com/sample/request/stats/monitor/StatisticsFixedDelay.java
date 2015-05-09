package com.sample.request.stats.monitor;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.sample.request.stats.DataDistribution;
import com.sample.request.stats.Percent;
import com.sample.request.stats.Publisher;
/**
 * Provides stats information with fixed delay in millisec for parallel request.
 * 
 * executeCommand uses publishInterval value to define the fixed delay.
 * 
 *
 */
public class StatisticsFixedDelay {
	
	
	final static Logger logger = Logger.getLogger(StatisticsBucketFixedPool.class);
	private static final double[] PERCENTS = makePercentValues();

	private DataDistribution dataDist = new DataDistribution(1, PERCENTS); 
	private Publisher publisher = null;

	int bufferSize = 10;
	int publishInterval = 100;

	// Server server;

	AtomicLong totalRequests = new AtomicLong();

	/**
	 * Initializes the object, starting data collection and reporting.
	 */
	public void initialize() {
		if (publisher == null) {
			dataDist = new DataDistribution(getBufferSize(), PERCENTS);
			publisher = new Publisher(dataDist, getPublishIntervalMillis());
			publisher.start();
		}
	}

	public void close() {
		if (publisher != null)
			publisher.stop();
	}

	private int getBufferSize() {
		return bufferSize;
	}

	private long getPublishIntervalMillis() {
		return publishInterval;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setPublishInterval(int publishInterval) {
		this.publishInterval = publishInterval;
	}

	private static double[] makePercentValues() {
		Percent[] percents = Percent.values();
		double[] p = new double[percents.length];
		for (int i = 0; i < percents.length; i++) {
			p[i] = percents[i].getValue();
		}
		return p;
	}

	/**
	 * /** Call this method to note the response time after every request
	 * 
	 * @param msecs
	 */
	public void noteResponseTime(double msecs) {
		dataDist.dataCollector(msecs);
	}

	public void incrementNumRequests() {
		totalRequests.incrementAndGet();
	}

	public void executeCommand() throws ClientProtocolException, IOException {
		this.setBufferSize(1000);
		this.setPublishInterval(3000);
		this.initialize();
		CloseableHttpClient httpclient = null;
		try {
			for (int i = 0; i < 100; i++) {
				int timeout = 5;
				RequestConfig config = RequestConfig.custom()
						.setConnectTimeout(timeout * 1000)
						.setConnectionRequestTimeout(timeout * 1000)
						.setSocketTimeout(timeout * 1000).build();
				httpclient = HttpClientBuilder.create()
						.setDefaultRequestConfig(config).build();
				HttpGet httpget = new HttpGet(
						"http://en.wikipedia.org/wiki/Main_Page");

				// System.out.println("Executing request "
				// + httpget.getRequestLine());
				long startTime = System.currentTimeMillis();
				HttpResponse response = httpclient.execute(httpget);
				// Create a custom response handler
				long elapsedTime = System.currentTimeMillis() - startTime;
				this.noteResponseTime(elapsedTime);
				this.incrementNumRequests();

			}

		} finally {
			httpclient.close();
		}
		logger.info("done ---");
		this.publisher.stop();

	}

}
