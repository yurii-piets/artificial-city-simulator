package com.acs.pool.def;

import com.acs.models.agent.Agent;

import java.util.Set;

public interface AgentPool {

    void save(Agent agent);
    void update(Agent agent);
    void removeById(Long id);

    Set<Agent> getAgents();
    Agent findAgentById(Long id);
    Long getMinId();
    Long getMaxId();
    void removeAll();
}
