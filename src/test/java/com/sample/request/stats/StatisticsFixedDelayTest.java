package com.sample.request.stats;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import com.sample.request.stats.monitor.StatisticsFixedDelay;

public class StatisticsFixedDelayTest {
	
	@Test
	public void testStatsWithFixedDelay() throws ClientProtocolException, IOException  {
		System.out.println("test execute command with bucketed fixed deplay parallel request : start");
		final StatisticsFixedDelay ss = new StatisticsFixedDelay();
		ss.executeCommand();
		System.out.println("test execute command with bucketed fixed deplay parallel request : end");
		
	}

}
