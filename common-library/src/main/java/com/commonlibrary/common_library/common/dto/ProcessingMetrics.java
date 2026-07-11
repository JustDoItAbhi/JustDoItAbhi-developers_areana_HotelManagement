package com.commonlibrary.common_library.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class ProcessingMetrics {
    private long apiResponseTime;
    private String memoryUsage;
    private String apiName;
    private double errorRate;
    private long numberOfActiveRequests;

    public void print() {
        log.info("\n===== PERFORMANCE METRICS =====\n" +
                "API Name: {}\n" +
                "Response Time: {} ms\n" +
                "Memory Usage: {}\n" +
                "Error Rate: {}%\n" +
                "Active Requests: {}\n" +
                "===============================",
                apiName, apiResponseTime, memoryUsage, errorRate, numberOfActiveRequests);
    }
}
