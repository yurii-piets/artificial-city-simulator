package com.acs.simulator.def;

public interface Simulator {

    void simulate();

    void changeAgentsAmount(Integer count);
    void resetSimulation();
}
