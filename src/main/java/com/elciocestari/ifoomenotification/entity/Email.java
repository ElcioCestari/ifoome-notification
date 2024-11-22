package com.elciocestari.ifoomenotification.entity;


import java.io.Serializable;

public record Email(
        String sender,
        String content,
        String destiny
) implements Notification, Serializable {
}
