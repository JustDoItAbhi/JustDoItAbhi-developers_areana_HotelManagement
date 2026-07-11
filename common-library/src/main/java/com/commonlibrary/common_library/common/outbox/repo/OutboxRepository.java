package com.commonlibrary.common_library.common.outbox.repo;

import com.commonlibrary.common_library.common.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("SELECT o FROM OutboxEvent o WHERE o.status = 'PENDING' ORDER BY o.createdAt ASC")
    List<OutboxEvent> findPendingEvents();

    @Query("SELECT o FROM OutboxEvent o WHERE o.status = 'PENDING' AND o.retryCount < :maxRetries AND o.createdAt < :cutoffTime")
    List<OutboxEvent> findPendingEventsWithRetryLimit(@Param("maxRetries") int maxRetries, @Param("cutoffTime") Instant cutoffTime);

    @Modifying
    @Transactional
    @Query("UPDATE OutboxEvent o SET o.status = 'PROCESSED', o.processedAt = :processedAt WHERE o.id = :id")
    void markAsProcessed(@Param("id") UUID id, @Param("processedAt") Instant processedAt);

    @Modifying
    @Transactional
    @Query("UPDATE OutboxEvent o SET o.status = 'FAILED', o.errorMessage = :errorMessage, o.retryCount = o.retryCount + 1 WHERE o.id = :id")
    void markAsFailed(@Param("id") UUID id, @Param("errorMessage") String errorMessage);
}