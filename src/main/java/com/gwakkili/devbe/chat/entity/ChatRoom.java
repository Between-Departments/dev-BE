package com.gwakkili.devbe.chat.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "chat_room",
        uniqueConstraints = @UniqueConstraint(name = "unique_master_and_member", columnNames = {"master_id", "member_id"})
)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public ChatRoom(Member master, Member member) {
        this.master = master;
        this.member = member;
    }
}
