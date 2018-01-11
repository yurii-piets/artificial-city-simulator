package com.acs;

import com.acs.simulator.def.Simulator;
import com.acs.simulator.impl.AgentSimulator;
import com.acs.simulator.impl.LightsSimulator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext
                = SpringApplication.run(Application.class, args);

        Simulator agentSimulator = applicationContext.getBean(AgentSimulator.class);
        if (agentSimulator != null) {
            agentSimulator.simulate();
        }

        Simulator lightsSimulator = applicationContext.getBean(LightsSimulator.class);
        if (lightsSimulator != null) {
            lightsSimulator.simulate();
        }
    }
}
