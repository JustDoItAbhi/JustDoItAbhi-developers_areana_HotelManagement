package com.commonlibrary.common_library.common.ratelimit;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
public class RateLimiterAspect {
    private final Map<String, Deque<Long>> requestMap = new ConcurrentHashMap<>();

    public synchronized boolean allowRequest(String key, int maxRequests, long windowMs) {
        long now = System.currentTimeMillis();

        requestMap.putIfAbsent(key, new ArrayDeque<>());
        Deque<Long> timestamps = requestMap.get(key);

        while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > windowMs) {
            timestamps.pollFirst();
        }

        if (timestamps.size() >= maxRequests) {
            return false;
        }

        timestamps.addLast(now);
        return true;
    }
}
