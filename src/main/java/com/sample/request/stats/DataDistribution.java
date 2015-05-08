package com.sample.request.stats;

import org.apache.log4j.Logger;

public class DataDistribution extends AbstractAccumulator  {
	final static Logger logger = Logger.getLogger(DataDistribution.class);
    private long numValues = 0L;
    private double mean = 0.0;
    private double variance = 0.0;
    private double stddev = 0.0;
    private int size = 0;
    private final double[] percents;
    private final double[] percentiles;

    public DataDistribution(int bufferSize, double[] percents) {
        super(bufferSize);
        this.percents = percents;
        this.percentiles = new double[percents.length];
    }

   

    protected void publish(DataBuffer buf) {
        numValues = buf.getNumValues();
        mean = buf.getMean();
        variance = buf.getVariance();
        stddev = buf.getStdDev();
        size = buf.getSampleSize();
        buf.getPercentiles(percents, percentiles);
        System.out.println("Statistics: " + buf);
        logger.debug("Statistics: " + buf);
    }

   
    
    /**
     * Gets the 10-th percentile time spent handling a request, in milliseconds.
     */
    public double getResponseTime10thPercentile() {
        return getResponseTimePercentile(Percent.TEN);
    }


    /**
     * Gets the 50-th percentile time spent handling a request, in milliseconds.
     */
    public double getResponseTime50thPercentile() {
        return getResponseTimePercentile(Percent.FIFTY);
    }

    
    /**
     * Gets the 90-th percentile time spent handling a request, in milliseconds.
     */
    public double getResponseTime90thPercentile() {
        return getResponseTimePercentile(Percent.NINETY);
    }

    /**
     * Gets the 95-th percentile time spent handling a request, in milliseconds.
     */
    public double getResponseTime95thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_FIVE);
    }

   
    /**
     * Gets the 99-th percentile time spent handling a request, in milliseconds.
     */
    public double getResponseTime99thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_NINE);
    }

   
    /**
     * Gets the 100-th percentile time spent handling a request, in milliseconds.
     */
    public double getResponseTime100thPercentile() {
        return getResponseTimePercentile(Percent.HUNDRED);
    }
   
    
    private double getResponseTimePercentile(Percent p) {
        return this.getPercentiles()[p.ordinal()];
    }
   
    public void clear() {
        numValues = 0L;
        mean = 0.0;
        variance = 0.0;
        stddev = 0.0;
        size = 0;
        for (int i = 0; i < percentiles.length; i++) {
            percentiles[i] = 0.0;
        }
    }

   
    public long getNumValues() {
        return numValues;
    }

   
    public double getMean() {
        return mean;
    }

   
    public double getVariance() {
        return variance;
    }

   
    public double getStdDev() {
        return stddev;
    }

   
    public int getSampleSize() {
        return size;
    }

   
    public double[] getPercents() {
        return percents;
    }

   
    public double[] getPercentiles() {
        return percentiles;
    }

} 