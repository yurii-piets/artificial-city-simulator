package com.acs.rest.controller;

import com.acs.models.statics.Road;
import com.acs.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class StaticsController {

    private final ParserService parserService;

    @Autowired
    public StaticsController(ParserService parserService) {
        this.parserService = parserService;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/statics")
    public ResponseEntity statics(){
        return new ResponseEntity<>(parserService.getStatics(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ways/ids")
    public ResponseEntity waysIds(){
        Set<Long> ids = parserService.getRoads().stream()
                .map(Road::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ways/{wayId}")
    public ResponseEntity ways(@PathVariable Long wayId){
        Road road = parserService.getRoads().stream()
                .filter(r -> r.getId().equals(wayId))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(road, HttpStatus.OK);
    }
}
