package com.acs.rest.controller;

import com.acs.models.agent.Agent;
import com.acs.pool.def.AgentPool;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/agents")
@RequiredArgsConstructor
public class AgentPoolController {

    private final AgentPool agentPool;

    @RequestMapping(method = RequestMethod.GET, value = "/ids")
    public ResponseEntity<Set<Long>> agentsIds() {
        Set<Long> agentIds = agentPool.getAgents().stream()
                .map(Agent::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agentIds, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/objects/v1")
    public ResponseEntity<?> agentsObjectsV1() {
        Collection<Agent> agents = agentPool.getAgents();

        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @GetMapping(value = "/objects", produces = "application/stream+json")
    public Flux<Agent> agentsObjects() {
        Collection<Agent> agents = agentPool.getAgents();

        return Flux.fromIterable(agents).delayElements(Duration.of(500L, ChronoUnit.MILLIS));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/objects")
    public ResponseEntity<Set<Agent>> agentsObjectsPost(@RequestBody Set<Long> ids) {
        Set<Agent> agents = agentPool.getAgents().stream()
                .filter(a -> ids.contains(a.getId()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dead/ids")
    public ResponseEntity<Set<Long>> deadAgents() {
        Set<Long> deadAgentsIds = agentPool.getDeadAgents().stream()
                .map(Agent::getId)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(deadAgentsIds, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/count")
    public ResponseEntity<Integer> agentsChangeSize(@RequestBody Integer count) {
        agentPool.changeAgentsAmount(count);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAll() {
        agentPool.killAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
