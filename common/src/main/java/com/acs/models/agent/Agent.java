package com.acs.models.agent;

import com.acs.models.Location;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class Agent {
    private final Long id = id();
    private AgentType type;
    private Location location;
    private List<Location> destinations;
    private Double speed;

    public void addDestination(Location location) {
        if (destinations == null) {
            destinations = new LinkedList<>();
        }

        destinations.add(location);
    }


    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
