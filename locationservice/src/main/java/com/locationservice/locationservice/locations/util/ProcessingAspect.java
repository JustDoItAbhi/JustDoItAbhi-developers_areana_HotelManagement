package com.locationservice.locationservice.locations.util;


import com.locationservice.locationservice.locations.util.query.HibernateStatisticsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class ProcessingAspect {
    private static final Logger LOGGER= LoggerFactory.getLogger(ProcessingAspect.class);

    private static final AtomicLong totalRequest = new AtomicLong();
    private static final AtomicLong failedRequests = new AtomicLong();
    private static final AtomicLong activeRequests = new AtomicLong();

    private final HibernateStatisticsService statisticsService;

    public ProcessingAspect(
            HibernateStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Around("@annotation(tracking)")
    public Object processingTime(
            ProceedingJoinPoint joinPoint,
            Tracking tracking) throws Throwable {

        activeRequests.incrementAndGet();
        totalRequest.incrementAndGet();

        statisticsService.clear();

        long startTime = System.nanoTime();

        try {
            return joinPoint.proceed();

        } catch (Exception e) {

            failedRequests.incrementAndGet();
            throw e;

        } finally {

            activeRequests.decrementAndGet();

            long responseTime =
                    TimeUnit.NANOSECONDS.toMillis(
                            System.nanoTime() - startTime);

            long queryCount =
                    statisticsService.getQueryCount();

            double errorPercentage =
                    totalRequest.get() == 0 ? 0 :
                            ((double) failedRequests.get()
                                    / totalRequest.get()) * 100;

            ProcessingMatrics metrics =
                    ProcessingMatrics.builder()
                            .apiName(joinPoint.getSignature().getName())
                            .apiResponseTime(responseTime)
                            .databaseQueryCount(queryCount)
                            .memoryUsage(getMemoryUsage())
                            .numberOfActiveUsers(activeRequests.get())
                            .errorTime(errorPercentage)
                            .build();
LOGGER.info("PROFORMANCE MATRIX LOGS "+ metrics.toString());
            metrics.print();
        }
    }

    private String getMemoryUsage() {

        Runtime runtime = Runtime.getRuntime();

        long usedMemory =
                (runtime.totalMemory() - runtime.freeMemory())
                        / (1024 * 1024);

        return usedMemory + " MB";
    }
}