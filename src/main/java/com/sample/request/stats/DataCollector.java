package com.sample.request.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class DataCollector implements Runnable {

	private static CopyOnWriteArrayList<Long> rawDataBuffer = new CopyOnWriteArrayList<Long>();
	private static List<Long> dataBuffer = new ArrayList<Long>();
	public static String URL = "http://en.wikipedia.org/wiki/Main_Page";

	public DataCollector() {
	}
	public static void clear(){
		rawDataBuffer.clear();
		dataBuffer.clear();
	}

	public static void printValue() {
		dataBuffer.addAll(rawDataBuffer);
		Collections.sort(dataBuffer);
		String sb = new StringBuilder().append("{Stats:").append("N=")
				.append(dataBuffer.size()).append(": Mean : ")
				.append(getMean()).append(" : Standard Deviation :")
				.append(getStdDev()).append(" : 10 th Percentile ")
				.append(get10thPercentile()).append(" : 50 th Percentile ")
				.append(get50thPercentile()).append(" : 90 th Percentile ")
				.append(get90thPercentile()).append(" : 95 th Percentile ")
				.append(get95thPercentile()).append(" : data collected ")
				.append(dataBuffer).append("}").toString();
		System.out.println(sb);
	}

	public void initialize() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = null;
		try {
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
			rawDataBuffer.add(elapsedTime);

		} finally {
			httpclient.close();
		}
	}

	public static double getMean() {
		double totalms = 0.0;
		for (double data : dataBuffer) {
			totalms += data;
		}
		return (double) totalms / dataBuffer.size();
	}

	public static double get10thPercentile() {
		return computePercentile(dataBuffer, 10);
	}

	public static double get50thPercentile() {
		return computePercentile(dataBuffer, 50);
	}

	public static double get90thPercentile() {
		return computePercentile(dataBuffer, 90);
	}

	public static double get95thPercentile() {
		return computePercentile(dataBuffer, 95);
	}

	private static double computePercentile(List<Long> dataBuffer,
			double percent) {

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

	public static double variance() {
		if (dataBuffer.size() == 0)
			return Double.NaN;
		double avg = getMean();
		double sum = 0.0;
		for (int i = 0; i < dataBuffer.size(); i++) {
			sum += (dataBuffer.get(i) - avg) * (dataBuffer.get(i) - avg);
		}

		return sum / (dataBuffer.size() - 1);
	}

	public static double getStdDev() {
		return Math.sqrt(variance());
	}

	public void run() {
		try {
			initialize();
		} catch (Exception e) {
		}

	}

}
