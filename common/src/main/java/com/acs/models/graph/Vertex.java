package com.acs.models.graph;

import com.acs.converter.LocationConverter;
import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.statics.StaticPoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NodeEntity(label = "vertex")
@EqualsAndHashCode(of = "id")
public class Vertex {

    @Id
    @GeneratedValue
    private Long gid;

    private Long id;

    @Convert(LocationConverter.class)
    private Location location;

    private Integer agentsCount = 0;

    @JsonIgnore
    @Relationship(type = "CONTAINS")
    private Agent agent;

    @JsonIgnore
    @Relationship(type = "CONTAINS_STATIC")
    private List<StaticPoint> staticPoints = new LinkedList<>();

    @JsonIgnore
    @Relationship(type = "CAN_REACHED")
    private Set<Vertex> reachableVertices = new HashSet<>();

    public Vertex() {
        id = id();
    }

    public void addStaticPoint(StaticPoint staticPoint) {
        staticPoints.add(staticPoint);
    }

    public void addReachableVertex(Vertex vertex) {
        reachableVertices.add(vertex);
    }

    public void setId(Long id) {
        this.id = id;
        if (id >= staticId) {
            staticId = id + 1;
        }
    }

    public void setAgent(Agent agent) {
        if (agent != null && !agent.equals(this.agent)) {
            agentsCount = agentsCount + 1;
        }

        this.agent = agent;
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
