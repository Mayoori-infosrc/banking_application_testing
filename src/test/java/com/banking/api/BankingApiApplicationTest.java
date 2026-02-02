package com.banking.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankingApiApplicationTest {

    @Test
    void testMainMethodExecutesWithoutException() {
        assertDoesNotThrow(() -> BankingApiApplication.main(new String[]{}));
    }

    @Test
    void testMainMethodCallsSpringApplicationRun() {
        try (var mocked = mockStatic(SpringApplication.class)) {
            BankingApiApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(BankingApiApplication.class, new String[]{}));
        }
    }

    @Test
    void testMainMethodWithNullArgument() {
        assertDoesNotThrow(() -> BankingApiApplication.main(null));
    }

    @Test
    void testMainMethodWithEmptyArgument() {
        assertDoesNotThrow(() -> BankingApiApplication.main(new String[]{}));
    }

    @Test
    void testMainMethodWithMultipleArguments() {
        assertDoesNotThrow(() -> BankingApiApplication.main(new String[]{"arg1", "arg2", "arg3"}));
    }

    @Test
    void testSpringBootApplicationAnnotationPresent() {
        assertTrue(BankingApiApplication.class.isAnnotationPresent(SpringBootApplication.class));
    }
}
