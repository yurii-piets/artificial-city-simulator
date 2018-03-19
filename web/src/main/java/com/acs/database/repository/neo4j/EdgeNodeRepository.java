package com.acs.database.repository.neo4j;

import com.acs.database.node.EdgeNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeNodeRepository extends Neo4jRepository<EdgeNode, Long> {
}
