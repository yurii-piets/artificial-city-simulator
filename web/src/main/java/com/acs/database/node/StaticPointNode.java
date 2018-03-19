package com.acs.database.node;

import com.acs.models.Location;
import com.acs.models.statics.StaticType;
import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Data
@NodeEntity(label = "static_point")
public class StaticPointNode {

    @Id
    private Long id;

    private Location location;

    private StaticType type;

    private Boolean locked;

    @Relationship
    private RelationNode relation;

    @Relationship
    private VertexNode vertex;
}
