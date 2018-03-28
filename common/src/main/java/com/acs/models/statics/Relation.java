package com.acs.models.statics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@NodeEntity(label = "relation")
public class Relation {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "IS_IN_RELATION")
    private List<StaticPoint> members = new ArrayList<>();

    @JsonIgnore
    private int current = 0;

    public void next() {
        if (members.size() == 0) {
            return;
        }

        current = current < members.size() - 1 ? current + 1 : 0;

        StaticPoint point = members.get(current);
        point.setLocked(!point.isLocked());
    }

    public void addStaticPoint(StaticPoint staticPoint) {
        members.add(staticPoint);
    }
}
