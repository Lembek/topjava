package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoggerUtil extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(LoggerUtil.class);
    private static final List<String> info = new ArrayList<>();

    private void logInfo(Description description, String status, long nanos) {
        String timeInfo = String.format("Test %s - spent %d microseconds",
                description.getMethodName(), TimeUnit.NANOSECONDS.toMicros(nanos));
        log.info(timeInfo);
        info.add(timeInfo);
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }

    public static void printInfo() {
        info.forEach(log::info);
    }
}
