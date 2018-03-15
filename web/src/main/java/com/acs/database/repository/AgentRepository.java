package com.acs.database.repository;

import com.acs.database.document.AgentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgentRepository extends MongoRepository<AgentDocument, Long> {

}