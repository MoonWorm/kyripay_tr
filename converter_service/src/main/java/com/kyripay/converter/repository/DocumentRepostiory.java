package com.kyripay.converter.repository;

import com.kyripay.converter.domain.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DocumentRepostiory extends MongoRepository<PaymentDocument, String>
{
}
