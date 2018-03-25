package com.acs.database.repository.neo4j;

import com.acs.models.agent.Agent;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends Neo4jRepository<Agent, Long> {
}
