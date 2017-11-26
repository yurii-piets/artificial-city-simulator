package com.acs;

import com.acs.models.agent.Agent;

import java.util.Set;

public interface Simulator {

    void simulate() throws InterruptedException;

    Set<Agent> getAgents();

    Agent findAgentById(Long id);

    void save(Agent agent);

    void removeById(Long id);
    void removeAll();

    void update(Agent agent);

    void reset();

    void changeSize(Integer count);

    Long getMinId();

    Long getMaxId();
}
