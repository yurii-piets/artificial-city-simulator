package com.acs.simulator.impl;

import com.acs.pool.def.AgentPool;
import com.acs.simulator.def.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class RSimulator implements Simulator {

    private final AgentPool pool;

    @Autowired
    public RSimulator(AgentPool pool) {
        this.pool = pool;
    }

    @PostConstruct
    public void postConstruct() {

    }

    @Override
    public void simulate() {

    }

    @Override
    public void resetSimulation() {
        pool.removeAll();
    }
}
