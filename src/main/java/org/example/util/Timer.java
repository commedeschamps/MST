package org.example.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Measures execution time and computes median (excluding I/O).
 */
public class Timer {

    public static double measureMedian(Runnable task, int runs) {
        List<Long> times = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            times.add(end - start);
        }

        Collections.sort(times);
        long medianNanos = times.get(runs / 2);
        return medianNanos / 1_000_000.0;
    }
}


