package com.gwakkili.devbe.chat.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveChatMessageDto {

    @JsonIgnore
    private long senderId;

    @JsonIgnore
    private long chatRoomId;

    @Schema(description = "채팅 내용", example = "안녕하세요")
    private String content;

    public SaveChatMessageDto(String content) {
        this.content = content;
    }
}
