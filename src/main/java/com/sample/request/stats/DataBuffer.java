package com.sample.request.stats;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBuffer extends Distribution {

	private final Lock lock;
	private final double[] buf;
	private int size;
	private int insertPos;

	public DataBuffer(int capacity) {
		lock = new ReentrantLock();
		buf = new double[capacity];
		size = 0;
		insertPos = 0;
	}

	public Lock getLock() {
		return lock;
	}

	public int getCapacity() {
		return buf.length;
	}

	public int getSampleSize() {
		return size;
	}

	@Override
	public void clear() {
		super.clear();
		size = 0;
		insertPos = 0;
	}

	/**
	 * Notifies the buffer that data is collection is now enabled.
	 */
	public void startCollection() {
		clear();
	}

	public void endCollection() {
		Arrays.sort(buf, 0, size);
	}

	@Override
	public void dataCollector(double val) {
		super.dataCollector(val);
		buf[insertPos++] = val;
		if (insertPos >= buf.length) {
			insertPos = 0;
			size = buf.length;
		} else if (insertPos > size) {
			size = insertPos;
		}
	}

	/**
	 * @see <a href="http://en.wikipedia.org/wiki/Percentile">Percentile
	 *      (Wikipedia)</a>
	 * 
	 */
	public double[] getPercentiles(double[] percents, double[] percentiles) {
		for (int i = 0; i < percents.length; i++) {
			percentiles[i] = computePercentile(percents[i]);
		}
		return percentiles;
	}

	private double computePercentile(double percent) {
		if (size <= 0) {
			return 0.0;
		} else if (percent <= 0.0) {
			return buf[0];
		} else if (percent >= 100.0) {
			return buf[size - 1];
		}

		double index = (percent / 100.0) * size;
		int iLow = (int) Math.floor(index);
		int iHigh = (int) Math.ceil(index);
		if (iHigh >= size) {
			return buf[size - 1];
		} else if (iLow == iHigh) {
			return buf[iLow];
		} else {
			return buf[iLow] + (index - iLow) * (buf[iHigh] - buf[iLow]);
		}
	}

	@Override
	public String toString() {

		return new StringBuilder()
				.append("{Stats:")
				.append("N=")
				.append(this.size)
				.append(": Data Collected")
				.append(Arrays.toString(buf))
				.append(": Mean : ")
				.append(this.getMean())
				.append(" : Standard Deviation :")
				.append(this.getStdDev())
				.append(" : 10 th Percentile ")
				.append(this.getPercentiles(new double[] { 10 }, new double[1])[0])
				.append(" : 50 th Percentile ")
				.append(this.getPercentiles(new double[] { 50 }, new double[1])[0])
				.append(" : 90 th Percentile ")
				.append(this.getPercentiles(new double[] { 90 }, new double[1])[0])
				.append(" : 95 th Percentile ")
				.append(this.getPercentiles(new double[] { 95 }, new double[1])[0])
				.toString();

	}

}
