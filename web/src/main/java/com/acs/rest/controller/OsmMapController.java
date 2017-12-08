package com.acs.rest.controller;

import com.acs.models.Location;
import com.acs.models.LocationRange;
import com.acs.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/map")
public class OsmMapController {

    private final ParserService parserService;

    @Autowired
    public OsmMapController(ParserService parserService) {
        this.parserService = parserService;
    }

    @RequestMapping(path = "/center")
    public ResponseEntity<Location> center(){
        LocationRange locationRange = parserService.getLocationRange();
        Location locationCenter = new Location((locationRange.getMinLng() + locationRange.getMaxLng()) / 2,
                (locationRange.getMinLat() + locationRange.getMaxLat()) / 2);

        return new ResponseEntity<>(locationCenter, HttpStatus.OK);
    }
}
