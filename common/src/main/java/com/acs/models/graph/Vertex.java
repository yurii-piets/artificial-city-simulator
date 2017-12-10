package com.acs.models.graph;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.statics.StaticPoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Vertex {

    private final Long id = id();

    private final Location location;

    @Setter
    private Agent agent;

    @JsonIgnore
    private List<StaticPoint> staticPoints = new LinkedList<>();

    @JsonIgnore
    private Set<Vertex> reachableVertices = new HashSet<>();

    public boolean addStaticPoint(StaticPoint staticPoint) {
        return staticPoints.add(staticPoint);
    }

    public boolean addReachableVertex(Vertex vertex) {
        return reachableVertices.add(vertex);
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
