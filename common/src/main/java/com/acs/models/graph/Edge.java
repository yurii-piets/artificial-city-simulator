package com.acs.models.graph;

import com.acs.algorithm.DistanceAlgorithm;
import lombok.Getter;

@Getter
public class Edge {

    private final Long id = id();

    private final Vertex source;

    private final Vertex destination;

    private Double weight;

    Edge(Vertex source, Vertex destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Neither of source and destination cannot be null.");
        }

        this.source = source;
        this.destination = destination;

        initDistance();
    }

    private void initDistance() {
        this.weight = DistanceAlgorithm.distance(source.getLocation(), destination.getLocation());
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
