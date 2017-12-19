package com.acs.rest.controller;

import com.acs.simulator.def.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/simulator")
public class SimulatorManagementController {

    private final Simulator simulator;

    @Autowired
    public SimulatorManagementController(Simulator simulator) {
        this.simulator = simulator;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reset")
    public ResponseEntity reset() {
        simulator.resetSimulation();

        return new ResponseEntity(HttpStatus.OK);
    }
}
