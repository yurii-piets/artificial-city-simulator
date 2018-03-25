package com.acs.database.repository.neo4j;

import com.acs.models.statics.Relation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends Neo4jRepository<Relation, Long> {
}
