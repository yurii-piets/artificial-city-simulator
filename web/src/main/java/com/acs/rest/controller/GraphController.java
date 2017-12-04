package com.acs.rest.controller;

import com.acs.models.graph.Edge;
import com.acs.models.graph.Graph;
import com.acs.models.graph.Vertex;
import com.acs.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/graph")
public class GraphController {

    private final GraphService graphService;

    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @CrossOrigin
    @RequestMapping(path = "/edges/ids")
    public ResponseEntity<Set<Long>> edges() {
        Graph graph = graphService.getGraph();
        Set<Long> ids = graph.getEdges().stream()
                .map(Edge::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/edge/{id}")
    public ResponseEntity<Edge> edge(@PathVariable Long id) {
        Graph graph = graphService.getGraph();
        Edge edge = graph.getEdges().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(edge, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/vertexes/ids")
    public ResponseEntity<Set<Long>> vertexes() {
        Graph graph = graphService.getGraph();
        Set<Long> ids = graph.getVertices().stream()
                .map(Vertex::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/vertex/{id}")
    public ResponseEntity<Vertex> vertex(@PathVariable Long id) {
        Graph graph = graphService.getGraph();
        Vertex vertex = graph.getVertices().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(vertex, HttpStatus.OK);
    }
}
