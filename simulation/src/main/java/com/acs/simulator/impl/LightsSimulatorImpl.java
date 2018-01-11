package com.acs.simulator.impl;

import com.acs.models.statics.Relation;
import com.acs.models.statics.StaticPoint;
import com.acs.models.statics.StaticType;
import com.acs.service.ParserService;
import com.acs.simulator.def.LightsSimulator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LightsSimulatorImpl implements LightsSimulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final Set<StaticPoint> lights;

    private final Set<Relation> relations = new ConcurrentSkipListSet<>();

    private final Set<StaticPoint> withoutRelations = new ConcurrentSkipListSet<>();

    @Autowired
    public LightsSimulatorImpl(ParserService parserService) {
        this.lights = parserService.getStatics().stream()
                .filter(point -> point.getType() == StaticType.LIGHTS)
                .collect(Collectors.toSet());
    }

    @PostConstruct
    public void postConstruct() {
        initRelations();
        initLightsWithoutRelations();
    }

    private void initRelations() {
        logger.info("Searching for lights relations.");
    }

    private void initLightsWithoutRelations() {
        logger.info("Searching for lights without relation.");
        lights.stream()
                .filter(light -> light.getRelation() == null)
                .forEach(withoutRelations::add);
    }

    @Async
    @Override
    public void simulate() {
        try {
            while (!Thread.interrupted()) {
                oneStep();
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            logger.error("Unexpected: ", e);
        }
    }

    private void oneStep() {
        relations.forEach(Relation::next);
        withoutRelations.forEach(light -> light.setLocked(!light.isLocked()));
    }

    @Override
    public void resetSimulation() {

    }
}
