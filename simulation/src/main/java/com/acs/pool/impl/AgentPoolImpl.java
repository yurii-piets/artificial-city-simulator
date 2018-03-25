package com.acs.pool.impl;

import com.acs.models.agent.Agent;
import com.acs.pool.def.AgentPool;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class AgentPoolImpl implements AgentPool {

    @Getter
    @Value("${simulation.unit.max}")
    private Integer maxUnits;

    @Getter(AccessLevel.NONE)
    private Map<Long, Agent> agents = new ConcurrentHashMap<>();

    @Getter
    private Set<Agent> deadAgents = new ConcurrentSkipListSet<>();

    @Override
    public Agent findAgentById(Long id) {
        return agents.get(id);
    }

    @Override
    public Collection<Agent> getAgents() {
        return agents.values();
    }

    @Override
    public void save(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    @Override
    public void saveAll(Iterable<Agent> agents) {
        agents.forEach(a -> this.agents.put(a.getId(), a));
    }

    @Override
    public void kill(Agent agent) {
        agents.remove(agent.getId());
        deadAgents.add(agent);
    }

    @Override
    public void removeById(Long id) {
        agents.remove(id);
    }

    @Override
    public void update(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    @Override
    public void changeAgentsAmount(Integer count) {
        maxUnits = count;
    }

    @Override
    public void killAll() {
        agents.values().forEach(this::kill);
    }
}
