package com.acs.models.graph;

import com.acs.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vertex {

    private final Location location;

    private final Long id = id();

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }
}
