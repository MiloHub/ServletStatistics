package com.sample.request.stats;

public interface IDistribution {

	int getSampleSize();

	double[] getPercents();

	double[] getPercentiles();

}
