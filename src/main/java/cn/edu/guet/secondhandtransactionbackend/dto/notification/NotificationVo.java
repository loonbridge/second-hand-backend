package cn.edu.guet.secondhandtransactionbackend.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NotificationVo {
    private String notificationId;
    private String type;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isRead;
    private String userId;
}
