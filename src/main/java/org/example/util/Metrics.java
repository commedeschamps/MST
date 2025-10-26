package org.example.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe operations counter for algorithm metrics.
 */
public class Metrics {
    private final AtomicLong operations;

    public Metrics() {
        this.operations = new AtomicLong(0);
    }

    public void inc(long n) {
        operations.addAndGet(n);
    }

    public void inc() {
        operations.incrementAndGet();
    }

    public long get() {
        return operations.get();
    }

    public void reset() {
        operations.set(0);
    }
}

