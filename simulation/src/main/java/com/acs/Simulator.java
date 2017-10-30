package com.acs;

import com.acs.models.agent.Agent;

import java.util.Set;

public interface Simulator {

    Set<Agent> getAllAgents();

    void removeById(Long id);
    void removeAll();
}
