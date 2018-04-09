package com.acs.database.service;

import com.acs.database.repository.neo4j.AgentRepository;
import com.acs.database.repository.neo4j.GraphRepository;
import com.acs.database.repository.neo4j.RelationRepository;
import com.acs.database.repository.neo4j.StaticPointRepository;
import com.acs.database.repository.neo4j.VertexRepository;
import com.acs.models.agent.Agent;
import com.acs.models.graph.Graph;
import com.acs.models.graph.Vertex;
import com.acs.models.node.GraphNode;
import com.acs.models.statics.Relation;
import com.acs.models.statics.StaticPoint;
import com.acs.models.statics.StaticType;
import com.acs.pool.def.AgentPool;
import com.acs.service.def.GraphService;
import com.acs.simulator.def.LightsSimulator;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GraphPersistenceService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentRepository agentRepository;

    private final GraphRepository graphRepository;

    private final RelationRepository relationRepository;

    private final StaticPointRepository staticPointRepository;

    private final VertexRepository vertexRepository;

    private final LightsSimulator lightsSimulator;

    private final AgentPool agentPool;

    private final GraphService graphService;

    @Value("${simulation.unit.import}")
    private Boolean importAgentOnStartup;

    @Value("${simulation.unit.export}")
    private Boolean exportAgents;

    @Value("${graph.import}")
    private Boolean importGraph;

    @Value("${graph.export}")
    private Boolean exportGraph;

    public void saveGraph(Graph graph) {
        if (!exportGraph) {
            return;
        }

        GraphNode graphNode = new GraphNode(graph);
        graphRepository.save(graphNode, 6);
    }

    public Graph restoreGraph() {
        if (!importGraph) {
            return null;
        }
        Iterable<GraphNode> graphNodes = graphRepository.findAll(5);
        if (!graphNodes.iterator().hasNext()) {
            return null;
        }
        Iterable<StaticPoint> lights = staticPointRepository.findAllByType(StaticType.LIGHTS);
        Iterable<StaticPoint> lightWithoutRelation = staticPointRepository.findByRelationIsNull();
        Iterable<Relation> relations = relationRepository.findAll(2);

        lightsSimulator.setLights(Sets.newHashSet(lights));
        lightsSimulator.setWithoutRelations(Sets.newHashSet(lightWithoutRelation));
        lightsSimulator.setRelations(Sets.newHashSet(relations));

        return new Graph(graphNodes.iterator().next());
    }

    @Scheduled(fixedDelay = 1 * 1000 * 60)
    public void saveAgents() {
        if (!exportAgents) {
            return;
        }

        logger.info("Saving agents to NEO4J database.");
//        Collection<Agent> activeAgents = agentPool.getAgents();


        vertexRepository.dropOldRelations();

        List<Long> deadAgentGids = agentPool.getDeadAgents()
                .stream()
                .map(Agent::getGid)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<Vertex> verticesToUpdate = agentPool.getAgents().stream()
                .map(Agent::getVertex)
                .filter(Objects::nonNull)
                .filter(v -> v.getAgent() != null)
                .collect(Collectors.toSet());

        for (Vertex vertex : verticesToUpdate) {
            Optional<Vertex> updatableVertexOptional = vertexRepository.findById(vertex.getGid());
            if (!updatableVertexOptional.isPresent()) {
                continue;
            }
            Vertex updatableVertex = updatableVertexOptional.get();
            updatableVertex.setAgent(vertex.getAgent());
            vertexRepository.save(updatableVertex, 3);
        }

        for (Long deadId : deadAgentGids) {
            agentRepository.deleteById(deadId);
        }

        logger.info("Saved agents number: [" + verticesToUpdate.size() + "]");
        logger.info("Deleted agents number: [" + deadAgentGids.size() + "]");
        logger.info("Database size: [" + agentRepository.count() + "]");
    }

    public void restoreAgents() {
        if (!importAgentOnStartup) {
            return;
        }
        if (agentRepository.count() < 1) {
            return;
        }

        logger.info("Restoring agents from database: [" + agentRepository.count() + "]");
        Iterable<Agent> agents = agentRepository.findAll(5);

        agentPool.saveAll(agents);
    }
}
