package com.sample.request.stats.monitor;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.sample.request.stats.DataCollector;
import com.sample.request.stats.DataBucketCollector;

public class StatisticsFixedPool {
	final static Logger logger = Logger.getLogger(StatisticsFixedPool.class);
	public void executeCommand(int PARALLELREQ) throws ClientProtocolException, IOException {
		DataCollector.clear();
		DataBucketCollector.URL = "http://en.wikipedia.org/wiki/Main_Page";
		ExecutorService executor = new ThreadPoolExecutor(
				PARALLELREQ, // core thread pool size
				PARALLELREQ, // maximum thread pool size
				1, // time to wait before resizing pool
				TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(PARALLELREQ, true),
				new ThreadPoolExecutor.CallerRunsPolicy());

		for (int i = 0; i < 100; i++) {
			Runnable worker = new DataCollector();
			executor.submit(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		DataCollector.printValue();
		logger.info("Done");

	}

}
