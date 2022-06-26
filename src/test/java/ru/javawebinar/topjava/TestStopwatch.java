package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestStopwatch extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(TestStopwatch.class);
    private static final StringBuilder sb = new StringBuilder("\n");

    @Override
    protected void finished(long nanos, Description description) {
        String timeInfo = String.format("%40s - %d ms", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        log.info(timeInfo);
        sb.append(timeInfo).append("\n");
    }

    public static void printInfo() {
        log.info(sb.toString());
    }
}
