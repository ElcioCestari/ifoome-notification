package com.elciocestari.ifoomenotification.service;

import com.elciocestari.ifoomenotification.entity.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationServiceImpl implements NotificationService<Email> {
    @Override
    public boolean send(Email notification) {
        if (notification != null) {
            log.error("Notification is null");
            return false;
        }
        //TODO should send email
        log.info("TODO - should send Email notification sent");
        return true;
    }
}
