package com.acs.service;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeService {

    private final Date startDate = new Date();

    public Double countTimeDifference() {
        Date actualDate = new Date();

        long diffMillis = actualDate.getTime() - startDate.getTime();
        double diffBasic = (double) diffMillis / (60 * 60 * 1000);

        return (diffBasic / 24) + (diffBasic % 24);
    }
}
