package com.acs.database.node;

import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@NodeEntity(label = "graph")
public class GraphNode {

    @Id
    private Long id;

    private String name;

    @Relationship
    private Set<EdgeNode> edges = new HashSet<>();
}
