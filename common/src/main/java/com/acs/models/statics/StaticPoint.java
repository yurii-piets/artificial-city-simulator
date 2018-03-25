package com.acs.models.statics;

import com.acs.models.Location;
import com.acs.models.graph.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Data
@NodeEntity
@EqualsAndHashCode(of = "id")
public class StaticPoint implements Comparable<StaticPoint> {

    @Id
    private Long id;

    private Location location;

    private StaticType type;

    private volatile boolean locked = false;

    @JsonIgnore
    @Relationship
    private Relation relation;

    @JsonIgnore
    @Relationship
    private Vertex vertex;

    public StaticPoint() {
        this.id = id();
    }

    public StaticPoint(Location location, StaticType type) {
        this.location = location;
        this.type = type;
        this.id = id();
    }

    public void setId(Long id) {
        this.id = id;
        if (id >= staticId) {
            staticId = id + 1;
        }
    }

    private static Long staticId = 1L;

    private static Long id() {
        return staticId++;
    }

    @Override
    public int compareTo(StaticPoint o) {
        return id.compareTo(o.getId());
    }
}
