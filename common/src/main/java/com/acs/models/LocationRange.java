package com.acs.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationRange {

    // TODO: 13/01/2018 change fields to location
    private Double minLat;
    private Double maxLat;

    private Double minLng;
    private Double maxLng;
}
