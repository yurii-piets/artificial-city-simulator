package com.acs.models.statics;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Relation {

    private List<StaticPoint> members = new ArrayList<>();

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
