package com.commonlibrary.common_library.common.aspect;

import com.commonlibrary.common_library.common.annotation.Tracking;
import com.commonlibrary.common_library.common.dto.ProcessingMetrics;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class PerformanceTrackingAspect {

    private static final AtomicLong totalRequests = new AtomicLong();
    private static final AtomicLong failedRequests = new AtomicLong();
    private static final AtomicLong activeRequests = new AtomicLong();

    @Around("@annotation(tracking)")
    public Object trackPerformance(ProceedingJoinPoint joinPoint, Tracking tracking) throws Throwable {
        activeRequests.incrementAndGet();
        totalRequests.incrementAndGet();
        long startTime = System.nanoTime();

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            failedRequests.incrementAndGet();
            throw t;
        } finally {
            activeRequests.decrementAndGet();
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

            double errorRate = totalRequests.get() == 0 ? 0.0 :
                    ((double) failedRequests.get() / totalRequests.get()) * 100.0;

            ProcessingMetrics metrics = ProcessingMetrics.builder()
                    .apiName(joinPoint.getSignature().toShortString())
                    .apiResponseTime(durationMs)
                    .memoryUsage(getMemoryUsage())
                    .errorRate(errorRate)
                    .numberOfActiveRequests(activeRequests.get())
                    .build();

            metrics.print();
        }
    }

    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        return usedMemory + " MB";
    }
}
