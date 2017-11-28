package com.acs.pool.impl;

import com.acs.models.agent.Agent;
import com.acs.pool.def.AgentPool;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

// TODO: 11/11/2017 make all void methods async
@Component
public class MockAgentPool implements AgentPool {

    @Getter
    private Set<Agent> agents = new ConcurrentSkipListSet<>();

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
