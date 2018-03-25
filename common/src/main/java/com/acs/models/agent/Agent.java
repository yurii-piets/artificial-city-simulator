package com.acs.models.agent;

import com.acs.converter.LocationConverter;
import com.acs.models.Location;
import com.acs.models.graph.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@Data
@NodeEntity
@EqualsAndHashCode(of = "id")
public class Agent implements Comparable<Agent> {

    @Id
    private Long id;

    @Convert(LocationConverter.class)
    private Location location;

    private AgentType type;

    @JsonIgnore
    @Relationship(type = "IS_ON_VERTEX")
    private Vertex vertex;

    public Agent() {
        this.id = id();
    }

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
        if (id > staticId) {
            staticId = id + 1;
        }
    }

    public void setId(Long id) {
        this.id = id;
        if (id >= staticId) {
            staticId = id + 1;
        }
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
