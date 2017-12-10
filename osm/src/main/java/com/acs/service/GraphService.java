package com.acs.service;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import com.acs.models.graph.Edge;
import com.acs.models.graph.Graph;
import com.acs.models.graph.Vertex;
import com.acs.models.statics.Road;
import com.acs.models.statics.RoadType;
import com.acs.models.statics.StaticPoint;
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
        putStaticsOnGraph();
        initStartVertex();
    }

    private void initGraph() {
        for (Road road : parserService.getRoads()) {
            if (road.getType() == RoadType.PRIMARY_LINK
                    || road.getType() == RoadType.SECONDARY_LINK
                    || road.getType() == RoadType.PRIMARY
                    || road.getType() == RoadType.SECONDARY
                    || road.getType() == RoadType.RESIDENTIAL) {

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
        for (Vertex vertex1 : graph.getVertices()) {
            for (Vertex vertex2 : graph.getVertices()) {
                if (vertex1 == vertex2) {
                    continue;
                }

                Location locationVertex1 = vertex1.getLocation();
                Location locationVertex2 = vertex2.getLocation();

                if (DistanceAlgorithm.distance(locationVertex1, locationVertex2) <= maxReachable) {
                    graph.addEdge(locationVertex1, locationVertex2);
                    graph.addEdge(locationVertex2, locationVertex1);
                }
            }
        }
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

    private void putStaticsOnGraph() {
        for (StaticPoint staticPoint : parserService.getStatics()) {
            Location location = staticPoint.getLocation();
            Vertex vertex = graph.getClosestVertexForLocation(location);
            vertex.addStaticPoint(staticPoint);
        }
    }

    private void initStartVertex() {
        nextVertex:
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : graph.getEdges()) {
                if (edge.getDestination().equals(vertex)) {
                    continue nextVertex;
                }
            }

            graph.addStartVertex(vertex);
        }
    }

    private Double scale(Double s, Double d, int i, Double n) {
        Double res = ((d - s) * i / n) + s;
        return res;
    }
}