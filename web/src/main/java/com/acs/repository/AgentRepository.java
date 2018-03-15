package com.acs.repository;

import com.acs.document.AgentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgentRepository extends MongoRepository<AgentDocument, Long> {

}