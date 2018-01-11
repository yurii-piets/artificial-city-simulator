package com.acs.simulator.impl;

import com.acs.service.ParserService;
import com.acs.simulator.def.Simulator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LightsSimulator implements Simulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final ParserService parserService;

    @Autowired
    public LightsSimulator(ParserService parserService) {
        this.parserService = parserService;
    }

    @PostConstruct
    public void postConstruct() {

    }

    @Async
    @Override
    public void simulate() {

    }

    @Override
    public void resetSimulation() {

    }
}
