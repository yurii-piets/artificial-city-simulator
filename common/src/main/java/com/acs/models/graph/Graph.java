package com.acs.models.graph;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import com.acs.models.node.GraphNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NodeEntity
@NoArgsConstructor
public class Graph {

    private Set<Edge> edges = new HashSet<>();

    private Map<Long, Vertex> vertices = new HashMap<>();

    private Set<Vertex> startVertices = new HashSet<>();

    public Graph(GraphNode graphNode) {
        graphNode.getVertices().stream()
                .map(Vertex::getEdges)
                .flatMap(Collection::stream)
                .map(edges::add);
        this.vertices = graphNode.getVertices().stream()
                .collect(Collectors.toMap(Vertex::getId, v -> v));
        this.startVertices = graphNode.getStartVertices();
    }

    public boolean addEdge(Edge edge) {
        Location source = edge.getSource().getLocation();
        Location destination = edge.getDestination().getLocation();

        return addEdge(source, destination);
    }

    public boolean addEdge(Location sourceLocation, Location destinationLocation) {
        Vertex sourceVertex = findVertexOrCreateNew(sourceLocation);
        Vertex destinationVertex = findVertexOrCreateNew(destinationLocation);

        vertices.put(sourceVertex.getId(), sourceVertex);
        vertices.put(destinationVertex.getId(), destinationVertex);

        sourceVertex.addReachableVertex(destinationVertex);

        Edge edge = new Edge(sourceVertex, destinationVertex);
        sourceVertex.addEdge(edge);
        return edges.add(edge);
    }

    public boolean addStartVertex(Vertex vertex) {
        return startVertices.add(vertex);
    }

    private Vertex findVertexOrCreateNew(Location location) {
        Vertex vertex = findVertexByLocation(location);

        if (vertex == null) {
            vertex = new Vertex();
            vertex.setLocation(location);
        }

        return vertex;
    }

    private Vertex findVertexByLocation(Location source) {
        return vertices.values().stream()
                .filter(v -> v.getLocation().equals(source))
                .findFirst()
                .orElse(null);
    }

    public Vertex getClosestVertexForLocation(Location location) {
        Vertex closestVertex = null;
        Double minDistance = Double.MAX_VALUE;

        for (Vertex vertex : vertices.values()) {
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

    public Vertex getVertexById(Long id) {
        return vertices.get(id);
    }

    public Collection<Vertex> getVertices() {
        return vertices.values();
    }
}
