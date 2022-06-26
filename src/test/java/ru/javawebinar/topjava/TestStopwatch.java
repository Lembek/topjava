package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestStopwatch extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(TestStopwatch.class);
    private static final List<String> info = new ArrayList<>();

    @Override
    protected void finished(long nanos, Description description) {
        String timeInfo = String.format("%40s - %d milliseconds",
                description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        log.info(timeInfo);
        info.add(timeInfo + "\n");
    }

    public static void printInfo() {
        StringBuilder sb = new StringBuilder("\n");
        info.forEach(sb::append);
        log.info(sb.toString());
    }
}
