package com.sample.request.stats.monitor;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.sample.request.stats.DataBucketCollector;

public class StatisticsFixedPool {
	final static Logger logger = Logger.getLogger(StatisticsFixedPool.class);
	public void executeCommand() throws ClientProtocolException, IOException {
		DataBucketCollector.requestCallSize = 10;
		DataBucketCollector.URL = "http://en.wikipedia.org/wiki/Main_Page";
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Runnable worker = new Runnable() {
			public void run() {
				try {
					new DataBucketCollector().initialize();
				} catch (Exception e) {
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
