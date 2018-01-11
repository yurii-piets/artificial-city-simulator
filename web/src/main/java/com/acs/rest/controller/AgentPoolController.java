package com.acs.rest.controller;

import com.acs.models.agent.Agent;
import com.acs.pool.def.AgentPool;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/agents")
public class AgentPoolController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool agentPool;

    @Autowired
    public AgentPoolController(AgentPool agentPool) {
        this.agentPool = agentPool;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/ids")
    public ResponseEntity<Set<Long>> agentsIds() {
        Set<Long> agentIds = agentPool.getAgents().stream()
                .map(Agent::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agentIds, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/objects")
    public ResponseEntity<Set<Agent>> agentsObjects() {
        Set<Agent> agents = agentPool.getAgents();

        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/objects")
    public ResponseEntity<Set<Agent>> agentsObjectsPost(@RequestBody Set<Long> ids) {
        Set<Agent> agents = agentPool.getAgents().stream()
                .filter(a -> ids.contains(a.getId()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dead/ids")
    public ResponseEntity<Set<Long>> deadAgents() {
        Set<Long> deadAgentsIds = agentPool.getDeadAgents().stream()
                .map(Agent::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(deadAgentsIds, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/count")
    public ResponseEntity<Integer> agentsChangeSize(@RequestBody Integer count) {
        agentPool.changeAgentsAmount(count);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAll() {
        agentPool.killAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
