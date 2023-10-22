package com.imatalk.chatservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {

    //TODO: add the data field for DM, GM, and NOTI

    @Id
    private String id;
    private String userId;
    private NotificationType type;
    private String content;
    private boolean unread;
    private LocalDateTime createdAt;
    private String conversationId;
    private String senderId;
    public enum NotificationType {
        //TODO: seen message notification (only for current conversation)
        D_M, // for direct message
        G_M, // for group message
        NOTI, // for notification (when other user reply or react to your message)
    }
}
