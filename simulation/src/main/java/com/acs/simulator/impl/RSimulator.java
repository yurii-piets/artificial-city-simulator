package com.acs.simulator.impl;

import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import com.acs.models.graph.Vertex;
import com.acs.pool.def.AgentPool;
import com.acs.service.GraphService;
import com.acs.simulator.def.Simulator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Primary
@Component
public class RSimulator implements Simulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool pool;

    private final GraphService graphService;

    @Autowired
    public RSimulator(AgentPool pool,
                      GraphService graphService) {
        this.pool = pool;
        this.graphService = graphService;
    }

    @PostConstruct
    public void postConstruct() {
        initRandomAgents();
    }

    private void initRandomAgents() {
        Set<Vertex> startVertices = graphService.getGraph().getStartVertices();
        while (pool.getAgents().size() < pool.getMaxUnits()) {
            Vertex vertex = randomValueFromSet(startVertices);
            Agent agent = Agent.builder()
                    .type(AgentType.CAR)
                    .vertex(vertex)
                    .location(vertex.getLocation())
                    .build();

            vertex.setAgent(agent);
            pool.save(agent);
        }
    }

    @Async
    @Override
    public void simulate() {
        try {
            while (!Thread.interrupted()) {
                oneStep();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            logger.error("Unexpected: ", e);
        }
    }

    private void oneStep() {
        for (Agent agent : pool.getAgents()) {
            Vertex vertex = agent.getVertex();
            Set<Vertex> reachableVertices = vertex.getReachableVertices();
            Vertex nextVertex = null;
            if (reachableVertices.size() == 0) {
                logger.error("Found vertex with no reachable vertexes");
            } else if (reachableVertices.size() == 1) {
                nextVertex = reachableVertices.stream().findFirst().get();
            } else {
                // TODO: 10/12/2017 refactor this logic
                nextVertex = reachableVertices.stream().findFirst().get();
            }

            if(nextVertex != null){
                // TODO: 10/12/2017 refactor this logic
                boolean canBeAdded = nextVertex.setAgent(agent);
                if(canBeAdded) {
                    agent.setVertex(nextVertex);
                    vertex.setAgent(null);
                }
            }
        }
    }

    @Override
    public void resetSimulation() {
        pool.removeAll();
        initRandomAgents();
    }

    private Vertex randomValueFromSet(Set<Vertex> set) {
        int size = set.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (Vertex vertex : set) {
            if (i == item) {
                return vertex;
            }
            i++;
        }

        throw new IllegalStateException("Unexpected error while getting random value from set.");
    }
}
