package com.kyripay.notification.dto;

import com.kyripay.notification.domain.vo.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class NotificationResponse {

    private final UUID uuid;
    private final Status status;

}
