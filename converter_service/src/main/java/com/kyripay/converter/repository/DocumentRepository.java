package com.kyripay.converter.repository;

import com.kyripay.converter.dto.Document;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DocumentRepository extends MongoRepository<Document, String>
{
}
