package com.acs.database.repository.neo4j;

import com.acs.models.statics.StaticPoint;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticPointRepository extends Neo4jRepository<StaticPoint, Long> {
}
