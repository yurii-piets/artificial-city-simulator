package com.acs.service.def;

import com.acs.models.graph.Graph;

public interface GraphService {

    void setGraph(Graph graph);

    Graph getGraph();

    void processGraph();
}
