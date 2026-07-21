package com.commonlibrary.common_library.common.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent  {

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, PROCESSING, PROCESSED, FAILED

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    private Integer retryCount = 0;

    private String errorMessage;
}
