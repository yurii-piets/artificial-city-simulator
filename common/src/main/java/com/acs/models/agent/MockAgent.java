package com.acs.models.agent;

import com.acs.models.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

@Data
public class MockAgent implements Comparable<MockAgent> {
    private final Long id = id();

    private Location location;

    private AgentType type;

    @JsonIgnore
    private Double dLatitude;

    @JsonIgnore
    private Double dLongitude;

    @JsonIgnore
    private Queue<Location> destinations;

    @JsonIgnore
    private Deque<Location> reachedDestination;

    @Builder
    public MockAgent(AgentType type, Location location, Double dLatitude, Double dLongitude) {
        this.type = type;
        this.location = location;
        this.dLatitude = dLatitude;
        this.dLongitude = dLongitude;

        this.destinations = new LinkedList<>();
        this.reachedDestination = new LinkedList<>();
    }

    public void move() {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        location.setLatitude(latitude + dLatitude);
        location.setLongitude(longitude + dLongitude);
    }

    public void addDestination(Location location) {
        destinations.add(location);
    }

    public void reachDestination() {
        if (destinations != null && !destinations.isEmpty()) {
            Location destination = destinations.poll();
            reachedDestination.addFirst(destination);
            location = destination;
        }
    }
    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }

    @Override
    public int compareTo(MockAgent mo) {
        return id.compareTo(mo.id);
    }
}
