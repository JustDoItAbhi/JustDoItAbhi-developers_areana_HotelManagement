package com.locationservice.locationservice.locations.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessingMatrics {

    private long apiResponseTime;
    private long databaseQueryCount;
    private String memoryUsage;
    private String apiName;
    private double errorTime;
    private long numberOfActiveUsers;

    public void print() {

        System.out.println("\n===== PERFORMANCE METRICS =====");
        System.out.println("API Name: " + apiName);
        System.out.println("Response Time: " + apiResponseTime + " ms");
        System.out.println("Database Queries: " + databaseQueryCount);
        System.out.println("Memory Usage: " + memoryUsage);
        System.out.println("Error Rate: " + errorTime + "%");
        System.out.println("Active Users: "+ numberOfActiveUsers);
        System.out.println("===============================");
    }
}