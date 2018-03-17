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
public class AgentPersistenceService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentRepository agentRepository;

    private final AgentPool agentPool;

    private final Graph graph;

    @Autowired
    public AgentPersistenceService(AgentRepository agentRepository,
                                   AgentPool agentPool,
                                   GraphService graphService) {
        this.agentRepository = agentRepository;
        this.agentPool = agentPool;
        this.graph = graphService.getGraph();
    }

    @Value("${simulation.unit.import}")
    private Boolean importAgentOnStartup;

    @Value("${simulation.unit.export}")
    private Boolean exportAgents;

    @Scheduled(fixedDelay = 15 * 1000 * 60)
    public void saveAgents() {
        if (!exportAgents) {
            return;
        }

        logger.info("Saving agents to database.");
        List<AgentDocument> activeAgentDocuments = agentPool.getAgents()
                .stream()
                .map(AgentDocument::new)
                .collect(Collectors.toList());

        List<Long> deadAgentDocumentIds = agentPool.getDeadAgents()
                .stream()
                .map(Agent::getId)
                .collect(Collectors.toList());

        deadAgentDocumentIds.forEach(agentRepository::deleteById);
        agentRepository.saveAll(activeAgentDocuments);
    }

    public void restoreAgents() {
        if (!importAgentOnStartup) {
            return;
        }
        if (agentRepository.count() < 1) {
            return;
        }

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
