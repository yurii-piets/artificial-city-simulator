package com.acs.rest.controller;

import com.acs.models.Location;
import com.acs.models.StatisticDto;
import com.acs.models.graph.Vertex;
import com.acs.service.def.GraphService;
import com.acs.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final static Double MAGIC_NUMBER = 4.0;

    private final GraphService graphService;

    private final TimeService timeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity statistic(@RequestParam Double longitude, @RequestParam Double latitude) {
        Vertex vertex = graphService.getGraph().getClosestVertexForLocation(new Location(longitude, latitude));

        if (vertex == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Integer agentsCount = vertex.getAgentsCount();
        Double hourDifference = timeService.countTimeDifference();
        Double rescaledHourDifference = hourDifference * MAGIC_NUMBER;

        int agentCount = rescaledHourDifference < 1 ? (int) (agentsCount / rescaledHourDifference) : agentsCount;

        StatisticDto statisticDto = new StatisticDto(vertex.getLocation(), agentCount);
        return new ResponseEntity<>(statisticDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/uptime")
    public ResponseEntity<Double> uptime() {
        Double uptime = timeService.countTimeDifference();
        return new ResponseEntity<>(uptime, HttpStatus.OK);
    }
}
