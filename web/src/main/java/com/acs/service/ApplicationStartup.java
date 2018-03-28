package com.acs.service;

import com.acs.database.service.GraphPersistenceService;
import com.acs.simulator.def.AgentSimulator;
import com.acs.simulator.def.LightsSimulator;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@AllArgsConstructor
public class ApplicationStartup {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentSimulator agentSimulator;

    private final LightsSimulator lightsSimulator;

    private final GraphPersistenceService graphPersistenceService;

    private final GraphService graphService;

    @PostConstruct
    public void postConstruct() {
        graphService.processGraph();

        graphPersistenceService.restoreAgents();

        logger.info("Starting agent simulation.");
        agentSimulator.simulate();

        logger.info("Starting lights simulation.");
        lightsSimulator.simulate();
    }

    @PreDestroy
    public void preDestroy(){
        logger.info("called");
        graphPersistenceService.saveAgents();
    }
}
