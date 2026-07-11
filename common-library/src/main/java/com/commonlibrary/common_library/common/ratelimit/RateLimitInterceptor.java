package com.commonlibrary.common_library.common.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterAspect rateLimiterService;

    public RateLimitInterceptor( RateLimiterAspect rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RateLimit rateLimit = handlerMethod.getMethod().getAnnotation(RateLimit.class);

        if (rateLimit == null) {
            return true;
        }

        String ip = getClientIP(request);
        String uri = request.getRequestURI();
        String key = ip + ":" + uri;

        boolean allowed = rateLimiterService.allowRequest(
                key,
                rateLimit.value(),
                rateLimit.duration()
        );

        if (!allowed) {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Try again later.");
            return false;
        }

        return true;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {

            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}