package com.kyripay.notification.dao.repository;

import com.kyripay.notification.dao.entity.EmailNotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailNotificationRepository extends MongoRepository<EmailNotificationDocument, String> {

}
