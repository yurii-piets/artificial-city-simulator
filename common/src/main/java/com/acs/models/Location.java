package com.acs.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private static final int MIN_LONGITUDE = -180;
    private static final int MAX_LONGITUDE = 180;

    private static final int MIN_LATITUDE = -90;
    private static final int MAX_LATITUDE = 90;

    // TODO: 28/11/2017 rename variables longitude and latitude to lng and lat
    // TODO: 04/12/2017 change type from double to bigdecimal
    @NotNull
    @Min(value = MIN_LONGITUDE)
    @Max(value = MAX_LONGITUDE)
    private Double longitude;

    @NotNull
    @Min(value = MIN_LATITUDE)
    @Max(value = MAX_LATITUDE)
    private Double latitude;
}

