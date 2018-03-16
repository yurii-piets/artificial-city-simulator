package com.acs.database.repository;

import com.acs.database.document.AgentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AgentRepository extends MongoRepository<AgentDocument, Long> {

    void deleteAllById(Collection<Long> ids);
}