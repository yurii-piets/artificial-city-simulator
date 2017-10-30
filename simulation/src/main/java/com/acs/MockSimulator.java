package com.acs;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Component
public class MockSimulator implements Simulator {

    @Value("${location.min.latitude}")
    private Double minLatitude;

    @Value("${location.max.latitude}")
    private Double maxLatitude;

    @Value("${location.min.longitude}")
    private Double minLongitude;

    @Value("${location.max.longitude}")
    private Double maxLongitude;

    private final static Integer amountOfMockSimulationUnits = 2;

    private Set<Agent> agents = new ConcurrentSkipListSet<>();

    Supplier<Double> randomLongitudeFromRange = () -> minLongitude + (maxLongitude - minLongitude) * new Random().nextDouble();

    Supplier<Double> randomLatitudeFromRange = () -> minLatitude + (maxLatitude - minLatitude) * new Random().nextDouble();

    Supplier<AgentType> randomAgentType = () -> AgentType.values()[ThreadLocalRandom.current().nextInt(0, AgentType.values().length)];

    public void initRandomAgents() {
        if (agents.size() > 100) {
            return;
        }
        for (int i = 0; i < amountOfMockSimulationUnits; ++i) {
            Agent agent = Agent.builder()
                    .speed(40.0)
                    .type(randomAgentType.get())
                    .location(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()))
                    .build();

            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));

            agents.add(agent);
        }
    }

    @Override
    public Set<Agent> getAllAgents() {
        initRandomAgents();
        return agents;
    }


    @Override
    public void removeById(Long id) {
        Agent agent = agents.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);

        agents.remove(agent);
    }

    @Override
    public void removeAll() {
        agents.clear();
    }
}
