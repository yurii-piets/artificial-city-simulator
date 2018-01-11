package com.acs.models.statics;

import com.acs.models.Location;
import lombok.Getter;

import java.util.Deque;
import java.util.LinkedList;

@Getter
public class Road {

    private final Long id = id();
    private Deque<Location> points = new LinkedList<>();
    private RoadType type;

    public Road(RoadType roadType) {
        this.type = roadType;
    }

    public void addPoint(Location location) {
        points.addFirst(location);
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
