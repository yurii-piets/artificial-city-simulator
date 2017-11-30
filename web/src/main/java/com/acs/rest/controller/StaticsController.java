package com.acs.rest.controller;

import com.acs.models.statics.Road;
import com.acs.models.statics.RoadType;
import com.acs.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
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
    public ResponseEntity statics() {
        return new ResponseEntity<>(parserService.getStatics(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ways/ids")
    public ResponseEntity waysIds() {
        Set<Long> ids = parserService.getRoads().stream()
                .map(Road::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ways/{wayId}")
    public ResponseEntity ways(@PathVariable Long wayId) {
        Road road = parserService.getRoads().stream()
                .filter(r -> r.getId().equals(wayId))
                .findFirst()
                .orElse(null);

        return new ResponseEntity<>(road, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ways")
    public ResponseEntity ways(@RequestParam(value = "type") List<String> types) {

        Set<RoadType> roadTypes = types.stream()
                .map(String::toUpperCase)
                .map(RoadType::valueOf)
                .collect(Collectors.toSet());

        Set<Road> ways = parserService.getRoads().stream()
                .filter(way -> roadTypes.contains(way.getRoadType()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(ways, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/ways/types")
    public ResponseEntity types() {
        Set<String> types = Arrays.stream(RoadType.values())
                .map(RoadType::toString)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(types, HttpStatus.OK);
    }


}
