package com.acs.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticDto {

    private final Location location;
    private final Integer count;
}
