package com.sample.request.stats.monitor;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.sample.request.stats.DataBucketCollector;
/**
 * Provides stats information with fixed pool size for parallel request.
 * 
 * executeCommand uses parallelreq value to define the pool size.
 * 
 * With Fixed bucketed size
 * 
 *
 */
public class StatisticsBucketFixedPool {
	final static Logger logger = Logger.getLogger(StatisticsBucketFixedPool.class);
	public void executeCommand() throws ClientProtocolException, IOException {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Runnable worker = new Runnable() {
			public void run() {
				try {
					new DataBucketCollector().initialize();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		};
		for (int i = 0; i < DataBucketCollector.requestCallSize; i++) {
			executor.submit(worker);

		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		logger.info("Done");

	}

}
