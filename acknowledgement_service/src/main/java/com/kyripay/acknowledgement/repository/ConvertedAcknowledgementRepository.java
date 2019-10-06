package com.kyripay.acknowledgement.repository;

import com.kyripay.acknowledgement.dto.ConvertedAcknowledgement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConvertedAcknowledgementRepository extends MongoRepository<ConvertedAcknowledgement, String>{

}
