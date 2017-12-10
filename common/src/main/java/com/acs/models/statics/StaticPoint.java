package com.acs.models.statics;

import com.acs.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaticPoint {

    private final Long id = id();

    private Location location;

    private StaticType type;

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
