package com.gwakkili.devbe.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteMemberImageEvent {

    private String imageUrl;

}
