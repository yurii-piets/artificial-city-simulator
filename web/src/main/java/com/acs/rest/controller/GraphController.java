package com.acs.rest.controller;

import com.acs.models.graph.Edge;
import com.acs.models.graph.Vertex;
import com.acs.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/graph")
@RequiredArgsConstructor
public class GraphController {

    private final GraphService graphService;

    @RequestMapping(path = "/edges/ids")
    public ResponseEntity<Set<Long>> edges() {
        Set<Long> ids = graphService.getGraph().getEdges().stream()
                .map(Edge::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @RequestMapping(path = "/edge/{id}")
    public ResponseEntity<Edge> edge(@PathVariable Long id) {
        Edge edge = graphService.getGraph().getEdges().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(edge, HttpStatus.OK);
    }

    @RequestMapping(path = "/vertices/ids")
    public ResponseEntity<Set<Long>> vertices() {
        Set<Long> ids = graphService.getGraph().getVertices().stream()
                .map(Vertex::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @RequestMapping(path = "/vertex/{id}")
    public ResponseEntity<Vertex> vertex(@PathVariable Long id) {
        Vertex vertex = graphService.getGraph().getVertices().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(vertex, HttpStatus.OK);
    }

    @RequestMapping(path = "/startVertices")
    public ResponseEntity<Set<Vertex>> startVertices() {
        Set<Vertex> startVertices = graphService.getGraph().getStartVertices();

        return new ResponseEntity<>(startVertices, HttpStatus.OK);
    }
}
