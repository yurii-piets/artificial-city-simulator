package com.acs.service;

import com.acs.database.service.AgentService;
import com.acs.simulator.def.AgentSimulator;
import com.acs.simulator.def.LightsSimulator;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class ApplicationStartup {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentSimulator agentSimulator;

    private final LightsSimulator lightsSimulator;

    private final AgentService agentService;

    @PostConstruct
    public void postConstruct() {
        agentService.restoreAgents();

        logger.info("Starting agent simulation.");
        agentSimulator.simulate();

        logger.info("Starting lights simulation.");
        lightsSimulator.simulate();
    }
}