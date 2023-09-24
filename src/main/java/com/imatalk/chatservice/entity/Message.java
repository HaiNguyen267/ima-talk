package com.imatalk.chatservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {
    // the reason why i store string as id instead of ObjectId
    // is because message will only be retrieved along with the conversation it belongs to
    // and the conversation has all the information that the message needs (user info)
    private String id;
    private long messageNo; // an incremental number for each message in the conversation it belongs to
    private String senderId;
    private String content;
    private String conversationId;
    private LocalDateTime createdAt;
    private String repliedMessageId; // this is null if the message is not a reply to another message

    public boolean isSent() {
        return id != null; // if the message has an id, it means it is stored in the database
    }

}
