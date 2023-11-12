package com.gwakkili.devbe.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteByManagerEvent {

    private Long memberId;
}
