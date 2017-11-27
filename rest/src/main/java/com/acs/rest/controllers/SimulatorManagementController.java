package com.acs.rest.controllers;

import com.acs.pool.def.AgentPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/simulator")
public class SimulatorManagementController {

    private final AgentPool agentAgentPool;

    @Autowired
    public SimulatorManagementController(AgentPool agentAgentPool) {
        this.agentAgentPool = agentAgentPool;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/count")
    public ResponseEntity<Integer> agentsChangeSize(@RequestBody Integer count) {
        agentAgentPool.changeSize(count);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reset")
    public ResponseEntity reset() {
        agentAgentPool.reset();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAll() {
        agentAgentPool.removeAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
