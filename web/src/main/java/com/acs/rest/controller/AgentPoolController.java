package com.acs.rest.controller;

import com.acs.models.RangeDTO;
import com.acs.models.agent.Agent;
import com.acs.pool.def.AgentPool;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/agent/all")
public class AgentPoolController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool agentAgentPool;

    @Value("${basic.url}")
    private String basicUrl;

    @Autowired
    public AgentPoolController(AgentPool agentAgentPool) {
        this.agentAgentPool = agentAgentPool;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/ids")
    public ResponseEntity<Set<Long>> agentsIds() {

        Set<Long> agentIds = agentAgentPool.getAgents().stream()
                .map(Agent::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agentIds, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/links")
    public ResponseEntity<Set<URI>> agentsLinks() {
        Set<Agent> agents = agentAgentPool.getAgents();

        Set<URI> agentUrls = agents.stream()
                .map(Agent::getId)
                .map(id -> {
                    try {
                        return new URI(basicUrl + "agent/" + id);
                    } catch (URISyntaxException e) {
                        logger.error("Unexpected url: ", e);
                        return null;
                    }
                })
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agentUrls, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/objects")
    public ResponseEntity<Set<Agent>> agentsObjects() {
        Set<Agent> agents = agentAgentPool.getAgents();

        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public ResponseEntity<Integer> agentsSize() {
        Integer agents = agentAgentPool.getAgents().size();

        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/range")
    public ResponseEntity agentsRange() {
        Long min = agentAgentPool.getMinId();
        Long max = agentAgentPool.getMaxId();

        RangeDTO range = new RangeDTO(min, max);
        return new ResponseEntity<>(range, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dead/ids")
    public ResponseEntity deadAgents(){
        Set<Agent> deadAgents = agentAgentPool.getDeadAgents();

        return new ResponseEntity<>(deadAgents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/count")
    public ResponseEntity<Integer> agentsChangeSize(@RequestBody Integer count) {
        agentAgentPool.changeAgentsAmount(count);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAll() {
        agentAgentPool.killAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
