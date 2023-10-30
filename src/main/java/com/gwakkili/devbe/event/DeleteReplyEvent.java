package com.gwakkili.devbe.event;

import com.gwakkili.devbe.reply.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DeleteReplyEvent {

    List<Reply> replyList;
}
