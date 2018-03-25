package com.acs.service;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import com.acs.models.graph.Edge;
import com.acs.models.graph.Graph;
import com.acs.models.graph.Vertex;
import com.acs.models.Road;
import com.acs.models.RoadType;
import com.acs.models.statics.StaticPoint;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final ParserService parserService;

    @Value("${simulation.map.correction.reachable.max}")
    private Double maxReachable;

    @Value("${simulation.cell.size}")
    private Double cellSize;

    @Getter
    private Graph graph = new Graph();

    public void processGraph() {
        initGraph();
        connectCloseVertices();
        rescaleGraph();
        putStaticsOnGraph();
        initStartVertex();
    }

    private void initGraph() {
        logger.info("Initializing graph.");
        for (Road road : parserService.getRoads()) {
            if (RoadType.isInCarGroup(road.getType())) {

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
        logger.info("Connecting close vertices.");
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
        logger.info("Rescaling graph.");
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
        logger.info("Putting statics on graph.");
        for (StaticPoint staticPoint : parserService.getStatics()) {
            Location location = staticPoint.getLocation();
            Vertex vertex = graph.getClosestVertexForLocation(location);
            vertex.addStaticPoint(staticPoint);
            staticPoint.setVertex(vertex);
        }
    }

    private void initStartVertex() {
        logger.info("Initializing start vertices.");
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
        return ((d - s) * i / n) + s;
    }
}