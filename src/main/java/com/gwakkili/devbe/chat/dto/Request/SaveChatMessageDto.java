package com.gwakkili.devbe.chat.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SaveChatMessageDto {

    @JsonIgnore
    private long sender;

    @JsonIgnore
    private long chatRoomId;

    private String content;
}
