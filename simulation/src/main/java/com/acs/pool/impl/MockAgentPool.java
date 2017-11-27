package com.acs.pool.impl;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import com.acs.pool.def.AgentPool;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

// TODO: 11/11/2017 make all void methods async
@Component
public class MockAgentPool implements AgentPool {

    @Value("${location.latitude.min}")
    private Double minLatitude;

    @Value("${location.latitude.max}")
    private Double maxLatitude;

    @Value("${location.longitude.min}")
    private Double minLongitude;

    @Value("${location.longitude.max}")
    private Double maxLongitude;

    @Value("${simulation.unit.max}")
    private Integer maxUnits;

    @Getter
    private Set<Agent> agents = new ConcurrentSkipListSet<>();

    private Supplier<Double> randomLongitudeFromRange = () -> minLongitude + (maxLongitude - minLongitude) * new Random().nextDouble();

    private Supplier<Double> randomLatitudeFromRange = () -> minLatitude + (maxLatitude - minLatitude) * new Random().nextDouble();

    private Supplier<AgentType> randomAgentType = () -> AgentType.values()[ThreadLocalRandom.current().nextInt(0, AgentType.values().length)];

    private Supplier<Integer> randomWay = () ->  ThreadLocalRandom.current().nextInt(-1, 2);

    @PostConstruct
    public void initRandomAgents() {
        agents = new ConcurrentSkipListSet<>();

        for (int i = 0; i < maxUnits; ++i) {
            Agent agent = Agent.builder()
                    .dLatitude(randomWay.get() * 0.00001)
                    .dLongitude(randomWay.get() * 0.00001)
                    .type(randomAgentType.get())
                    .location(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()))
                    .build();

            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));

            agents.add(agent);
        }
    }

    @Override
    public Agent findAgentById(Long id) {
        Agent agent = agents.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);

        return agent;
    }

    @Override
    public void save(Agent agent) {
        agents.add(agent);
    }

    @Override
    public void removeById(Long id) {
        Agent agent = findAgentById(id);

        if (agent == null) {
            return;
        }

        agents.remove(agent);
    }

    @Override
    public void update(Agent agent) {
        removeById(agent.getId());
        agents.add(agent);
    }

    @Override
    public void reset() {
        agents.clear();
        initRandomAgents();
    }

    @Override
    public void changeSize(Integer count) {
        maxUnits = count;
        initRandomAgents();
    }

    @Override
    public Long getMinId() {
        return agents.stream()
                .map(Agent::getId)
                .min(Long::compare)
                .get();
    }

    @Override
    public Long getMaxId() {
        return agents.stream()
                .map(Agent::getId)
                .max(Long::compare)
                .get();
    }

    @Override
    public void removeAll() {
        agents.clear();
    }
}
