package com.acs.models.graph;

import com.acs.algorithm.DistanceAlgorithm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@Getter
@Setter
@RelationshipEntity(type = "EDGE")
public class Edge {

    @Id
    private Long id;

    @StartNode
    private Vertex source;

    @EndNode
    private Vertex destination;

    @JsonIgnore
    private Double weight;

    public Edge() {
        this.id = id();
    }

    public Edge(Vertex source, Vertex destination) {
        this.id = id();
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Neither of source and destination cannot be null.");
        }

        this.source = source;
        this.destination = destination;

        initDistance();
    }

    private void initDistance() {
        this.weight = DistanceAlgorithm.distance(source.getLocation(), destination.getLocation());
    }

    public void setId(Long id){
        this.id = id;
        if(id > staticId){
            staticId = id + 1;
        }
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
