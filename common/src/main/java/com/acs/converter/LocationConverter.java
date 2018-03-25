package com.acs.converter;

import com.acs.models.Location;
import org.neo4j.ogm.typeconversion.AttributeConverter;

public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    public String toGraphProperty(Location value) {
        return value.toString();
    }

    @Override
    public Location toEntityAttribute(String value) {
        return null;
    }
}
