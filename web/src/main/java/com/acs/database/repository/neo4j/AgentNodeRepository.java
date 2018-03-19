package com.acs.database.repository.neo4j;

import com.acs.database.node.AgentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentNodeRepository extends Neo4jRepository<AgentNode, Long> {
}
