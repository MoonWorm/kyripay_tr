package com.kyripay.notification.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document
@Data
@AllArgsConstructor
abstract class GenericNotification {

    @Id
    private String id;
    @NotBlank
    private String body;

}
