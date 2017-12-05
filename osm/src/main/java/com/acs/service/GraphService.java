package com.acs.service;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import com.acs.models.graph.Edge;
import com.acs.models.graph.Graph;
import com.acs.models.graph.Vertex;
import com.acs.models.statics.Road;
import com.acs.models.statics.RoadType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class GraphService {

    @Value("${simulation.cell.size}")
    private Double cellSize;

    @Value("${simulation.map.correction.reachable.max}")
    private Double reachableDistanceMax;

    @Value("${simulation.map.correction.reachable.min}")
    private Double reachableDistanceMin;

    @Getter
    private Graph graph = new Graph();

    @Getter
    private final Graph rescaledGraph = new Graph();

    @Getter
    private final Graph minimizedGraph = new Graph();

    private final ParserService parserService;

    @Autowired
    public GraphService(ParserService parserService) {
        this.parserService = parserService;
    }

    @PostConstruct
    public void init() {
        // TODO: 05/12/2017 refactor all methods
        initGraph();
        rescaleGraph();
        correction();
        minimizeGraph();
    }

    public void correction() {
        for (Vertex mainVertex : rescaledGraph.getVertices()) {
            Location mainLocation = mainVertex.getLocation();
            for (Vertex subVertex : rescaledGraph.getVertices()) {
                if (mainVertex == subVertex) {
                    continue;
                }

                Location subLocation = subVertex.getLocation();
                if (DistanceAlgorithm.distance(mainLocation, subLocation) > reachableDistanceMin
                        && DistanceAlgorithm.distance(mainLocation, subLocation) < reachableDistanceMax) {
                    rescaledGraph.addEdge(mainVertex, subVertex);
                }
            }
        }
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

    private void minimizeGraph() {
        boolean cnt = true;
        while (cnt) {
            ArrayList<Edge> edges = new ArrayList<>(rescaledGraph.getEdges());
            cnt = false;
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                for (int j = i; j < edges.size(); j++) {
                    Edge subEdge = edges.get(j);
                    if (edge == subEdge) {
                        continue;
                    }

                    if (edge.getDestination().getLocation().equals(subEdge.getSource().getLocation())) {
                        rescaledGraph.removeEdge(edge);
                        rescaledGraph.removeEdge(subEdge);

                        rescaledGraph.removeVertex(edge.getSource());
                        rescaledGraph.removeVertex(edge.getDestination());
                        rescaledGraph.removeVertex(subEdge.getSource());
                        rescaledGraph.removeVertex(subEdge.getDestination());

                        rescaledGraph.addEdge(edge.getSource().getLocation(), subEdge.getDestination().getLocation());
                        cnt = true;
                    }
                }
            }
        }
    }

    private void rescaleGraph() {
        for (Edge edge : graph.getEdges()) {

            if (edge.getWeight() > cellSize) {
                int n = (int) (edge.getWeight() / cellSize);
                Queue<Vertex> vertices = vertices(edge, n);

                Vertex poll = vertices.poll();
                if (vertices.isEmpty()) {
                    continue;
                }
                Location previousLocation = poll.getLocation();
                rescaledGraph.addEdge(edge.getSource().getLocation(), previousLocation);
                while (vertices.size() != 1) {
                    Location currentLocation = vertices.poll().getLocation();
                    rescaledGraph.addEdge(previousLocation, currentLocation);
                    previousLocation = currentLocation;
                }
                rescaledGraph.addEdge(vertices.poll().getLocation(), edge.getDestination().getLocation());
            }
        }
    }

    private Queue<Vertex> vertices(Edge edge, int n) {
        Location source = edge.getSource().getLocation();
        Location destination = edge.getDestination().getLocation();

        Queue<Vertex> vertices = new LinkedList<>();

        for (int i = 1; i < n; ++i) {
            Double lat = calc(source.getLatitude(), destination.getLatitude(), edge.getWeight(), i);
            Double lon = calc(source.getLongitude(), destination.getLongitude(), edge.getWeight(), i);
            vertices.add(new Vertex(new Location(lon, lat)));
        }

        return vertices;
    }

    private Double calc(Double x1, Double x2, Double m, int n) {
        BigDecimal b1 = new BigDecimal(x1);
        BigDecimal b2 = new BigDecimal(x2);
        BigDecimal dm = new BigDecimal(m);

        BigDecimal subx2x1 = b2.subtract(b1);
        BigDecimal subdiv = subx2x1.divide(dm, 32, RoundingMode.HALF_UP);

        BigDecimal v = new BigDecimal(cellSize * 2.0).multiply(subdiv).add(b1);
        return v.doubleValue();
    }
}