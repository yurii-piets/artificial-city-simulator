package com.acs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext
                = SpringApplication.run(Application.class, args);
        Simulator simulator = applicationContext.getBean(Simulator.class);
        if (simulator != null) {
            simulator.simulate();
        }
    }
}

