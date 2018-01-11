package com.acs.rest.controller;

import com.acs.simulator.def.AgentSimulator;
import com.acs.simulator.def.LightsSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/simulator")
public class SimulatorManagementController {

    private final AgentSimulator agentSimulator;

    private final LightsSimulator lightsSimulator;

    @Autowired
    public SimulatorManagementController(AgentSimulator agentSimulator,
                                         LightsSimulator lightsSimulator) {
        this.agentSimulator = agentSimulator;
        this.lightsSimulator = lightsSimulator;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reset")
    public ResponseEntity<?> reset() {
        agentSimulator.resetSimulation();
        lightsSimulator.resetSimulation();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/agents/reset")
    public ResponseEntity<?> resetAgentsSimulation() {
        agentSimulator.resetSimulation();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/lights/reset")
    public ResponseEntity<?> resetLightsSimulations() {
        lightsSimulator.resetSimulation();

        return new ResponseEntity(HttpStatus.OK);
    }
}
