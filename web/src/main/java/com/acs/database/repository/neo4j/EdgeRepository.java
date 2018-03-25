package com.acs.database.repository.neo4j;

import com.acs.models.graph.Edge;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends Neo4jRepository<Edge, Long> {
}
