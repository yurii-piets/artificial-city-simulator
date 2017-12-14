package com.acs.simulator.impl;

import com.acs.models.statics.StaticPoint;
import com.acs.models.statics.StaticType;
import com.acs.service.ParserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class StaticSimulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final ParserService parserService;

    @Autowired
    public StaticSimulator(ParserService parserService) {
        this.parserService = parserService;
    }

    @Async
    public void startLightsDaemon(){
        // TODO: 10/12/2017 think about better way of managing lights
        Set<StaticPoint> lights = parserService.getStatics().stream()
                .filter(s -> s.getType() == StaticType.LIGHTS)
                .collect(Collectors.toSet());

        Random random = new Random();
        lights.forEach(l -> l.setLocked(random.nextBoolean()));

        try {
            while (!Thread.interrupted()) {
                lights.forEach(l -> l.setLocked(!l.isLocked()));
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e){
            logger.error("Unexpected: ", e);
        }
    }
}
