package com.acs.database.repository.neo4j;

import com.acs.database.node.VertexNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VertexNodeRepository extends Neo4jRepository<VertexNode, Long> {
}
