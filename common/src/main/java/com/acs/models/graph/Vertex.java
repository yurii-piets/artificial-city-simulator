package com.acs.models.graph;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.statics.StaticPoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class Vertex {

    @Getter
    private final Long id = id();

    @Getter
    private final Location location;

    @Getter
    private Agent agent;

    @JsonIgnore
    private List<StaticPoint> staticPoints = new LinkedList<>();

    @Getter
    @JsonIgnore
    private Set<Vertex> reachableVertices = new HashSet<>();

    public boolean addStaticPoint(StaticPoint staticPoint) {
        return staticPoints.add(staticPoint);
    }

    public boolean addReachableVertex(Vertex vertex) {
        return reachableVertices.add(vertex);
    }

    public boolean setAgent(Agent agent) {
        if(this.agent == null) {
            this.agent = agent;
            return true;
        }
        return false;
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
