package com.acs.models.node;

import com.acs.models.graph.Graph;
import com.acs.models.graph.Vertex;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@NodeEntity(label = "graph")
public class GraphNode {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "VERTICES")
    private Set<Vertex> vertices = new HashSet<>();

    @Relationship(type = "START_VERTICES")
    private Set<Vertex> startVertices = new HashSet<>();

    public GraphNode(Graph graph) {
        this.vertices = new HashSet<>(graph.getVertices());
        this.startVertices = graph.getStartVertices();
    }
}
