package com.sample.request.stats;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

public class DataCollectorTest {

	@Test
	public void testPercentile() throws ClientProtocolException, IOException {
		DataBucketCollector dc = new DataBucketCollector();
		for (int i = 0; i < 10; i++) {
			dc.addData(200+i);
		}
		Assert.assertEquals("mean is not correct", 204.5, dc.getMean() , 1);
		Assert.assertEquals("SD is not correct", 3.02, dc.getStdDev() , 2);
		Assert.assertEquals("10th percentile is not correct", 201, dc.get10thPercentile(),2);
		Assert.assertEquals("50th percentile is not correct", 205, dc.get50thPercentile(),2);
		Assert.assertEquals("90th percentile is not correct", 209, dc.get90thPercentile(),2);
		Assert.assertEquals("95th percentile is not correct", 209, dc.get95thPercentile(),2);

	}

}
