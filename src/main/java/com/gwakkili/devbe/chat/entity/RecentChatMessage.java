package com.gwakkili.devbe.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.time.LocalDateTime;

@Entity
@Subselect(
        "SELECT cm1.chat_message_id, cm1.chat_room_id, cm1.content, cm1.create_at, cm1.is_read FROM chat_message cm1 " +
                "JOIN (SELECT MAX(cm2.chat_message_id) as recent_chat_message_id " +
                "FROM chat_message cm2 group by cm2.chat_room_id) rcm " +
                "on cm1.chat_message_id = rcm.recent_chat_message_id"
)
@Immutable
@Synchronize("chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Getter
public class RecentChatMessage {

    @Id
    long chatMessageId;

    private long chatRoomId;

    private LocalDateTime createAt;

    private String content;

    private boolean isRead;

}
