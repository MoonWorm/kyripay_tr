package com.kyripay.notification.dao.entity;

import com.kyripay.notification.domain.vo.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document
@Data
public class EmailNotificationDocument extends GenericNotification {

    @Email
    private String to;
    @NotBlank
    private String subject;
    @NotNull
    private Status status;

    public EmailNotificationDocument(@Email String to,
                                     @NotBlank String subject,
                                     @NotBlank String body,
                                     @NotNull Status status) {
        super(null, body);
        this.to = to;
        this.subject = subject;
        this.status = status;
    }

}
