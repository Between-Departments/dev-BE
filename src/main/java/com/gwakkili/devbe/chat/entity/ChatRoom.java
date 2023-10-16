package com.gwakkili.devbe.chat.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member master;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public ChatRoom(Member master, Member member) {
        this.master = master;
        this.member = member;
    }
}
