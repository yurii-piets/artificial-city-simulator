package com.acs.database.node;

import com.acs.models.Location;
import com.acs.models.agent.AgentType;
import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Data
@NodeEntity(label = "agent")
public class AgentNode {

    @Id
    private Long id;

    private Location location;

    private AgentType type;

    @Relationship
    private VertexNode vertex;
}
