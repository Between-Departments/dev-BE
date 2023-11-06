package com.gwakkili.devbe.chat.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema
public class ChatMessageDto {

    @Schema(description = "채팅 메시지 번호")
    private long chatMessageId;

    @Schema(description = "발신자", example = "신방과곰돌이")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String sender;

    @Schema(description = "채팅 내용", example = "안녕하세요~")
    private String content;

    @Schema(description = "생성 일자")
    private LocalDateTime createAt;


    public static ChatMessageDto of(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .sender(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent())
                .createAt(chatMessage.getCreateAt())
                .build();
    }
}
