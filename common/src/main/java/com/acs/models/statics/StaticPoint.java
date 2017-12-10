package com.acs.models.statics;

import com.acs.models.Location;
import lombok.Data;

@Data
public class StaticPoint {

    private final Long id = id();

    private Location location;

    private StaticType type;

    private volatile boolean locked = false;

    public StaticPoint(Location location, StaticType type) {
        this.location = location;
        this.type = type;
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
