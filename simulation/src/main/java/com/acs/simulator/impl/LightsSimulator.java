package com.acs.simulator.impl;

import com.acs.models.statics.Relation;
import com.acs.models.statics.StaticPoint;
import com.acs.models.statics.StaticType;
import com.acs.service.ParserService;
import com.acs.simulator.def.Simulator;
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
public class LightsSimulator implements Simulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final Set<StaticPoint> lights;

    private final Set<Relation> relations = new ConcurrentSkipListSet<>();

    private final Set<StaticPoint> withoutRelations = new ConcurrentSkipListSet<>();

    @Autowired
    public LightsSimulator(ParserService parserService) {
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
    }

    private void initLightsWithoutRelations() {
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
                TimeUnit.SECONDS.sleep(5);
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
