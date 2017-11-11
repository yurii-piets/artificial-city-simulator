package com.acs.models.agent;

import com.acs.models.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

@Data
@Builder
public class Agent implements Comparable<Agent>, Cloneable {
    private final Long id = id();
    private AgentType type;
    private Location location;
    private Queue<Location> destinations;

    @JsonIgnore
    private Deque<Location> reachedDestination = new LinkedList<>();

    private Double speed;

    public void addDestination(Location location) {
        if (destinations == null) {
            destinations = new LinkedList<>();
        }

        destinations.add(location);
    }

    public void reachDestination(){
        if(destinations != null && !destinations.isEmpty()){
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
    public int compareTo(Agent o) {
        return id.compareTo(o.id);
    }
}

