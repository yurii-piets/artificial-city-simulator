package com.acs.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationRange {

    private Double minLat;
    private Double maxLat;

    private Double minLng;
    private Double maxLng;
}
