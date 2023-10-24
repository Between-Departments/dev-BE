package com.gwakkili.devbe.chat.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema
public class SaveChatMessageDto {

    @JsonIgnore
    private long senderId;

    @JsonIgnore
    private long chatRoomId;

    @Schema(description = "채팅 내용", example = "안녕하세요")
    private String content;
}
