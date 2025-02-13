package com.elciocestari.ifoomenotification.service;

import com.elciocestari.ifoomenotification.entity.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static java.time.Duration.ofSeconds;
import static java.util.Collections.singletonList;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageConsumerService {
    private final ObjectMapper objectMapper;
    private final ConsumerFactory<String, String> consumerFactory;
    private final NotificationService<Email> notificationService; //TODO - should implement factory to inject
    private final ProducerService producerService;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void listen() {
        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(singletonList(this.topic));

            // Poll messages
            ConsumerRecords<String, String> records = consumer.poll(ofSeconds(10));
            if (records.isEmpty()) {
                log.info("No new messages found.");
                return;
            }

            log.info("-------------------- STARTING MESSAGE PROCESSING --------------------");

            for (ConsumerRecord<String, String> r : records) {
                try {
                    log.info("Received message: {}", getValueAsString(r.value()));

                    if (!notificationService.send(null)) {
                        log.warn("Processing failed, message will remain in topic for retry.");
                        producerService.send(r.value());
                    }

                    consumer.commitSync(Collections.singletonMap(
                            new TopicPartition(r.topic(), r.partition()),
                            new OffsetAndMetadata(r.offset() + 1)
                    )); // ✅ Acknowledge only if processed successfully

                } catch (Exception e) {
                    log.error("Error processing message, it will be retried later: {}", r.value(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error reading Kafka messages from topic", e);
        }
    }

    private String getValueAsString(String value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Error converting record to string", e);
            throw new RuntimeException("Error converting record to string");
        }
    }
}
