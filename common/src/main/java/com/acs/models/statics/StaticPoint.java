package com.acs.models.statics;

import com.acs.models.Location;
import com.acs.models.graph.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class StaticPoint implements Comparable<StaticPoint> {

    private final Long id = id();

    private Location location;

    private StaticType type;

    private volatile boolean locked = false;

    @JsonIgnore
    private Relation relation;

    @JsonIgnore
    private Vertex vertex;

    public StaticPoint(Location location, StaticType type) {
        this.location = location;
        this.type = type;
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }

    @Override
    public int compareTo(StaticPoint o) {
        return id.compareTo(o.getId());
    }
}
