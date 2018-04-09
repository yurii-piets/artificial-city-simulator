package com.acs.database.service;

import com.acs.database.repository.neo4j.AgentRepository;
import com.acs.database.repository.neo4j.GraphRepository;
import com.acs.database.repository.neo4j.RelationRepository;
import com.acs.database.repository.neo4j.StaticPointRepository;
import com.acs.database.repository.neo4j.VertexRepository;
import com.acs.models.agent.Agent;
import com.acs.models.graph.Graph;
import com.acs.models.node.AgentNode;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

        List<AgentNode> activeAgentNodes = agentPool.getAgents()
                .stream()
                .map(AgentNode::new)
                .collect(Collectors.toList());

        List<Long> deadAgentIds = agentPool.getDeadAgents()
                .stream()
                .map(Agent::getId)
                .collect(Collectors.toList());

        Iterable<AgentNode> allAgents = agentRepository.findAll();
        for (AgentNode activeAgent : activeAgentNodes) {
            for (AgentNode agentNode : allAgents) {
                if (agentNode.equals(activeAgent)) {
                    activeAgent.setId(agentNode.getId());
                }
            }
        }

        agentRepository.saveAll(activeAgentNodes);
        for (Long deadAgentId : deadAgentIds) {
            agentRepository.deleteAgentNodeByAgentId(deadAgentId);
        }

        logger.info("Saved agents number: [" + activeAgentNodes.size() + "]");
        logger.info("Deleted agents number: [" + deadAgentIds.size() + "]");
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
        Graph graph = graphService.getGraph();

        Iterable<AgentNode> agentNodes = agentRepository.findAll(2);

        List<Agent> agents = StreamSupport.stream(agentNodes.spliterator(), false)
                .map(an -> Agent.builder()
                        .location(an.getLocation())
                        .type(an.getType())
                        .location(an.getLocation())
                        .id(an.getAgentId())
                        .vertex(graph.getVertexById(an.getVertexId()) == null
                                ? graph.getClosestVertexForLocation(an.getLocation())
                                : graph.getVertexById(an.getVertexId())
                        )
                        .build())
                .collect(Collectors.toList());

        agentPool.saveAll(agents);
    }
}
