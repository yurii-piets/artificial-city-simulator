package com.acs.database.node;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@NodeEntity(label = "relation")
public class RelationNode {

    @Relationship
    private List<StaticPointNode> members = new ArrayList<>();
}
