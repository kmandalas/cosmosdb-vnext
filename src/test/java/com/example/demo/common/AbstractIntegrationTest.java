package com.example.demo.common;

import com.example.demo.DemoApplication;
import com.example.demo.utils.CosmosDBSSLHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootTest(classes = DemoApplication.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    @Container
    static CosmosDBEmulatorContainer cosmosDbContainer = new CosmosDBEmulatorContainer(
            DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:vnext-preview"))
            .withCommand("--protocol", "https")
            .waitingFor(Wait.forHttps("/").forStatusCode(200).allowInsecure())
            .withStartupTimeout(Duration.ofMinutes(1));

    @DynamicPropertySource
    static void defineCosmos(DynamicPropertyRegistry registry) throws Exception {
        cosmosDbContainer.start();

        // Set up the trust store
        CosmosDBSSLHelper.setupTrustStore(cosmosDbContainer.getHost(), cosmosDbContainer.getFirstMappedPort(),
                cosmosDbContainer.getEmulatorKey());

        registry.add("spring.cloud.azure.cosmos.endpoint", cosmosDbContainer::getEmulatorEndpoint);
        registry.add("spring.cloud.azure.cosmos.key", cosmosDbContainer::getEmulatorKey);
    }

}
