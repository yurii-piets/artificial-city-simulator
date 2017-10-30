package com.acs.rest.controllers;

import com.acs.models.agent.Agent;
import com.acs.services.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity<Set<Agent>> agents() {
        Set<Agent> agents = agentService.getAllAgents();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}")
    public ResponseEntity agent(@PathVariable Long agentId) {
        Agent agent = agentService.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(agent, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{agentId}")
    public ResponseEntity updateAgent(@PathVariable Long agentId) {
        Agent agent = agentService.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // TODO: 30/10/2017 YP - create method
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }
}
