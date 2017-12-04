package com.acs.service;

import com.acs.models.Location;
import com.acs.models.graph.Graph;
import com.acs.models.statics.Road;
import com.acs.models.statics.RoadType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class GraphService {

    @Getter
    private Graph graph = new Graph();

    private final ParserService parserService;

    @Autowired
    public GraphService(ParserService parserService) {
        this.parserService = parserService;
    }

    @PostConstruct
    public void initGraph() {
        for (Road road : parserService.getRoads()) {
            if (road.getType() == RoadType.PRIMARY_LINK
                    || road.getType() == RoadType.LIVING_STREET
                    || road.getType() == RoadType.SECONDARY
                    || road.getType() == RoadType.RESIDENTIAL
                    || road.getType() == RoadType.SECONDARY_LINK
                    || road.getType() == RoadType.PRIMARY) {

                Location currentLocation;
                Location previousLocation = null;
                for (Location location : road.getPoints()) {
                    currentLocation = location;

                    if (previousLocation == null) {
                        previousLocation = currentLocation;
                        continue;
                    }
                    graph.addEdge(currentLocation, previousLocation);

                    previousLocation = currentLocation;
                }
            }
        }
    }
}