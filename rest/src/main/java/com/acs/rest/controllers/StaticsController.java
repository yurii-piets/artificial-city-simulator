package com.acs.rest.controllers;

import com.acs.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// TODO: 28/11/2017 create not rest controller that returns index.html page
@RestController(value = "/statics")
public class StaticsController {

    private final ParserService parserService;

    @Autowired
    public StaticsController(ParserService parserService) {
        this.parserService = parserService;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity statics(){
        return new ResponseEntity<>(parserService.getStatics(), HttpStatus.OK);
    }
}
