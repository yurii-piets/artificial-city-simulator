package com.acs.models.graph;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class Graph {

    private Set<Edge> edges = new HashSet<>();

    private Set<Vertex> vertices = new HashSet<>();

    private Set<Vertex> startVertices = new HashSet<>();

    public boolean addEdge(Edge edge) {
        Location source = edge.getSource().getLocation();
        Location destination = edge.getDestination().getLocation();

        return addEdge(source, destination);
    }

    public boolean addEdge(Location sourceLocation, Location destinationLocation) {
        Vertex sourceVertex = findVertexOrCreateNew(sourceLocation);
        Vertex destinationVertex = findVertexOrCreateNew(destinationLocation);
        sourceVertex.addReachableVertex(destinationVertex);

        vertices.add(sourceVertex);
        vertices.add(destinationVertex);

        return edges.add(new Edge(sourceVertex, destinationVertex));
    }

    public boolean addStartVertex(Vertex vertex) {
        return startVertices.add(vertex);
    }

    private Vertex findVertexOrCreateNew(Location location) {
        Vertex vertex = findVertexByLocation(location);

        if (vertex == null) {
            vertex = new Vertex(location);
        }

        return vertex;
    }

    private Vertex findVertexByLocation(Location source) {
        return vertices.stream()
                .filter(v -> v.getLocation().equals(source))
                .findFirst()
                .orElse(null);
    }

    public Vertex getClosestVertexForLocation(Location location) {
        Vertex closestVertex = null;
        Double minDistance = Double.MAX_VALUE;

        for (Vertex vertex : vertices) {
            if (vertex.getLocation().equals(location)) {
                return vertex;
            }

            Double currentDistance = DistanceAlgorithm.distance(vertex.getLocation(), location);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestVertex = vertex;
            }
        }

        return closestVertex;
    }
}
