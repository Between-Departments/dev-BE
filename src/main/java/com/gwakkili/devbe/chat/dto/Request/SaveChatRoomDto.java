package com.gwakkili.devbe.chat.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveChatRoomDto {

    @JsonIgnore
    private long masterId;

    @Schema(description = "상대 회원 번호", example = "5")
    private long memberId;

    public SaveChatRoomDto(long memberId) {
        this.memberId = memberId;
    }
}
