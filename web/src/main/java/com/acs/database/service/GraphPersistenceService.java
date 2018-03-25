package com.acs.database.service;

import com.acs.database.repository.neo4j.AgentRepository;
import com.acs.database.repository.neo4j.EdgeRepository;
import com.acs.database.repository.neo4j.GraphRepository;
import com.acs.database.repository.neo4j.RelationRepository;
import com.acs.database.repository.neo4j.StaticPointRepository;
import com.acs.database.repository.neo4j.VertexRepository;
import com.acs.models.graph.Graph;
import com.acs.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Repository
@RequiredArgsConstructor
public class GraphPersistenceService {

    private final AgentRepository agentRepository;

    private final EdgeRepository edgeRepository;

    private final GraphRepository graphRepository;

    private final RelationRepository relationRepository;

    private final StaticPointRepository staticPointRepository;

    private final VertexRepository vertexRepository;

    private final ParserService parserService;

    public void save(Graph graph) {
        throw new NotImplementedException();
    }

    public Graph findGraphByName(String name) {
        throw new NotImplementedException();
    }
}
