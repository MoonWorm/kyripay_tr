package com.kyripay.notification.dto;

import com.kyripay.notification.domain.vo.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {

    private String id;
    private Status status;

}
