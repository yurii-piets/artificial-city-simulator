package com.acs.database.repository.neo4j;

import com.acs.models.graph.Vertex;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VertexRepository extends Neo4jRepository<Vertex, Long> {

    Iterable<Vertex> findAllByAgent_Id(Iterable<Long> id);
}
