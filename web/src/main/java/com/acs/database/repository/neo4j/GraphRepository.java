package com.acs.database.repository.neo4j;

import com.acs.models.graph.Graph;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends Neo4jRepository<Graph, Long> {
}
