package com.kyripay.notification.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document
@Data
@RequiredArgsConstructor
abstract class GenericNotification {

    @Id
    private String id;
    @NotBlank
    private final String body;

}
