package com.acs.services.agent;

import com.acs.Simulator;
import com.acs.models.agent.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AgentService {

    @Autowired
    private Simulator simulator;

    public Agent findAgentById(Long agentId) {
        if (agentId == null) {
            return null;
        }

        // TODO: 30/10/2017 YP implement method
        return null;
    }

    // TODO: 30/10/2017 make this method async
    public void saveAgent(Agent agent) {
        // TODO: 30/10/2017 YP implement method
    }

    // TODO: 30/10/2017 make this method async
    public void updateAgent(Agent agent) {
        // TODO: 30/10/2017 yp - implemant method
    }

    // TODO: 30/10/2017 make this method async
    public void removeAgent(Agent agent) {
        // TODO: 30/10/2017 YP implement method
    }

    public void removeById(Long id) {
        simulator.removeById(id);
    }

    public void removeAll() {
        simulator.removeAll();
    }

    public Set<Agent> getAllAgents() {
        return simulator.getAllAgents();
    }
}
