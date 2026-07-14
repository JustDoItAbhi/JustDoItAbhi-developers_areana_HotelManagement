package com.commonlibrary.common_library.common.kafka;

import com.commonlibrary.common_library.common.kafka.EventProducer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
public class KafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ProducerFactory<String, Object> producerFactory(
            KafkaProperties properties) {

        Map<String, Object> configs = properties.buildProducerProperties(null);

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventProducer eventProducer(
            KafkaTemplate<String, Object> kafkaTemplate) {

        return new EventProducer(kafkaTemplate);
    }
}