package com.acs.rest.controllers;

import com.acs.pool.def.AgentPool;
import com.acs.simulator.def.Simulator;
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

    private final Simulator simulator;

    @Autowired
    public SimulatorManagementController(AgentPool agentAgentPool,
                                         Simulator simulator) {
        this.agentAgentPool = agentAgentPool;
        this.simulator = simulator;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/count")
    public ResponseEntity<Integer> agentsChangeSize(@RequestBody Integer count) {
        simulator.changeAgentsAmount(count);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reset")
    public ResponseEntity reset() {
        simulator.resetSimulation();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAll() {
        agentAgentPool.removeAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
