package com.gwakkili.devbe.event;

import com.gwakkili.devbe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DeletePostImageEvent {

    List<Post> postList;
}
