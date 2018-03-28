package com.acs.database.service;

import com.acs.database.repository.neo4j.AgentRepository;
import com.acs.database.repository.neo4j.EdgeRepository;
import com.acs.database.repository.neo4j.GraphRepository;
import com.acs.database.repository.neo4j.RelationRepository;
import com.acs.database.repository.neo4j.StaticPointRepository;
import com.acs.database.repository.neo4j.VertexRepository;
import com.acs.models.agent.Agent;
import com.acs.models.graph.Graph;
import com.acs.models.node.GraphNode;
import com.acs.pool.def.AgentPool;
import com.acs.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraphPersistenceService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentRepository agentRepository;

    private final EdgeRepository edgeRepository;

    private final GraphRepository graphRepository;

    private final RelationRepository relationRepository;

    private final StaticPointRepository staticPointRepository;

    private final VertexRepository vertexRepository;

    private final ParserService parserService;

    private final AgentPool agentPool;

    @Value("${simulation.unit.import}")
    private Boolean importAgentOnStartup;

    @Value("${simulation.unit.export}")
    private Boolean exportAgents;

    @Value("${graph.export}")
    private Boolean exportGraph;

    public void save(Graph graph) {
        throw new NotImplementedException();
    }

    public Graph findGraphByName(String name) {
        throw new NotImplementedException();
    }

    public void saveGraph(Graph graph) {
        if(!exportGraph) {
            return;
        }

        GraphNode graphNode = new GraphNode(graph);
        graphRepository.save(graphNode, 3);
    }

    public Graph restoreGraph(){
         return new Graph(graphRepository.findAll().iterator().next());
    }

    @Scheduled(fixedDelay = 15 * 1000 * 60)
    public void saveAgents() {
        if (!exportAgents) {
            return;
        }

        logger.info("Saving agents to NEO4J database.");
        Collection<Agent> activeAgents = agentPool.getAgents();

        List<Long> deadAgentIds = agentPool.getDeadAgents()
                .stream()
                .map(Agent::getId)
                .collect(Collectors.toList());

        for(Agent agent: activeAgents){
            agentRepository.save(agent, 2);
        }
        deadAgentIds.forEach(agentRepository::deleteById);

        logger.info("Saved agents number: [" + activeAgents.size() + "]");
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
        Iterable<Agent> agents = agentRepository.findAll();

        agentPool.saveAll(agents);
    }
}
