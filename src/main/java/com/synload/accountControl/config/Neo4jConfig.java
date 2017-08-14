package com.synload.accountControl.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Nathaniel on 7/23/2017.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.synload.accountControl")
@EnableNeo4jRepositories("com.synload.accountControl.repository.neo4j")
@PropertySource("classpath:ogm.properties")
public class Neo4jConfig {
    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory("com.synload.accountControl.domain");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }
}
