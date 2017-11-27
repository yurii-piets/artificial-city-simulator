package com.acs.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ParserService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @PostConstruct
    public void postConstruct() {
    }
}
