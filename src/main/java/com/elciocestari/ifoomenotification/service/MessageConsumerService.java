package com.elciocestari.ifoomenotification.service;

import com.elciocestari.ifoomenotification.entity.Email;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageConsumerService {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        try {
            log.info("Received Message in group: " + objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Error to read kafka message in topic", e);
            throw new RuntimeException(e);
        }
    }
}
