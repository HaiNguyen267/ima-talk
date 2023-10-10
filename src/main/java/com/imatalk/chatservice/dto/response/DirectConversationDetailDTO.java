package com.imatalk.chatservice.dto.response;

import com.imatalk.chatservice.entity.Conversation;
import com.imatalk.chatservice.entity.Message;
import com.imatalk.chatservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this object contains the list of members and the list of messages of the conversation
@Data
public class DirectConversationDetailDTO {
    private String conversationId;
    private String conversationName;
    private String conversationAvatar;
    private Map<String, MemberDTO> members; // the other user in the conversation
    // using map for easier access for the frontend
    private Map<String, DirectMessageDTO> messages;


    public DirectConversationDetailDTO(Conversation conversation, User currentUser) {
        this.conversationId = conversation.getId();

        if (conversation.isGroupConversation()) {
            this.conversationName = conversation.getGroupName();
            this.conversationAvatar = conversation.getGroupAvatar();
        } else {
            User otherUser =  getTheOtherUserInConversation(currentUser, conversation);
            // if the conversation is not a group conversation, then it is a direct conversation, and there are only 2 members in the conversation
            this.conversationName = otherUser.getDisplayName();
            this.conversationAvatar = otherUser.getAvatar();
        }
    }



    public  User getTheOtherUserInConversation(User user, Conversation conversation) {

        // there can be only 2 members in a direct conversation
        // find the other user in the conversation
        User otherUser = conversation.getMembers().get(0);
        // if the first member is the current user, then the other user is the second member
        if (otherUser.getId().equals(user.getId())) {
            otherUser = conversation.getMembers().get(1);
        }
        return otherUser;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberDTO {
        private String id;
        private String displayName;
        private String username;
        private String avatar;
        private LastSeen lastSeen; // this is the last message seen by the user in the conversation

        public MemberDTO(User user) {
            this.id = user.getId();
            this.displayName = user.getDisplayName();
            this.username = user.getUsername();
            this.avatar = user.getAvatar();
        }


        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LastSeen {
//            private String messageId;
            private long messageNo;
        }


    }

    @Data
    public static class DirectMessageDTO {
        private String id;
        private String senderId;
        private String content;
//        private String type;
        private boolean senderIsMe;
        private String createdAt;
        private long messageNo;

        private String repliedMessageId; // this is the id of the message that this message is replying to

        public DirectMessageDTO(Message message, User currentUser) {
            this.id = message.getId();
            this.senderId = message.getSenderId();
            this.content = message.getContent();
            this.senderIsMe = message.getSenderId().equals(currentUser.getId());
            this.createdAt = message.getCreatedAt().toString();
            this.messageNo = message.getMessageNo();
            this.repliedMessageId = message.getRepliedMessageId();
        }
    }



}
