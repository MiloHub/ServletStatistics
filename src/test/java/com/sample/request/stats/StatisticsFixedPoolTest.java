package com.sample.request.stats;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;

import com.sample.request.stats.monitor.StatisticsFixedPool;

public class StatisticsFixedPoolTest {

	@Before
	public void setUp() {
		DataCollector.clear();
		DataCollector.URL = "http://en.wikipedia.org/wiki/Main_Page";
	}

	@Test
	public void testSampleRequest100Request() {
		ExecutorService executor =
				  new ThreadPoolExecutor(
				    10, // core thread pool size
				    10, // maximum thread pool size
				    1, // time to wait before resizing pool
				    TimeUnit.MINUTES, 
				    new ArrayBlockingQueue<Runnable>(10, true),
				    new ThreadPoolExecutor.CallerRunsPolicy());
		for (int i = 0; i < 100; i++) {
			Runnable worker = new DataCollector();
			executor.submit(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		DataCollector.printValue();
	}
	
	@Test
	public void testSampleRequest20Bucket100Request() {
		ExecutorService executor =
				  new ThreadPoolExecutor(
				    20, // core thread pool size
				    20, // maximum thread pool size
				    1, // time to wait before resizing pool
				    TimeUnit.MINUTES, 
				    new ArrayBlockingQueue<Runnable>(20, true),
				    new ThreadPoolExecutor.CallerRunsPolicy());
		for (int i = 0; i < 100; i++) {
			Runnable worker = new DataCollector();
			executor.submit(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		DataCollector.printValue();
	}
	
	@Test
	public void testExecuteCommand() throws ClientProtocolException, IOException {
		StatisticsFixedPool sfp = new StatisticsFixedPool();
		sfp.executeCommand();
		
	}

}
