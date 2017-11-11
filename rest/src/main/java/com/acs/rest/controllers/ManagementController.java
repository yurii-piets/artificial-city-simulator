package com.acs.rest.controllers;

import com.acs.Simulator;
import com.acs.models.RangeDTO;
import com.acs.models.agent.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
public class ManagementController {

    private final Simulator agentSimulator;

    @Value("${basic.url}")
    private String basicUrl;

    @Autowired
    public ManagementController(Simulator agentSimulator) {
        this.agentSimulator = agentSimulator;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ids")
    public ResponseEntity<Set<Long>> agentsIds() {
        Set<Agent> agents = agentSimulator.getAllAgents();

        Set<Long> agentIds = agents.stream().map(Agent::getId).collect(Collectors.toSet());

        return new ResponseEntity<>(agentIds, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/links")
    public ResponseEntity<Set<URI>> agentsLinks() {
        Set<Agent> agents = agentSimulator.getAllAgents();

        Set<URI> agentUrls = agents.stream()
                .map(Agent::getId)
                .map(id -> {
                    try {
                        return new URI(basicUrl + "agent/" + id);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agentUrls, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/objects")
    public ResponseEntity<Set<Agent>> agentsObjects() {
        Set<Agent> agents = agentSimulator.getAllAgents();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public ResponseEntity<Integer> agentsSize() {
        Integer agents = agentSimulator.getAllAgents().size();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/range")
    public ResponseEntity agentsRange() {
        Long min = agentSimulator.getMinId();
        Long max = agentSimulator.getMaxId();

        RangeDTO range = new RangeDTO(min, max);
        return new ResponseEntity<>(range, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/count")
    public ResponseEntity<Integer> agentsChangeSize(@RequestBody Integer count) {
        agentSimulator.changeSize(count);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reset")
    public ResponseEntity reset() {
        agentSimulator.reset();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAll() {
        agentSimulator.removeAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
