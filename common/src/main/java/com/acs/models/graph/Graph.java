package com.acs.models.graph;

import com.acs.algorithm.DistanceAlgorithm;
import com.acs.models.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NodeEntity
@NoArgsConstructor
public class Graph {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship
    private Set<Edge> edges = new HashSet<>();

    @Relationship
    private Map<Long, Vertex> vertices = new HashMap<>();

    @Relationship
    private Set<Vertex> startVertices = new HashSet<>();

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

        return edges.add(new Edge(sourceVertex, destinationVertex));
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
