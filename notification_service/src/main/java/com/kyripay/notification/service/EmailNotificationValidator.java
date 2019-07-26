package com.kyripay.notification.service;

import com.kyripay.notification.dao.entity.EmailNotificationDocument;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class EmailNotificationValidator {

    public void validateEmailNotification(@Valid EmailNotificationDocument notification) {
        // validation is made by annotations
    }

}