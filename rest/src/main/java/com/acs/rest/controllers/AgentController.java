package com.acs.rest.controllers;

import com.acs.Simulator;
import com.acs.models.agent.Agent;
import com.acs.service.JsonPatchService;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/agent")
public class AgentController {

    private final Simulator agentSimulator;

    private final JsonPatchService patchService;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Value("${basic.url}")
    private String basicUrl;

    @Autowired
    public AgentController(Simulator agentSimulator, JsonPatchService patchService) {
        this.agentSimulator = agentSimulator;
        this.patchService = patchService;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity<Set<URI>> agents() {
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
    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}")
    public ResponseEntity agent(@PathVariable Long agentId) {
        Agent agent = agentSimulator.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (agent.getDestinations() != null && !agent.getDestinations().isEmpty()) {
            agent.setLocation(agent.getDestinations().poll());
        }

        return new ResponseEntity<>(agent, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}/info")
    public ResponseEntity agentInfo(@PathVariable Long agentId) {
        Agent agent = agentSimulator.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(agent, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/count")
    public ResponseEntity<Integer> agentsSize() {
        Integer agents = agentSimulator.getAllAgents().size();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity agent(@RequestBody Agent agent) {
        agentSimulator.save(agent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{agentId}")
    public ResponseEntity updateAgent(@PathVariable Long agentId, @RequestBody String updateBody) {
        Agent agent = agentSimulator.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Agent mergedUser = (Agent) patchService.patch(updateBody, agent).get();
            agentSimulator.update(mergedUser);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (JsonPatchException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/all/reset")
    public ResponseEntity reset() {
        agentSimulator.reset();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/all")
    public ResponseEntity deleteAll() {
        agentSimulator.removeAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{agentId}")
    public ResponseEntity deleteAll(@PathVariable Long agentId) {
        agentSimulator.removeById(agentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
