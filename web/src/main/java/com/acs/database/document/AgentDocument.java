package com.acs.database.document;

import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "agents")
@NoArgsConstructor
public class AgentDocument {

    @Id
    private Long id;

    private Location location;

    private AgentType type;

    private Long vertexId;

    public AgentDocument(Agent agent) {
        this.id = agent.getId();
        this.location = agent.getLocation();
        this.type = agent.getType();
        this.vertexId = agent.getVertex() != null ? agent.getVertex().getId() : null;
    }
}


