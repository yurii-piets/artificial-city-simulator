package com.acs.document;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class AgentDocument {

    @Id
    private final Long id;

    private final Location location;

    private final AgentType type;

    private final Long vertexId;

    public AgentDocument(Agent agent) {
        this.id = agent.getId();
        this.location = agent.getLocation();
        this.type = agent.getType();
        this.vertexId = agent.getVertex() != null ? agent.getVertex().getId() : null;
    }
}


