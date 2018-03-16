package com.acs.models.agent;

import com.acs.models.Location;
import com.acs.models.graph.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
public class Agent implements Comparable<Agent> {

    private final Long id;

    private Location location;

    private AgentType type;

    @JsonIgnore
    private Vertex vertex;

    @Builder
    public Agent(Location location, AgentType type, Vertex vertex) {
        this.location = location;
        this.type = type;
        this.vertex = vertex;
        this.id = id();
    }

    @Builder
    public Agent(Location location, AgentType type, Vertex vertex, Long id) {
        this.location = location;
        this.type = type;
        this.vertex = vertex;
        this.id = id;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
        if (vertex != null) {
            this.location = vertex.getLocation();
        }
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }

    @Override
    public int compareTo(Agent o) {
        return id.compareTo(o.id);
    }
}
