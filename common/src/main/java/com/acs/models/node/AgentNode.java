package com.acs.models.node;

import com.acs.converter.LocationConverter;
import com.acs.models.Location;
import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@Data
@NodeEntity(label = "agent")
@NoArgsConstructor
@EqualsAndHashCode(of = "agentId")
public class AgentNode {

    @Id
    @GeneratedValue
    private Long id;

    private Long agentId;

    @Convert(LocationConverter.class)
    private Location location;

    private AgentType type;

    private Long vertexId;

    public AgentNode(Agent agent) {
        this.agentId = agent.getId();
        this.location = agent.getLocation();
        this.type = agent.getType();
        this.vertexId = agent.getVertex() != null ? agent.getVertex().getId() : null;
    }
}
