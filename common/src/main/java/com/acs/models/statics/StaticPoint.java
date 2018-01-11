package com.acs.models.statics;

import com.acs.models.Location;
import lombok.Data;

@Data
public class StaticPoint implements Comparable<StaticPoint> {

    private final Long id = id();

    private Location location;

    private StaticType type;

    private volatile boolean locked = false;

    private Relation relation;

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
