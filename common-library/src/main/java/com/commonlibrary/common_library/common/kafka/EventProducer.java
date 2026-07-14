package com.commonlibrary.common_library.common.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j

public class EventProducer {


    private  KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic, Object event) {
        kafkaTemplate.send(topic, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Event sent to {}: {}", topic, event);
                    } else {
                        log.error("Failed to send event: {}", ex.getMessage());
                    }
                });
    }
}