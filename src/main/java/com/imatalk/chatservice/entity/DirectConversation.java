package com.imatalk.chatservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "direct_conversations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString(exclude = {"messages"}) // to prevent the call to getMessages() method when toString() is called
public class  DirectConversation {
    private String id;
    private LocalDateTime createdAt;
    @DBRef
    private List<User> members;
    private long lastMessageNo;
    private String lastMessageId;
    private LocalDateTime lastMessageCreatedAt;
    // q: what does it mean by "lazy" for @DBRef
    @DBRef
    private List<Message> messages;
    private Map<String, Long> seenMessageTracker; // track each user's last seen message number in this conversation


    // make this method private to prevent it from being exposed to the outside
    // when this method is called, the messages field will be populated by retrieving messages from the database
    // this is done by spring data mongodb and doing it can be expensive and unoptimized!
    // the messages should only be fetched using its own repository
    @JsonIgnore
    private List<Message> getMessages() {
        return messages;
    }


    public static Map<String, Long> createDefaultSeenMessageTracker(List<User> users) {
        Map<String, Long> map = new HashMap<String, Long>();

        // by default, all members have seen message number 0 (no message seen)
        for (User member : users) {
            map.put(member.getId(), 0L);
        }

        return map;
    }


    public void addMessage(Message message) {
        // set messageNo to be the next number in the sequence
        long newMessageNo = lastMessageNo + 1;
        messages.add(message);

        // update the seen message number for the sender of the message
        for (User member : members) {
            // if the user is the sender of the message, set the seen message number to be the message number of the message
            // otherwise, set the seen message number to be the last seen message number of the user
            long seenMessageNo = member.getId().equals(message.getSenderId()) ? newMessageNo : seenMessageTracker.get(member.getId());
            seenMessageTracker.put(member.getId(), seenMessageNo);

        }
        lastMessageNo = newMessageNo;
        lastMessageId = message.getId();
        lastMessageCreatedAt = message.getCreatedAt();
    }

    public long getLastMessageNo() {
        return lastMessageNo;
    }
}
