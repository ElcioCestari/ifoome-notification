package com.elciocestari.ifoomenotification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        this.kafkaTemplate
                .send(topic, message)
                .whenComplete((record, ex) -> {
                    if (nonNull(ex)) {
                        log.error(ex.getMessage());
                    } else {
                        var msg = new StringBuilder();
                        msg.append("topic: ").append(record.getRecordMetadata().topic());
                        msg.append("partition: ").append(record.getRecordMetadata().partition());
                        msg.append("offset: ").append(record.getRecordMetadata().offset());
                        log.info("Msg send successfully: %s".formatted(msg.toString()));
                    }
                });
    }
}
