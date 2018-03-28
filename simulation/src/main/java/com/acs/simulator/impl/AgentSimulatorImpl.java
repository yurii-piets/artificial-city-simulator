package com.acs.simulator.impl;

import com.acs.models.agent.Agent;
import com.acs.models.agent.AgentType;
import com.acs.models.graph.Vertex;
import com.acs.models.statics.StaticPoint;
import com.acs.pool.def.AgentPool;
import com.acs.service.def.GraphService;
import com.acs.simulator.def.AgentSimulator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AgentSimulatorImpl implements AgentSimulator {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AgentPool pool;

    private final GraphService graphService;

    private final Queue<Agent> queueToAppear = new LinkedBlockingQueue<>();

    private void initRandomAgents() {
        Set<Vertex> startVertices = graphService.getGraph().getStartVertices();
        while (pool.getAgents().size() < pool.getMaxUnits()) {
            createRandomAgent(startVertices);
        }
    }

    private void createRandomAgent(Collection<Vertex> startVertices) {
        Vertex vertex = randomValueFromSet(startVertices);
        if (vertex == null) {
            vertex = randomValueFromSet(graphService.getGraph().getVertices());
            if (vertex == null) {
                return;
            }
        }
        Agent agent = new Agent(vertex.getLocation(), AgentType.CAR, vertex);
        pool.save(agent);

        if (checkAndSetNextVertex(agent, vertex)) {
            vertex.setAgent(agent);
        } else {
            queueToAppear.add(agent);
        }
    }

    @Async
    @Override
    public void simulate() {
        initRandomAgents();
        initDaemonForQueueToAppear();

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
        pool.killAll();
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
            Vertex vertex = agent.getVertex();

            if (vertex != null) {
                vertex.setAgent(null);
            }

            pool.kill(agent);
            return null;
        } else if (reachableVertices.size() == 1) {
            Vertex vertex = reachableVertices.stream().findFirst().orElse(null);

            if (vertex == agent.getVertex()) {
                vertex.setAgent(null);
                pool.kill(agent);
                return null;
            }

            return vertex;
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
        if (agent == null || vertex == null) {
            return false;
        }

        if (vertex.getAgent() != null) {
            return false;
        }

        Boolean isStatusPointLocked = vertex.getStaticPoints().stream()
                .filter(StaticPoint::isLocked)
                .findFirst()
                .map(StaticPoint::isLocked)
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
        if (agent == null) {
            return;
        }

        if (vertex == null) {
            agent.setVertex(null);
            pool.kill(agent);
            createRandomAgent(graphService.getGraph().getStartVertices());
        }

        if (graphService.getGraph().getStartVertices().contains(vertex)) {
            if (vertex != null) {
                vertex.setAgent(null);
            }
            agent.setVertex(null);
            pool.kill(agent);
            createRandomAgent(graphService.getGraph().getStartVertices());
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
                logger.error("Unexpected: ", e);
            }
        }).start();
    }

    private Vertex randomValueFromSet(Collection<Vertex> set) {
        int size = set.size();
        if (size == 0) {
            return null;
        }
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
