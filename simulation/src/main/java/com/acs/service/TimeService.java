package com.acs.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TimeService {

    private final LocalDateTime startDateTime = LocalDateTime.now();

    public Double countTimeDifference() {
        LocalDateTime actualDateTime = LocalDateTime.now();

        LocalDateTime tempDateTime = LocalDateTime.from(startDateTime);
        double hours = tempDateTime.until(actualDateTime, ChronoUnit.HOURS);
        double minutes = tempDateTime.until(actualDateTime, ChronoUnit.MINUTES);

        return hours + (minutes % 60 / 60);
    }
}
