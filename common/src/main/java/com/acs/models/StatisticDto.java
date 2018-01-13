package com.acs.models;

import lombok.Value;

@Value
public class StatisticDto {

    private final Location location;
    private final Integer count;
}
