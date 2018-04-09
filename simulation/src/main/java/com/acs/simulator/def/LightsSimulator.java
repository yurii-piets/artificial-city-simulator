package com.acs.simulator.def;

import com.acs.models.statics.Relation;
import com.acs.models.statics.StaticPoint;

import java.util.Set;

public interface LightsSimulator extends Simulator {

    void prepare();

    void setLights(Set<StaticPoint> lights);
    void setRelations(Set<Relation> relations);
    void setWithoutRelations(Set<StaticPoint> withoutRelations);
}
