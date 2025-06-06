package com.std_data_mgmt.app;

import com.std_data_mgmt.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Application-level smoke tests to verify the Spring Boot application starts correctly
 * and all major components are properly wired together.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests extends BaseIntegrationTest {
    /**
     * Smoke test: Verify Spring context loads successfully
     */
    @Test
    void contextLoads() {
    }
}