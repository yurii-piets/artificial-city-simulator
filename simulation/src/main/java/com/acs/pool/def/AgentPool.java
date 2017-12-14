package com.acs.pool.def;

import com.acs.models.agent.Agent;

import java.util.Set;

public interface AgentPool {

    void save(Agent agent);
    void update(Agent agent);
    void changeAgentsAmount(Integer count);
    void removeById(Long id);
    void kill(Agent agent);

    Set<Agent> getAgents();
    Set<Agent> getDeadAgents();
    Integer getMaxUnits();
    Agent findAgentById(Long id);
    Long getMinId();
    Long getMaxId();
    void removeAll();

    void killAll();
}
