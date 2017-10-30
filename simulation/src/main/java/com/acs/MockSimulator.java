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

    @PostConstruct
    public void initRandomAgents() {
        for (int i = 0; i < amountOfMockSimulationUnits; ++i) {
            Supplier<Double> randomLongitudeFromRange = () -> minLongitude + (maxLongitude - minLongitude) * new Random().nextDouble();

            Supplier<Double> randomLatitudeFromRange = () -> minLatitude + (maxLatitude - minLatitude) * new Random().nextDouble();

            Agent agent = Agent.builder()
                    .type(AgentType.HUMAN)
                    .speed(5.0)
                    .location(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()))
                    .build();

            agent.addDestination(new Location(randomLongitudeFromRange.get(), randomLatitudeFromRange.get()));

            agents.add(agent);
        }
    }

    @Override
    public Set<Agent> getAllAgents() {
        return agents;
    }
}
