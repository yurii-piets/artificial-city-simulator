package com.acs.simulator.impl;

import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import com.acs.models.graph.Vertex;
import com.acs.models.statics.StaticPoint;
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
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Primary
@Component
public class RSimulator implements Simulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool pool;

    private final GraphService graphService;

    private final Queue<Agent> queueToAppear = new LinkedBlockingQueue<>();

    @Autowired
    public RSimulator(AgentPool pool,
                      GraphService graphService) {
        this.pool = pool;
        this.graphService = graphService;
    }

    @PostConstruct
    public void postConstruct() {
        initRandomAgents();
        initDaemonForQueueToAppear();
    }

    private void initRandomAgents() {
        Set<Vertex> startVertices = graphService.getGraph().getStartVertices();
        while (pool.getAgents().size() < pool.getMaxUnits()) {
            Vertex vertex = randomValueFromSet(startVertices);
            Agent agent = Agent.builder()
                    .type(AgentType.CAR)
                    .location(vertex.getLocation())
                    .build();

            agent.setVertex(vertex);
            pool.save(agent);

            if (checkAndSetNextVertex(agent, vertex)) {
                vertex.setAgent(agent);
            } else {
                queueToAppear.add(agent);
            }
        }
    }

    @Async
    @Override
    public void simulate() {
        try {
            while (!Thread.interrupted()) {
                oneStep();
                TimeUnit.MILLISECONDS.sleep(500);
            }
        } catch (InterruptedException e) {
            logger.error("Unexpected: ", e);
        }
    }

    @Override
    public void resetSimulation() {
        pool.removeAll();
        initRandomAgents();
    }

    private void oneStep() {
        for (Agent agent : pool.getAgents()) {
            Vertex nextVertex = calculateNextVertex(agent);
            checkAndSetNextVertex(agent, nextVertex);
            checkIfIsInBound(agent, nextVertex);
        }
    }

    private Vertex calculateNextVertex(Agent agent) {
        Set<Vertex> reachableVertices = agent.getVertex().getReachableVertices();
        if (reachableVertices.size() == 0) {
            logger.error("Vertex with no reachable vertices: " + agent.getVertex().getId());
            return agent.getVertex();
        } else if (reachableVertices.size() == 1) {
            return reachableVertices.stream().findFirst().get();
        } else {
            int size = reachableVertices.size();
            int item = new Random().nextInt(size);
            int i = 0;
            for (Vertex vertex : reachableVertices) {
                if (i == item) {
                    return vertex;
                }
                i++;
            }
        }

        return null;
    }

    private boolean checkAndSetNextVertex(Agent agent, Vertex vertex) {
        if (vertex.getAgent() != null) {
            return false;
        }

        Boolean isStatusPointLocked = vertex.getStaticPoints().stream()
                .filter(StaticPoint::getLocked)
                .findFirst()
                .map(StaticPoint::getLocked)
                .orElse(false);

        if (isStatusPointLocked) {
            return false;
        }

        Vertex prevVertex = agent.getVertex();
        if (prevVertex != null) {
            prevVertex.setAgent(null);
        }

        agent.setVertex(vertex);
        vertex.setAgent(agent);

        return true;
    }

    private void checkIfIsInBound(Agent agent, Vertex vertex) {
        if(graphService.getGraph().getStartVertices().contains(vertex)) {
            vertex.setAgent(null);
            agent.setVertex(null);
            pool.removeById(agent.getId());
        }
    }

    private void initDaemonForQueueToAppear() {
        new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    if (queueToAppear.isEmpty()) {
                        TimeUnit.SECONDS.sleep(5);
                    } else {
                        while (!queueToAppear.isEmpty()) {
                            Agent agent = queueToAppear.poll();
                            Vertex vertex = agent.getVertex();
                            if (vertex != null) {
                                vertex.setAgent(agent);
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Vertex randomValueFromSet(Set<Vertex> set) {
        int size = set.size();
        int item = new Random().nextInt(size);
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
