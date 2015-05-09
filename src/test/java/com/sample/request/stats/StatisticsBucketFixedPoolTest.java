package com.sample.request.stats;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;

import com.sample.request.stats.monitor.StatisticsFixedPool;

public class StatisticsBucketFixedPoolTest {

	@Before
	public void setUp() {
		
		DataBucketCollector.URL = "http://en.wikipedia.org/wiki/Main_Page";
	}

	@Test
	public void testSampleRequest100Request() {
		DataBucketCollector.requestCallSize = 10;
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
	}
	
	@Test
	public void testSampleRequest20Bucket100Request() {
		DataBucketCollector.requestCallSize = 20;
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
	}
	
	@Test
	public void testExecuteCommand() throws ClientProtocolException, IOException {
		StatisticsFixedPool sfp = new StatisticsFixedPool();
		sfp.executeCommand();
		
	}

}
