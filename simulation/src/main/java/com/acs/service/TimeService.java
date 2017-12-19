package com.acs.service;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeService {

    private final Date startTime = new Date();

    public Double countTimeDifference() {
        Date actualTime = new Date();
        long diff = actualTime.getTime() - startTime.getTime();

        double diffHours = (double) diff / (60 * 60 * 1000) % 24;

        return diffHours;
    }
}
