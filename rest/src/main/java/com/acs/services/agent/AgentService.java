package com.acs.services.agent;

import com.acs.models.agent.Agent;

import java.util.List;

public interface AgentService {
    Agent findAgentById(Long agentId);
    void saveAgent(Agent agent);
    void updateAgent(Agent agent);
    void removeAgent(Agent agent);
    List<Agent> getAllAgents();
}
