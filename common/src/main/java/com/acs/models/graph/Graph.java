package com.acs.models.graph;

import com.acs.models.Location;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Graph {

    private Set<Edge> edges = new HashSet<>();

    private Set<Vertex> vertices = new HashSet<>();

    public boolean addVertex(Location location) {
        return vertices.add(new Vertex(location));
    }

    public boolean addEdge(Location source, Location destination) {
        Vertex sourceVertex = new Vertex(source);
        Vertex destinationVertex = new Vertex(destination);

        vertices.add(sourceVertex);
        vertices.add(destinationVertex);

        return edges.add(new Edge(sourceVertex, destinationVertex));
    }
}
