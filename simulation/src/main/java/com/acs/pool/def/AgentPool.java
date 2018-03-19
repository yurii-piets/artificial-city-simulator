package com.acs.pool.def;

import com.acs.models.agent.Agent;

import java.util.Collection;

public interface AgentPool {

    void save(Agent agent);
    void saveAll(Collection<Agent> agents);
    void update(Agent agent);
    void changeAgentsAmount(Integer count);
    void removeById(Long id);
    void kill(Agent agent);

    Collection<Agent> getAgents();
    Collection<Agent> getDeadAgents();
    Integer getMaxUnits();
    Agent findAgentById(Long id);

    void killAll();
}
