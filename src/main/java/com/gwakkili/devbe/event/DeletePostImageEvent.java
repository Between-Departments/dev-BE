package com.gwakkili.devbe.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DeletePostImageEvent {

    List<String> imageUrls;

}
