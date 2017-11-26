package com.acs.rest.controllers;

import com.acs.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/simulator")
public class SimulatorManagementController {

    private final Simulator agentSimulator;

    @Autowired
    public SimulatorManagementController(Simulator agentSimulator) {
        this.agentSimulator = agentSimulator;
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
