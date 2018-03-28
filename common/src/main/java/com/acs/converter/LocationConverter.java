package com.acs.converter;

import com.acs.models.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.io.IOException;

public class LocationConverter implements AttributeConverter<Location, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toGraphProperty(Location value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return value.toString();
        }
    }

    @Override
    public Location toEntityAttribute(String value) {
        try {
            return objectMapper.readValue(value, Location.class);
        } catch (IOException e) {
            return null;
        }
    }
}
