package com.commonlibrary.common_library.common.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic, Object event) {

        kafkaTemplate.send(topic, event)
                .whenComplete((result, ex) -> {

                    if (ex == null) {

                        log.info(
                                "Kafka Event sent successfully Topic={} Payload={}",
                                topic,
                                event
                        );

                    } else {

                        log.error(
                                "Kafka Event Failed Topic={} Error={}",
                                topic,
                                ex.getMessage(),
                                ex
                        );
                    }

                });

    }

}