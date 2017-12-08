package com.acs.service;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import com.acs.models.graph.Edge;
import com.acs.models.graph.Graph;
import com.acs.models.statics.Road;
import com.acs.models.statics.RoadType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class GraphService {

    @Value("${simulation.map.correction.reachable.max}")
    private Double maxReachable;

    @Value("${simulation.cell.size}")
    private Double cellSize;

    @Getter
    private Graph graph = new Graph();

    private final ParserService parserService;

    @Autowired
    public GraphService(ParserService parserService) {
        this.parserService = parserService;
    }

    @PostConstruct
    public void postConstruct() {
        initGraph();
        connectCloseVertices();
        rescaleGraph();
    }

    private void initGraph() {
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

    private void connectCloseVertices() {
        Graph connectedGraph = new Graph();
        for (Edge edge1 : graph.getEdges()) {
            for (Edge edge2 : graph.getEdges()) {
                if (edge1 == edge2) {
                    continue;
                }

                Location source1 = edge1.getSource().getLocation();
                Location destination2 = edge2.getDestination().getLocation();

                if (DistanceAlgorithm.distance(source1, destination2) < maxReachable) {
                    connectedGraph.addEdge(destination2, source1);
                }
            }
            connectedGraph.addEdge(edge1);
        }

        this.graph = connectedGraph;
    }

    private void rescaleGraph() {
        Graph rescaledGraph = new Graph();
        for (Edge edge : graph.getEdges()) {
            Double distance = edge.getWeight();

            int n = (int) (distance / cellSize);

            if (n > 0) {
                Location sourceLocation = edge.getSource().getLocation();
                Location destinationLocation = edge.getDestination().getLocation();

                Location previousLocation = sourceLocation;
                Location currentLocation = null;

                for (int i = 1; i <= n; i++) {
                    currentLocation =
                            new Location(
                                    scale(sourceLocation.getLongitude(), destinationLocation.getLongitude(), i, (double) n),
                                    scale(sourceLocation.getLatitude(), destinationLocation.getLatitude(), i, (double) n)
                            );

                    rescaledGraph.addEdge(previousLocation, currentLocation);
                    previousLocation = currentLocation;
                }

                rescaledGraph.addEdge(currentLocation, destinationLocation);
            } else {
                rescaledGraph.addEdge(edge);
            }
        }

        this.graph = rescaledGraph;
    }

    private Double scale(Double s, Double d, int i, Double n) {
        Double res = ((d - s) * i / n) + s;
        return res;
    }
}