package com.acs.database.repository.neo4j;

import com.acs.models.node.AgentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends Neo4jRepository<AgentNode, Long> {
    Iterable<Long> deleteAgentNodeByAgentId(Long agentId);
}
