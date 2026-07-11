package com.commonlibrary.common_library.common.outbox;

import com.commonlibrary.common_library.common.exception.EventProcessingException;
import com.commonlibrary.common_library.common.model.OutboxEvent;
import com.commonlibrary.common_library.common.outbox.repo.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class OutboxPublisher {

@Autowired
private  OutboxRepository outboxRepository;

    @Autowired private  KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired private  ObjectMapper objectMapper;
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findPendingEvents();

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Found {} pending outbox events to publish", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                publishEvent(event);
            } catch (Exception e) {
                log.error("Failed to publish outbox event: {}", event.getId(), e);
                outboxRepository.markAsFailed(event.getId(), e.getMessage());
            }
        }
    }

    private void publishEvent(OutboxEvent event) {
        try {
            // Parse payload
            Object payload = objectMapper.readValue(event.getPayload(), Object.class);

            // Publish to Kafka
            kafkaTemplate.send(event.getTopic(), event.getAggregateId(), payload);

            // Mark as processed
            outboxRepository.markAsProcessed(event.getId(), Instant.now());

            log.info("Successfully published outbox event: {} to topic: {}", event.getId(), event.getTopic());

        } catch (Exception e) {
            log.error("Failed to publish outbox event: {}", event.getId(), e);
            throw new EventProcessingException("Failed to publish outbox event: " + e.getMessage(), e);
        }
    }
}