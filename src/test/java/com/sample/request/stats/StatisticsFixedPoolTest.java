package com.sample.request.stats;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

public class StatisticsFixedPoolTest {

	@Before
	public void setUp() {
		
		DataCollector.URL = "http://en.wikipedia.org/wiki/Main_Page";
	}

	@Test
	public void testSampleRequest100Request() {
		DataCollector.requestCallSize = 10;
		final int dimSize = 10;
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Runnable worker = new Runnable() {
			public void run() {
				try {
					new DataCollector();
				} catch (Exception e) {
				}
			}
		};
		for (int i = 0; i < dimSize; i++) {
			executor.submit(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
	
	@Test
	public void testSampleRequest20Bucket100Request() {
		DataCollector.requestCallSize = 20;
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Runnable worker = new Runnable() {
			public void run() {
				try {
					new DataCollector();
				} catch (Exception e) {
				}
			}
		};
		for (int i = 0; i < DataCollector.requestCallSize; i++) {
			executor.submit(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
	
	@Test
	public void testExecuteCommand() {
		
	}

}
