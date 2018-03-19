package com.acs.database.repository.neo4j;

import com.acs.database.node.RelationNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationNodeRepository extends Neo4jRepository<RelationNode, Long> {
}
