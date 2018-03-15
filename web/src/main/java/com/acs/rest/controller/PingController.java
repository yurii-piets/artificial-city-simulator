package com.acs.rest.controller;

import com.acs.service.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.acs.service.PingService.PING_PROFILE;

@RestController
@RequestMapping(path = "/ping")
@RequiredArgsConstructor
@Profile(PING_PROFILE)
public class PingController {

    private final PingService pingService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity ping() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity pingPut() {
        pingService.ping();
        return new ResponseEntity(HttpStatus.OK);
    }
}
