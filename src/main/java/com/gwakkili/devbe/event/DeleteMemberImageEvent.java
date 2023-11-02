package com.gwakkili.devbe.event;

import com.gwakkili.devbe.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteMemberImageEvent {

    private Member member;

}
