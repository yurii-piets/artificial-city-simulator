package com.acs.database.node;

import com.acs.models.Location;
import com.acs.models.graph.Vertex;
import com.acs.models.statics.StaticPoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@NodeEntity(label = "vertex")
public class VertexNode {

    @Id
    private Long id;

    @Relationship
    private AgentNode agent;

    private Location location;

    private Integer agentsCount;

    @Relationship
    private List<StaticPointNode> staticPoints = new LinkedList<>();

    @Relationship
    private Set<VertexNode> reachableVertices = new HashSet<>();

}
