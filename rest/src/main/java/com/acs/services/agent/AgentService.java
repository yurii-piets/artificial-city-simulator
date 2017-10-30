package com.acs.services.agent;

import com.acs.models.agent.Agent;

import java.util.Set;

public interface AgentService {
    Agent findAgentById(Long agentId);
    void saveAgent(Agent agent);
    void updateAgent(Agent agent);
    void removeAgent(Agent agent);
    Set<Agent> getAllAgents();
}
