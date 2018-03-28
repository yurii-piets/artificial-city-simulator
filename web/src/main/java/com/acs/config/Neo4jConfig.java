package com.acs.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.acs.database.repository.neo4j")
@EnableTransactionManagement
public class Neo4jConfig {

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(configuration(),
                "com.acs.models.agent",
                "com.acs.models.graph",
                "com.acs.models.node",
                "com.acs.models.statics");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(getSessionFactory());
    }

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        return new org.neo4j.ogm.config.Configuration.Builder()
                .uri("http://127.0.0.1:7474/")
                .build();
    }
}
