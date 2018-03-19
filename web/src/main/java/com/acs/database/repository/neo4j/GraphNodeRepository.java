package com.acs.database.repository.neo4j;

import com.acs.database.node.GraphNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphNodeRepository extends Neo4jRepository<GraphNode, Long> {
}
