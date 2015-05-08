package com.sample.request.stats;


/**
 * An object that collects new values incrementally.

 */
public interface Collector {

    /**
     * Adds value to the collected data.
     */
    void dataCollector(double val);

} 