package com.example.akela.swim.crm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private String message;
    private String icon;
    private String color;
    private String route;
    private LocalDateTime time;

    public NotificationDTO() {
    }


}
