package com.acs;

import com.acs.simulator.def.Simulator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext
                = SpringApplication.run(Application.class, args);
        Simulator agentPool = applicationContext.getBean(Simulator.class);
        if (agentPool != null) {
            agentPool.simulate();
        }
    }
}

