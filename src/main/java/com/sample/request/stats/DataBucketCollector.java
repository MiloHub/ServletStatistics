package com.sample.request.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class DataBucketCollector {

	private volatile List<Long> dataBuffer = new ArrayList<Long>();
	private volatile long totalms = 0L;
	private AtomicLong requestCount = new AtomicLong();
	private AtomicLong totalSquareSum = new AtomicLong();
	public static int requestCallSize = 1;
	public  static String URL = 
			"http://en.wikipedia.org/wiki/Main_Page";

	public DataBucketCollector()  {
	}

	@Override
	public String toString() {
		return new StringBuilder().append("{Stats:").append("N=")
				.append(this.requestCount).append(": Mean : ")
				.append(this.getMean()).append(" : Standard Deviation :")
				.append(this.getStdDev()).append(" : 10 th Percentile ")
				.append(this.get10thPercentile())
				.append(" : 50 th Percentile ")
				.append(this.get50thPercentile())
				.append(" : 90 th Percentile ")
				.append(this.get90thPercentile())
				.append(" : 95 th Percentile ")
				.append(this.get95thPercentile())
				.append(" : data collected ")
				.append(this.dataBuffer).append("}").toString();
	}

	public void initialize() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = null;
		try {
			for (int i = 0; i < requestCallSize; i++) {
				int timeout = 5;
				RequestConfig config = RequestConfig.custom()
						.setConnectTimeout(timeout * 1000)
						.setConnectionRequestTimeout(timeout * 1000)
						.setSocketTimeout(timeout * 1000).build();
				httpclient = HttpClientBuilder.create()
						.setDefaultRequestConfig(config).build();
				HttpGet httpget = new HttpGet(URL);

				// System.out.println("Executing request "
				// + httpget.getRequestLine());
				long startTime = System.currentTimeMillis();
			    httpclient.execute(httpget);
				// Create a custom response handler
				long elapsedTime = System.currentTimeMillis() - startTime;
				this.addData(elapsedTime);

			}
			System.out.println(this);

		} finally {
			httpclient.close();
		}
	}

	public void addData(long ms) {
		dataBuffer.add(ms);
		Collections.sort(dataBuffer);
		totalms += ms;
		requestCount.incrementAndGet();
		totalSquareSum.addAndGet(ms * ms);

	}

	public double getMean() {
		return (double) totalms / this.dataBuffer.size();
	}

	public double get10thPercentile() {
		return computePercentile(this.dataBuffer, 10);
	}

	public double get50thPercentile() {
		return computePercentile(this.dataBuffer, 50);
	}

	public double get90thPercentile() {
		return computePercentile(this.dataBuffer, 90);
	}

	public double get95thPercentile() {
		return computePercentile(this.dataBuffer, 95);
	}

	private double computePercentile(List<Long> dataBuffer, double percent) {
		if (dataBuffer.size() <= 0) {
			return 0.0;
		} else if (percent <= 0.0) {
			return dataBuffer.get(0);
		} else if (percent >= 100.0) {
			return dataBuffer.get(dataBuffer.size() - 1);
		}
		double rank = (percent / 100.0) * (dataBuffer.size());
		int low = (int) Math.floor(rank);
		int high = (int) Math.ceil(rank);
		if (high >= dataBuffer.size()) {
			return dataBuffer.get(dataBuffer.size() - 1);
		} else if (low == high) {
			return dataBuffer.get(low);
		} else {
			return dataBuffer.get(low) + (rank - low)
					* (dataBuffer.get(high) - dataBuffer.get(low));
		}
	}

	
	public double variance() {
		if (this.dataBuffer.size() == 0)
			return Double.NaN;
		double avg = this.getMean();
		double sum = 0.0;
		for (int i = 0; i < this.dataBuffer.size(); i++) {
			sum += (this.dataBuffer.get(i) - avg)
					* (this.dataBuffer.get(i) - avg);
		}

		return sum / (this.dataBuffer.size() - 1);
	}

	public double getStdDev() {
		return Math.sqrt(variance());
	}

}
