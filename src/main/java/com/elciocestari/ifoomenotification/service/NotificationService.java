package com.elciocestari.ifoomenotification.service;

import com.elciocestari.ifoomenotification.entity.Notification;

public interface NotificationService<T extends Notification> {
    boolean send(T notification);
}
