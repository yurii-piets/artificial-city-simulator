package com.acs.simulator.impl;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import com.acs.pool.def.AgentPool;
import com.acs.service.ParserService;
import com.acs.simulator.def.Simulator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class MockSimulator implements Simulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool pool;

    private Double minLatitude;

    private Double maxLatitude;

    private Double minLongitude;

    private Double maxLongitude;

    @Value("${simulation.unit.max}")
    private Integer maxUnits;

    private Supplier<Double> randomLongitudeFromRange = () -> minLongitude + (maxLongitude - minLongitude) * new Random().nextDouble();

    private Supplier<Double> randomLatitudeFromRange = () -> minLatitude + (maxLatitude - minLatitude) * new Random().nextDouble();

    private Supplier<AgentType> randomAgentType = () -> AgentType.values()[ThreadLocalRandom.current().nextInt(0, AgentType.values().length)];

    private Supplier<Integer> randomWay = () ->  ThreadLocalRandom.current().nextInt(-1, 2);

    @Autowired
    public MockSimulator(AgentPool pool, ParserService parserService) {
        this.pool = pool;
        this.minLatitude = parserService.getLocationRange().getMinLat();
        this.maxLatitude = parserService.getLocationRange().getMaxLat();
        this.minLongitude = parserService.getLocationRange().getMinLng();
        this.maxLongitude = parserService.getLocationRange().getMaxLng();
    }

    @PostConstruct
    public void initRandomAgents() {
        pool.removeAll();
        for (int i = 0; i < maxUnits; ++i) {
            Agent agent = Agent.builder()
                    .dLatitude(randomWay.get() * 0.00001)
                    .dLongitude(randomWay.get() * 0.00001)
                    .type(AgentType.CAR)
                    .location(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()))
                    .build();

            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));

            pool.save(agent);
        }
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

    @Override
    public void changeAgentsAmount(Integer count) {
        maxUnits = count;
        initRandomAgents();
    }

    @Override
    public void resetSimulation() {
        pool.removeAll();
        initRandomAgents();
    }

}
