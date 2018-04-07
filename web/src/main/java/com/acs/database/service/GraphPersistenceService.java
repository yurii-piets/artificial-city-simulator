package com.acs.database.service;

import com.acs.database.repository.neo4j.AgentRepository;
import com.acs.database.repository.neo4j.GraphRepository;
import com.acs.models.agent.Agent;
import com.acs.models.graph.Graph;
import com.acs.models.node.GraphNode;
import com.acs.pool.def.AgentPool;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraphPersistenceService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentRepository agentRepository;

    private final GraphRepository graphRepository;

    private final AgentPool agentPool;

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
        return new Graph(graphNodes.iterator().next());
    }

    @Scheduled(fixedDelay = 1 * 1000 * 60)
    public void saveAgents() {
        if (!exportAgents) {
            return;
        }

        logger.info("Saving agents to NEO4J database.");
        Collection<Agent> activeAgents = agentPool.getAgents();

        List<Long> deadAgentGids = agentPool.getDeadAgents()
                .stream()
                .map(Agent::getGid)
                .collect(Collectors.toList());

        for (Agent agent : activeAgents) {
            agentRepository.save(agent, 2);
        }

        for(Long deadId : deadAgentGids){
            agentRepository.deleteById(deadId);
        }

        logger.info("Saved agents number: [" + activeAgents.size() + "]");
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
        Iterable<Agent> agents = agentRepository.findAll();

        agentPool.saveAll(agents);
    }
}
