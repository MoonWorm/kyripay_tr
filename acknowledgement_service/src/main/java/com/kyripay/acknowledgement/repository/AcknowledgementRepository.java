package com.kyripay.acknowledgement.repository;

import com.kyripay.acknowledgement.dto.Acknowledgement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AcknowledgementRepository extends MongoRepository<Acknowledgement, String>{

}
