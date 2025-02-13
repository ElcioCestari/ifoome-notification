package com.elciocestari.ifoomenotification;

import com.elciocestari.ifoomenotification.service.MessageConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class IfoomeNotificationApplication implements CommandLineRunner {
    private final MessageConsumerService messageConsumerService;

    public static void main(String[] args) {
        SpringApplication.run(IfoomeNotificationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("\n\n\nThe app are running\n\n\n");
    }
}
