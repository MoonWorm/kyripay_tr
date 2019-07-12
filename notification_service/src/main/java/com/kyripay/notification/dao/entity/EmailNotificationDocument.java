package com.kyripay.notification.dao.entity;

import com.kyripay.notification.domain.vo.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document
@Data
public class EmailNotificationDocument extends GenericNotification {

    @Email
    private final String to;
    @NotBlank
    private final String subject;
    @NotNull
    private final Status status;

    public EmailNotificationDocument(@Email String to,
                                     @NotBlank String subject,
                                     @NotBlank String body,
                                     @NotNull Status status) {
        super(body);
        this.to = to;
        this.subject = subject;
        this.status = status;
    }

}
