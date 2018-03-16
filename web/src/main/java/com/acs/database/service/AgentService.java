package com.acs.database.service;

import com.acs.database.document.AgentDocument;
import com.acs.database.repository.AgentRepository;
import com.acs.models.agent.Agent;
import com.acs.models.graph.Graph;
import com.acs.pool.def.AgentPool;
import com.acs.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentRepository agentRepository;

    private final AgentPool agentPool;

    private final Graph graph;

    @Autowired
    public AgentService(AgentRepository agentRepository,
                        AgentPool agentPool,
                        GraphService graphService) {
        this.agentRepository = agentRepository;
        this.agentPool = agentPool;
        this.graph = graphService.getGraph();
    }

    @Value("${simulation.unit.import}")
    private Boolean importAgentOnStartup;

    @Scheduled(fixedDelay = 15 * 1000 * 60)
    public void saveAgents() {
        logger.info("Saving agents to database.");
        List<AgentDocument> activeAgentDocuments = agentPool.getAgents()
                .stream()
                .map(AgentDocument::new)
                .collect(Collectors.toList());

        List<AgentDocument> deadAgentDocuments = agentPool.getDeadAgents()
                .stream()
                .map(AgentDocument::new)
                .collect(Collectors.toList());

        agentRepository.saveAll(activeAgentDocuments);
        agentRepository.deleteAll(deadAgentDocuments);
    }

    public void restoreAgents() {
        if (importAgentOnStartup) {
            if (agentRepository.count() > 0) {
                logger.info("Restoring agents from database.");
                List<Agent> agents = agentRepository.findAll().stream()
                        .map(ad -> Agent.builder()
                                .location(ad.getLocation())
                                .type(ad.getType())
                                .location(ad.getLocation())
                                .id(ad.getId())
                                .vertex(graph.getVertexById(ad.getId()) == null
                                        ? graph.getClosestVertexForLocation(ad.getLocation())
                                        : graph.getVertexById(ad.getId())
                                )
                                .build())
                        .collect(Collectors.toList());

                agentPool.saveAll(agents);
            }
        }
    }
}
