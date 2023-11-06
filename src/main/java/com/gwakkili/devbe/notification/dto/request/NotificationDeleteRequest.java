package com.gwakkili.devbe.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotificationDeleteRequest {

    private List<Long> notificationIds;
}
