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

    public boolean addEdge(Vertex source, Vertex destiantion){
        return edges.add(new Edge(source, destiantion));
    }

    public boolean addEdge(Location source, Location destination) {
        // TODO: 05/12/2017 refactor if vertex with current location already exist
        Vertex sourceVertex = new Vertex(source);
        Vertex destinationVertex = new Vertex(destination);

        vertices.add(sourceVertex);
        vertices.add(destinationVertex);

        return addEdge(sourceVertex, destinationVertex);
    }

    public boolean removeEdge(Edge removeEdge) {
        for (Edge edge : edges) {
            if (edge.getId().equals(removeEdge.getId())) {
                return edges.remove(edge);
            }
        }

        return false;
    }

    public boolean removeVertex(Vertex removeVertex){
        for(Vertex vertex: vertices){
            if(removeVertex.getId().equals(vertex.getId())){
                return vertices.remove(vertex);
            }
        }

        return false;
    }
}
