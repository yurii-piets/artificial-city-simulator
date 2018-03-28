package com.acs.service;

import com.acs.database.service.GraphPersistenceService;
import com.acs.models.graph.Graph;
import com.acs.service.def.GraphService;
import com.acs.simulator.def.AgentSimulator;
import com.acs.simulator.def.LightsSimulator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
public class ApplicationStartup {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentSimulator agentSimulator;

    private final LightsSimulator lightsSimulator;

    private final GraphPersistenceService graphPersistenceService;

    private final GraphService graphService;

    @Value("${graph.import}")
    private Boolean importGraph;

    @PostConstruct
    public void postConstruct() {
        if (importGraph) {
            graphService.processGraph();
            graphPersistenceService.saveGraph(graphService.getGraph());
        } else {
            Graph graph = graphPersistenceService.restoreGraph();
            if(graph != null) {
                graphService.setGraph(graph);
            } else {
                graphService.processGraph();
                graphPersistenceService.saveGraph(graphService.getGraph());
            }
        }

        graphPersistenceService.restoreAgents();

        logger.info("Starting agent simulation.");
        agentSimulator.simulate();

        logger.info("Starting lights simulation.");
        lightsSimulator.simulate();
    }

    @PreDestroy
    public void preDestroy() {
        logger.info("called");
        graphPersistenceService.saveAgents();
    }
}
