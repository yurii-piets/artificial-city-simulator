package com.acs.simulator.impl;

import com.acs.models.agent.Agent;
import com.acs.pool.def.AgentPool;
import com.acs.simulator.def.Simulator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class MockSimulator implements Simulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool pool;

    @Autowired
    public MockSimulator(AgentPool pool) {
        this.pool = pool;
    }

    @Async
    @Override
    public void simulate() {
        while (true){
            pool.getAgents().forEach(Agent::move);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error("Unexpected: ", e);
            }
        }
    }
}
