package com.acs.database.node;

import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@Data
@RelationshipEntity(type = "EDGE")
public class EdgeNode {

    @Id
    private Long id;

    @StartNode
    private VertexNode start;

    @EndNode
    private VertexNode end;

    private Integer weight;
}
