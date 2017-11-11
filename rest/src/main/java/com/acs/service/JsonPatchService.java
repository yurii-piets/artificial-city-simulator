package com.acs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class JsonPatchService {

    private final ObjectMapper mapper;

    @Autowired
    public JsonPatchService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> Optional patch(String updateBody, T target) throws IOException, JsonPatchException {
        JsonNode patchedNode;
        JsonPatch patch = mapper.readValue(updateBody, JsonPatch.class);
        patchedNode = patch.apply(mapper.convertValue(target, JsonNode.class));

        return Optional.ofNullable(mapper.convertValue(patchedNode, target.getClass()));
    }
}
