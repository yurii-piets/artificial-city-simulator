package com.acs;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Component
public class MockSimulator implements Simulator {

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

    private Set<Agent> agents = new ConcurrentSkipListSet<>();

    private Supplier<Double> randomLongitudeFromRange = () -> minLongitude + (maxLongitude - minLongitude) * new Random().nextDouble();

    private Supplier<Double> randomLatitudeFromRange = () -> minLatitude + (maxLatitude - minLatitude) * new Random().nextDouble();

    private Supplier<AgentType> randomAgentType = () -> AgentType.values()[ThreadLocalRandom.current().nextInt(0, AgentType.values().length)];

    @PostConstruct
    public void initRandomAgents() {
        for (int i = 0; i < maxUnits; ++i) {
            Agent agent = Agent.builder()
                    .speed(40.0)
                    .type(randomAgentType.get())
                    .location(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()))
                    .build();

            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));
            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));

            agents.add(agent);
        }
    }

    @Override
    public Set<Agent> getAllAgents() {
        return agents;
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
    public void removeAll() {
        agents.clear();
    }
}
