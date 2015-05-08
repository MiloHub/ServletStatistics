package com.sample.request.stats;

import java.util.concurrent.locks.Lock;


/**
 * Abstract Data Accumulator to collect data points
 * 
 *
 */
public abstract class AbstractAccumulator implements Collector {

    private DataBuffer current;
    private DataBuffer previous;
    private final Object objLock = new Object();

    public AbstractAccumulator(int bufferSize) {
        this.current = new DataBuffer(bufferSize);
        this.previous = new DataBuffer(bufferSize);
    }
    protected abstract void publish(DataBuffer buf);
    
    public void dataCollector(double val) {
        synchronized (objLock) {
            Lock l = current.getLock();
            l.lock();
            try {
                current.dataCollector(val);
            } finally {
                l.unlock();
            }
        }
    }

    public void publish() {
        
        DataBuffer tmp = null;
        Lock l = null;
        synchronized (objLock) {
            tmp = current;
            current = previous;
            previous = tmp;
            l = current.getLock();
            l.lock();
            try {
                current.startCollection();
            } finally {
                l.unlock();
            }
            l = tmp.getLock();
            l.lock();
        }
        try {
            tmp.endCollection();
            publish(tmp);
        } finally {
            l.unlock();
        }
    }

  

} 
