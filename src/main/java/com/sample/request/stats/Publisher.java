package com.sample.request.stats;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;



public class Publisher {
	final static Logger logger = Logger.getLogger(Publisher.class);
    private static final String THREAD_NAME = "DataPublisher";
    private static final boolean DAEMON_THREADS = true;
    private static ScheduledExecutorService sharedExecutor = null;

    private final AbstractAccumulator accumulator;
    private final long delayMillis;
    private Future<?> future = null;

    public Publisher(AbstractAccumulator accumulator,
                         long delayMillis) {
        this.accumulator = accumulator;
        this.delayMillis = delayMillis;
    }

    public AbstractAccumulator getDataAccumulator() {
        return accumulator;
    }

    public synchronized boolean isRunning() {
        return (future != null);
    }

    public synchronized void start() {
        if (future == null) {
            Runnable task = new Runnable() {
                                public void run() {
                                    try {
                                        accumulator.publish();
                                    } catch (Exception e) {
                                        handleException(e);
                                    }
                                }
                            };
            future = getExecutor().scheduleWithFixedDelay(task,
                                                          delayMillis, delayMillis,
                                                          TimeUnit.MILLISECONDS);
        }
    }

  
    protected synchronized ScheduledExecutorService getExecutor() {
        if (sharedExecutor == null) {
            sharedExecutor = Executors.newScheduledThreadPool(1, new PublishThreadFactory());
        }
        return sharedExecutor;
    }

    private static final class PublishThreadFactory implements ThreadFactory {
        PublishThreadFactory() { }
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, THREAD_NAME);
            t.setDaemon(DAEMON_THREADS);
            return t;
        }
    }

    public synchronized void stop() {
        if (future != null) {
            future.cancel(false);
            future = null;
        }
    }
    protected void handleException(Exception e) {
       logger.error(e.getMessage(), e);
    }

}
