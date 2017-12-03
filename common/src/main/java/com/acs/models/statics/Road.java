package com.acs.models.statics;

import com.acs.models.Location;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Road {

    private final Long id = id();

    private List<Location> points = new LinkedList<>();

    private RoadType roadType;

    public Road(RoadType roadType) {
        this.roadType = roadType;
    }

    public void addPoint(Location location){
        points.add(location);
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
