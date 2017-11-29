package com.acs.rest.controller;

import com.acs.pool.def.AgentPool;
import com.acs.models.agent.Agent;
import com.acs.service.JsonPatchService;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/agent")
public class AgentManagementController {

    private final AgentPool agentPool;

    private final JsonPatchService patchService;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public AgentManagementController(AgentPool agentAgentPool,
                                     JsonPatchService patchService) {
        this.agentPool = agentAgentPool;
        this.patchService = patchService;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}")
    public ResponseEntity agent(@PathVariable Long agentId) {
        Agent agent = agentPool.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(agent, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createAgent(@RequestBody Agent agent) {
        agentPool.save(agent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{agentId}")
    public ResponseEntity updateAgent(@PathVariable Long agentId, @RequestBody String updateBody) {
        Agent agent = agentPool.findAgentById(agentId);

        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Agent mergedUser = (Agent) patchService.patch(updateBody, agent).get();
            agentPool.update(mergedUser);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (JsonPatchException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{agentId}")
    public ResponseEntity deleteAgent(@PathVariable Long agentId) {
        agentPool.removeById(agentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
