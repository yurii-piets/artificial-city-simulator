package com.acs.database.repository.neo4j;

import com.acs.models.graph.Vertex;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VertexRepository extends Neo4jRepository<Vertex, Long> {

    @Query("MATCH ()-[r:CONTAINS_AGENT]-() DELETE r")
    void dropOldRelations();
}
