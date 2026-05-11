package com.example.microservices.config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractContainerBaseTest {

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        // H2 database config
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        
        // Kafka mock config
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9092");
    }
}