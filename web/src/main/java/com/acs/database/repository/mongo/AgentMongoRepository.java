package com.acs.database.repository.mongo;

import com.acs.database.document.AgentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentMongoRepository extends MongoRepository<AgentDocument, Long> {
}