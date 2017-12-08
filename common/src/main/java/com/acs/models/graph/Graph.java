package com.acs.models.graph;

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

    public boolean addEdge(Edge edge) {
        Location source = edge.getSource().getLocation();
        Location destination = edge.getDestination().getLocation();

        return addEdge(source, destination);
    }

    public boolean addEdge(Location sourceLocation, Location destinationLocation) {
        Vertex sourceVertex = findVertexOrCreateNew(sourceLocation);
        Vertex destinationVertex = findVertexOrCreateNew(destinationLocation);

        vertices.add(sourceVertex);
        vertices.add(destinationVertex);

        return edges.add(new Edge(sourceVertex, destinationVertex));
    }

    public boolean removeEdge(Edge removeEdge) {
        for (Edge edge : edges) {
            if (edge.getId().equals(removeEdge.getId())) {
                return edges.remove(edge);
            }
        }

        return false;
    }

    public boolean removeVertex(Vertex removeVertex) {
        for (Vertex vertex : vertices) {
            if (removeVertex.getId().equals(vertex.getId())) {
                return vertices.remove(vertex);
            }
        }

        return false;
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
}
